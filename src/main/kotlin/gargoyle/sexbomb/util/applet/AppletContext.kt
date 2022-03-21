package gargoyle.sexbomb.util.applet

import java.net.URL

interface AppletContext {
    fun showDocument(url: URL)
    fun showDocument(url: URL, target: String)
}
