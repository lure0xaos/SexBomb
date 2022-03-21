package gargoyle.sexbomb.util.res.load

import gargoyle.sexbomb.util.applet.Audio
import gargoyle.sexbomb.util.applet.AudioClip
import gargoyle.sexbomb.util.log.Log
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

object Loaders {
    private val loaders: MutableMap<Class<*>?, Loader<*>> = HashMap()

    init {
        loaders[AudioClip::class.java] = Loader<Any> { obj: URL -> Audio.newAudioClip(obj) }
        loaders[Image::class.java] = Loader<Any> { input: URL -> ImageIO.read(input) }
        loaders[BufferedImage::class.java] =
            Loader<Any> { input: URL? -> ImageIO.read(input) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getLoader(type: Class<T>?): Loader<T>? {
        return loaders[type] as Loader<T>?
    }

    @Throws(IOException::class)
    fun <R> load(type: Class<R>, url: URL): R {
        val loader = getLoader(type)
            ?: throw IOException(String.format("don't know how to load %s", type.simpleName))
        return loader.load(url)
    }

    fun <R> tryLoad(type: Class<R>, url: URL): R? {
        return try {
            load(type, url)
        } catch (e: IOException) {
            Log.error(String.format("cannot load %s from %s", type.simpleName, url))
            null
        }
    }
}
