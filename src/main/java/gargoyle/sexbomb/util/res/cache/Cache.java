package gargoyle.sexbomb.util.res.cache;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class Cache {
    public static final Cache GLOBAL = new Cache();
    private final Map<String, Object> cache = Collections.synchronizedMap(new WeakHashMap<>());

    @SuppressWarnings("unchecked")
    public <R> R get(String location) {
        return (R) cache.get(location);
    }

    public <R> void put(String location, R resource) {
        cache.put(location, resource);
    }
}
