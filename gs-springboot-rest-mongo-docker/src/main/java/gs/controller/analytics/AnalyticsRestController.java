package gs.controller.analytics;

import gs.controller.InmatesResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analytics")
public class AnalyticsRestController {

    @Autowired
    private StringRedisTemplate strRedisTemplate;
    private boolean DESC;

    @GetMapping
    public Map<String, Integer> findAll(@RequestParam("q") String query) {
        LocalDateTime time, now = LocalDateTime.now();
        if (query.equals("1h")) {   // TODO handle other values
            time = now.minusHours(1);
        } else {
            throw new IllegalArgumentException("q value must be '1h'"); // TODO return a proper VendorError
        }

        ZSetOperations<String, String> zset = strRedisTemplate.opsForZSet();

        Set<String> keys = strRedisTemplate.keys("operation:*:analytics");
        Map<String, Integer> analytics = keys.stream()
                .collect(Collectors.toMap(key -> key.split(":")[1], key -> zset.rangeByScore(key, toLong(time), toLong(now)).size()));
        return sortByComparator(analytics, DESC) ;
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private long toLong(LocalDateTime now) {
        return now.atOffset(ZoneOffset.ofTotalSeconds(0)).toInstant().toEpochMilli();
    }
}
