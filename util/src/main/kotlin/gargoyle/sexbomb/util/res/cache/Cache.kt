package gargoyle.sexbomb.util.res.cache

import java.util.*

class Cache {
    private val cache = Collections.synchronizedMap(WeakHashMap<String, Any>())

    @Suppress("UNCHECKED_CAST")
    operator fun <R : Any> get(location: String): R? = cache[location] as R?

    fun <R : Any> put(location: String, resource: R?): R? =
        resource?.also { cache[location] = it }

    operator fun <R : Any> get(location: String, function: () -> R?): R? =
        get(location) ?: (function()?.also { put(location, it) })

}
