package gargoyle.sexbomb.util.applet

import java.awt.Desktop
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL

internal class AppletContextImpl : AppletContext {
    override fun showDocument(url: URL) {
        try {
            Desktop.getDesktop().browse(url.toURI())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    override fun showDocument(url: URL, target: String) {
        try {
            Desktop.getDesktop().browse(url.toURI())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
}
