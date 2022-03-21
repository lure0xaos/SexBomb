package gargoyle.sexbomb.util.res

import gargoyle.sexbomb.util.log.Log
import gargoyle.sexbomb.util.res.cache.LoadCache
import gargoyle.sexbomb.util.res.load.Loaders
import java.io.IOException
import java.net.URL
import java.util.Objects

class Resources {
    private val cache: LoadCache = LoadCache.GLOBAL
    private val roots: MutableCollection<String> = mutableSetOf()
    private val useCache: Boolean

    //    {
    //        roots.add(Resources.classUrl(Resources.class).toExternalForm());
    //    }
    constructor(useCache: Boolean, roots: Iterable<URL>) {
        this.useCache = useCache
        for (url in roots) {
            this.roots.add(url.toExternalForm())
        }
    }

    constructor(useCache: Boolean, vararg roots: URL) {
        this.useCache = useCache
        for (url in roots) {
            this.roots.add(url.toExternalForm())
        }
    }

    fun addRoot(url: URL) {
        roots.add(url.toExternalForm())
    }

    fun addRoot(clazz: Class<*>, vararg names: String) {
        Objects.requireNonNull(clazz)
        val urls: MutableCollection<URL> = mutableSetOf()
        if (names.isEmpty()) {
            urls.add(Res.classUrl(clazz))
        } else {
            for (name in names) {
                val element = Res.nearClassUrl(clazz, name)
                if (element != null) urls.add(element)
            }
        }
        for (url in urls) {
            roots.add(url.toExternalForm())
        }
    }

    fun <R> load(type: Class<R>, vararg urls: URL): R? {
        Objects.requireNonNull(type)
        for (url in urls) {
            if (!Res.isUrlOk(url)) {
                continue
            }
            if (useCache) {
                val cachedResource = cache[type, url]
                if (cachedResource != null) {
                    return cachedResource
                }
            }
            var resource: R? = null
            try {
                resource = Loaders.load(type, url)
            } catch (e: IOException) {
                Log.warn(String.format("cannot load %s from %s", type.simpleName, url), e)
            }
            if (resource != null) {
                Log.info(String.format("loaded %s", url))
                return resource
            }
        }
        Log.warn(String.format("%s not found", urls.contentToString()))
        return null
    }

    fun <R> load(type: Class<R>, vararg names: String): R? {
        return load(type, *urls(true, *names))
    }

    fun url(check: Boolean, base: URL, value: String): URL? {
        val urls = urls(check, value)
        for (res in urls) {
            return res
        }
        return Res.url(base, value)
    }

    fun url(check: Boolean, vararg names: String): URL {
        return (urls(check, *names)).first()
    }

    fun urls(check: Boolean, vararg names: String): Array<URL> {
        val urls0: MutableCollection<URL> = mutableSetOf()
        for (location in roots) {
            for (name in names) {
                val locationUrl = Res.toURL(location) ?: continue
                val okUrls: MutableCollection<URL> = ArrayList()
                val locationNameUrl = Res.nearURL(locationUrl, name)
                if (locationNameUrl != null) {
                    if (!check || Res.isUrlOk(locationNameUrl)) {
                        okUrls.add(locationNameUrl)
                    }
                }
                val subUrl = Res.subUrl(locationUrl, name)
                if (subUrl != null) {
                    if (!check || Res.isUrlOk(subUrl)) {
                        okUrls.add(subUrl)
                    }
                }
                for (url2 in okUrls) {
                    if (!check || Res.isUrlOk(url2)) {
                        urls0.add(url2)
                    }
                }
            }
        }
        return urls0.toTypedArray()
    }


}
