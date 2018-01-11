package gargoyle.sexbomb.util.applet

import gargoyle.sexbomb.util.log.Log
import gargoyle.sexbomb.util.res.audio.Audio
import gargoyle.sexbomb.util.res.audio.AudioClip
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingUtilities
import javax.swing.border.BevelBorder

internal class GFrame(private val applet: GApplet, args: Array<String>) : JFrame(),
    AppletStub, AppletContext {
    private val parameters: Map<String, String>
    private val streams: MutableMap<String, InputStream> = mutableMapOf()
    private val status: JLabel

    init {
        applet.application = this
        applet.stub = this
        parameters = mapOf(*args.map { param -> param.split('=').let { it[0] to it[1] } }.toTypedArray())
        layout = BorderLayout()
        title = applet.title
        add(applet, BorderLayout.CENTER)
        status = JLabel(STR_READY)
            .also { it.border = BorderFactory.createBevelBorder(BevelBorder.LOWERED) }
            .also { add(it, BorderLayout.SOUTH) }
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowOpened(e: WindowEvent) = applet.init()
            override fun windowActivated(e: WindowEvent) = applet.start()
            override fun windowDeactivated(e: WindowEvent) = applet.stop()
            override fun windowClosed(e: WindowEvent) {}
            override fun windowClosing(event: WindowEvent) {
                try {
                    if (applet.canExit()) {
                        applet.destroy()
                        isVisible = false
                        dispose()
                    } else {
                        if (!isVisible) isVisible = true
                    }
                } catch (e: RuntimeException) {
                    applet.error(e)
                    dispose()
                }
            }
        })
        applet.preferredSize = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds.size
            .let { Dimension(it.width / 2, it.height / 2) }
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

    override fun getAudioClip(url: URL): AudioClip =
        Audio.newAudioClip(url)

    override fun getImage(url: URL): Image =
        ImageIO.read(url)

    override fun getApplet(name: String): Applet? {
        return applets.toList().firstOrNull { it.getParameter("name") == name }
    }

    override val applets: Enumeration<Applet>
        get() = Collections.enumeration(listOf(applet as Applet))
    override val documentBase: URL?
        get() = try {
            URL(File(System.getProperty("user.dir", ".")).canonicalFile.toURI().toURL(), "")
        } catch (e: IOException) {
            Log.error(e, e.localizedMessage)
            null
        }
    override val codeBase: URL?
        get() = try {
            javaClass.protectionDomain.codeSource.location
        } catch (e: RuntimeException) {
            Log.error(e, e.localizedMessage)
            null
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

    override fun showDocument(url: URL, target: String) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(url.toURI())
            }
        } catch (e: IOException) {
            Log.error(e, e.localizedMessage)
        } catch (e: URISyntaxException) {
            Log.error(e, e.localizedMessage)
        }
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
            val location = size.location
            val c = Point(getX() + getWidth() / 2, getY() + getHeight() / 2)
            setLocation(location)
            pack()
            val c2 = Point(getX() + getWidth() / 2, getY() + getHeight() / 2)
            setLocation(getX() - (c2.x - c.x), getY() - (c2.y - c.y))
        }
    }

    fun showMe() {
        SwingUtilities.invokeLater {
            isVisible = true
        }
    }


    companion object {
        private const val STR_READY = "Ready"
    }
}
