package gargoyle.sexbomb.util.applet

import gargoyle.sexbomb.util.log.Log
import gargoyle.sexbomb.util.res.Resources
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.Rectangle
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URISyntaxException
import java.net.URL
import java.util.Collections
import java.util.Enumeration
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingUtilities
import javax.swing.border.BevelBorder

class GFrame(private val applet: G0Applet, private val resources: Resources, args: Array<String>) : JFrame(),
    WindowListener, AppletStub, AppletContext {
    private val parameters: MutableMap<String, String> = HashMap()
    private val status: JLabel
    private val streams: MutableMap<String, InputStream> = HashMap()

    init {
        applet.application = this
        applet.setStub(this)
        setArgs(args)
        title = applet.title
        layout = BorderLayout()
        add(applet, BorderLayout.CENTER)
        status = JLabel(STR_READY)
        status.border = BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        add(status, BorderLayout.SOUTH)
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(this)
        val size = Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds.size)
        size.width = size.width shr 1
        size.height = size.height shr 1
        applet.preferredSize = size
        pack()
        setLocationRelativeTo(null)
    }

    fun exit() {
        applet.destroy()
        SwingUtilities.invokeLater {
            isVisible = false
            dispose()
        }
    }

    override fun getAudioClip(url: URL): AudioClip {
        return resources.load(AudioClip::class.java, url)!!
    }

    override fun getImage(url: URL): Image {
        return resources.load(Image::class.java, url)!!
    }

    override fun getApplet(name: String): Applet {
        return applet
    }

    override val applets: Enumeration<Applet>
        get() = Collections.enumeration(
            listOf(
                applet
            )
        )

    override fun showDocument(url: URL) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(url.toURI())
            }
        } catch (e: IOException) {
            Log.warn(e.localizedMessage)
        } catch (e: URISyntaxException) {
            Log.warn(e.localizedMessage)
        }
    }

    override fun showDocument(url: URL, target: String) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(url.toURI())
            }
        } catch (e: IOException) {
            Log.warn(e.localizedMessage)
        } catch (e: URISyntaxException) {
            Log.warn(e.localizedMessage)
        }
    }

    override fun showStatus(status: String) {
        if (isVisible) {
            this.status.text = status
        }
    }

    override fun setStream(key: String, stream: InputStream) {
        streams[key] = stream
    }

    override fun getStream(key: String): InputStream? {
        return streams[key]
    }

    override val streamKeys: Iterator<String>
        get() = streams.keys.iterator()
    override val documentBase: URL?
        get() = try {
            URL(File(System.getProperty("user.dir", ".")).canonicalFile.toURI().toURL(), "")
        } catch (e: IOException) {
            Log.error(e.localizedMessage, e)
            null
        }
    override val codeBase: URL?
        get() = try {
            javaClass.protectionDomain.codeSource.location
        } catch (e: RuntimeException) {
            Log.error(e.localizedMessage, e)
            null
        }

    override fun getParameter(name: String): String? {
        return parameters[name]
    }

    override val appletContext: AppletContext
        get() = this

    override fun appletResize(width: Int, height: Int) {
        synchronized(treeLock) {
            val screen = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
            val size = Rectangle(location, applet.minimumSize)
            if (width > size.width) {
                size.width = width
            }
            if (height > size.height) {
                size.height = height
            }
            if (size.width > screen.width) {
                size.width = screen.width
            }
            if (size.height > screen.height) {
                size.height = screen.height
            }
            val x = screen.x + screen.width - size.width
            if (size.x > x) {
                size.x = x
            }
            val y = screen.y + screen.height - size.height
            if (size.y > y) {
                size.y = y
            }
            applet.preferredSize = size.size
            applet.size = size.size
            location = size.location
            pack()
        }
    }

    private fun setArgs(args: Array<String>) {
        for (arg in args) {
            val split = arg.split("=").toTypedArray()
            parameters[split[0]] = split[1]
        }
    }

    fun showMe() {
        SwingUtilities.invokeLater {
            isVisible = true
            applet.init()
        }
    }

    override fun windowOpened(e: WindowEvent) {}
    override fun windowClosing(e: WindowEvent) {
        try {
            if (applet.canExit()) {
                applet.exit()
            } else {
                if (!isVisible) {
                    isVisible = true
                }
            }
        } catch (e1: RuntimeException) {
            applet.error(e1)
            dispose()
        }
    }

    override fun windowClosed(e: WindowEvent) {}
    override fun windowIconified(e: WindowEvent) {}
    override fun windowDeiconified(e: WindowEvent) {}
    override fun windowActivated(e: WindowEvent) {
        applet.start()
    }

    override fun windowDeactivated(e: WindowEvent) {
        applet.stop()
    }

    companion object {
        private const val STR_READY = "Ready"
    }
}
