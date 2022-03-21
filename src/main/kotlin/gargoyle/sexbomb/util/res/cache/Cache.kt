package gargoyle.sexbomb.util.res.cache

import java.util.Collections
import java.util.WeakHashMap

class Cache {
    private val cache = Collections.synchronizedMap(WeakHashMap<String, Any>())

    @Suppress("UNCHECKED_CAST")
    operator fun <R> get(location: String): R? {
        return cache[location] as R?
    }

    fun <R> put(location: String, resource: R) {
        cache[location] = resource
    }

    companion object {
        val GLOBAL = Cache()
    }
}
