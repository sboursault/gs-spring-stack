package gs.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * This controller exposes analytics data
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsRestController {

    private final static Pattern TIME_QUERY_PART = Pattern.compile("^(\\d*)([hms])$");

    @Autowired
    private StringRedisTemplate strRedisTemplate;

    @ExceptionHandler({InvalidQueryException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody VndErrors handleNotReadable(InvalidQueryException e) {
        return new VndErrors("error", e.getMessage());
    }

    @GetMapping
    public List<Map.Entry<String, Long>> query(@RequestParam("q") String query) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime queryTime = getTime(query);
        ZSetOperations<String, String> zset = strRedisTemplate.opsForZSet();
        Set<String> keys = strRedisTemplate.keys("operation:*:analytics");
        List<Map.Entry<String, Long>> analytics = keys.stream()
                .collect(Collectors.toMap(key -> key.split(":")[1], key -> zset.count(key, toLong(queryTime), toLong(now))))
                .entrySet().stream()
                .filter(entry -> entry.getValue() != 0l)
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());
        return analytics;
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
