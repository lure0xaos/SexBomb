package gargoyle.sexbomb.util.res.cache

import gargoyle.sexbomb.util.res.load.Loaders
import java.net.URL

class LoadCache {
    private val cache = Cache()
    operator fun <R> get(type: Class<R>, location: URL): R? {
        var resource = cache.get<R>(location.toExternalForm())
        if (resource == null) {
            resource = Loaders.tryLoad(type, location)
            if (resource != null) {
                cache.put(location.toExternalForm(), resource)
            }
        }
        return resource
    }

    companion object {
        val GLOBAL = LoadCache()
    }
}
