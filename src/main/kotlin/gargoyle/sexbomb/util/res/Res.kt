package gargoyle.sexbomb.util.res

import gargoyle.sexbomb.util.log.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object Res {
    fun classUrl(clazz: Class<*>): URL {
        return requireNotNull(clazz.classLoader.getResource(String.format("%s.class", clazz.name.replace('.', '/'))))
    }

    fun isUrlOk(url: URL): Boolean {
        return try {
            val urlConnection = url.openConnection()
            if (urlConnection is HttpURLConnection) {
                urlConnection.responseCode / 100 == 2
            } else urlConnection.contentLength > 0
        } catch (e: IOException) {
            false
        }
    }

    fun nearClassUrl(clazz: Class<*>, name: String): URL? {
        return nearURL(classUrl(clazz), name)
    }

    fun nearURL(base: URL, name: String): URL? {
        return try {
            URL(base, name)
        } catch (e: MalformedURLException) {
            Log.debug(String.format("%s:%s", base, name))
            null
        }
    }

    fun subUrl(url: URL, name: String): URL? {
        return toURL(String.format("%s/%s", url.toExternalForm(), name))
    }

    fun toURL(url: String): URL? {
        return try {
            URL(url)
        } catch (e: MalformedURLException) {
            Log.debug(url)
            null
        }
    }

    fun url(base: URL, value: String?): URL? {
        if (value == null) {
            Log.debug("resource null not found")
            return null
        }
        var res = toURL(value)
        if (res != null && isUrlOk(res)) {
            return res
        }
        res = nearURL(base, value)
        if (res != null && isUrlOk(res)) {
            return res
        }
        Log.debug(String.format("resource %s not found", value))
        return null
    }
}
