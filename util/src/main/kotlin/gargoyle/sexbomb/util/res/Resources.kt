package gargoyle.sexbomb.util.res

import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.io.path.Path
import kotlin.reflect.KClass

object Resources {

    fun resolveUrl(clazz: KClass<*>, name: String): URL? =
        clazz.java.classLoader.getResource(resolveName(clazz, name))

    fun resolveName(clazz: KClass<*>, name: String): String =
        if (name.startsWith('/')) name.trimStart('/')
        else "${clazz.qualifiedName!!.substringBeforeLast('.').replace('.', '/')}/$name"

    fun resolveUrl(url: URL, name: String): URL? =
        try {
            URL("${url.toExternalForm()}/$name")
        } catch (e: MalformedURLException) {
            null
        }

    fun resolveSiblingUrl(url: URL, name: String): URL? =
        try {
            URL(url, name)
        } catch (e: MalformedURLException) {
            null
        }

    fun findExternal(name: String): URL? =
        arrayOf(
            Path(System.getProperty("user.dir"), name),
            Path(System.getProperty("user.home"), name)
        ).map { it.toUri().toURL() }.firstOrNull { existsUrl(it) }

    fun existsUrl(url: URL?): Boolean =
        when (url) {
            null -> false
            else -> try {
                val urlConnection = url.openConnection()
                if (urlConnection is HttpURLConnection) {
                    urlConnection.responseCode / 100 == 2
                } else urlConnection.contentLength > 0
            } catch (e: Exception) {
                false
            }
        }

}
