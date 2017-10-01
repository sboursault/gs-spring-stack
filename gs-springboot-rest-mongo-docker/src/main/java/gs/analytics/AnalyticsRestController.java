package gs.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * This controller exposes analytics data
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsRestController {

    private final static Pattern TIME_QUERY_PART = Pattern.compile("^(\\d+)([hms])$");

    @Autowired
    private StringRedisTemplate strRedisTemplate;

    @ExceptionHandler({InvalidQueryException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody VndErrors handleNotReadable(InvalidQueryException e) {
        return new VndErrors("error", e.getMessage());
    }

    @GetMapping
    public List<Entry<String, Long>> query(@RequestParam("q") String query) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime queryTime = getTime(query);
        ZSetOperations<String, String> zset = strRedisTemplate.opsForZSet();
        List<String> keys = new ArrayList<>(strRedisTemplate.keys("operation:*:analytics"));
        List<Object> counts = countPipelined(keys, toLong(queryTime), toLong(now));
        Map<String, Long> analytics = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            long count = (Long) counts.get(i);
            if (count > 0) {
                analytics.put(keys.get(i), count);
            }
        }
        return analytics.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());
    }

    /*
     * sends a a serie of zcount commands at once, without waiting for a response between each commands.
     */
    private List<Object> countPipelined(List<String> keys, double min, double max) {
        return strRedisTemplate.executePipelined(
            new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisConnection strRedisConnection = (StringRedisConnection)connection;
                    for (String key : keys) {
                        strRedisConnection.zCount(key, min, max);
                    }
                    return null;
                }
            });
    }

    private LocalDateTime getTime(String query) {
        LocalDateTime requestTime = LocalDateTime.now();
        String[] parts = query.trim().replaceAll("\\s+", " ").split(" ");
        for (String part : parts) {
            Matcher m = TIME_QUERY_PART.matcher(part);
            if (!m.matches()) {
                throw new InvalidQueryException("could not interpret query with : '" + part + "'. Expected something like 'q=1h 30m'"); // TODO return a proper VendorError
            }
            ChronoUnit unit = m.group(2).equals("h") ? ChronoUnit.HOURS : m.group(2).equals("m") ? ChronoUnit.MINUTES : ChronoUnit.SECONDS;
            requestTime = requestTime.minus(Long.valueOf(m.group(1)), unit);
        }
        return requestTime;
    }

    private long toLong(LocalDateTime now) {
        return now.atOffset(ZoneOffset.ofTotalSeconds(0)).toInstant().toEpochMilli();
    }
}
