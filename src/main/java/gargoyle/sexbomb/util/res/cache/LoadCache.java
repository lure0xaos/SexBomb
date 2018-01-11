package gargoyle.sexbomb.util.res.cache;

import gargoyle.sexbomb.util.res.load.Loaders;

import java.net.URL;

public final class LoadCache {
    public static final LoadCache GLOBAL = new LoadCache();
    private final Cache cache = new Cache();

    public <R> R get(Class<R> type, URL location) {
        R resource = cache.get(location.toExternalForm());
        if (resource == null) {
            resource = Loaders.tryLoad(type, location);
            if (resource != null) {
                cache.put(location.toExternalForm(), resource);
            }
        }
        return resource;
    }
}
