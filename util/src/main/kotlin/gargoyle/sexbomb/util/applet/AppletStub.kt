package gargoyle.sexbomb.util.applet

import java.net.URL

interface AppletStub {
    val appletContext: AppletContext
    val codeBase: URL?
    val documentBase: URL?
    fun appletResize(width: Int, height: Int)
    fun getParameter(name: String): String?
    fun isActive(): Boolean
}
