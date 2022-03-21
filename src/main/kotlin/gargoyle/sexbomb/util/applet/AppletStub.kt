package gargoyle.sexbomb.util.applet

import java.awt.Image
import java.io.InputStream
import java.net.URL
import java.util.Enumeration

interface AppletStub {
    fun getAudioClip(url: URL): AudioClip
    fun getImage(url: URL): Image
    fun getApplet(name: String): Applet
    val applets: Enumeration<Applet>
    fun showDocument(url: URL)
    fun showDocument(url: URL, target: String)
    fun showStatus(status: String)
    fun setStream(key: String, stream: InputStream)
    fun getStream(key: String): InputStream?
    val streamKeys: Iterator<String>
    val documentBase: URL?
    val codeBase: URL?
    fun getParameter(name: String): String?
    val appletContext: AppletContext
    fun appletResize(width: Int, height: Int)
}
