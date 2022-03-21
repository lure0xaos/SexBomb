package gargoyle.sexbomb.util.applet

import java.awt.Image
import java.io.InputStream
import java.net.URL
import java.util.Enumeration
import javax.swing.JMenuBar
import javax.swing.JPanel

open class JApplet : JPanel(), Applet {
    val appletContext: AppletContext = AppletContextImpl()
    private lateinit var stub: AppletStub
    fun appletResize(width: Int, height: Int) {
        stub.appletResize(width, height)
    }

    fun getApplet(name: String): Applet {
        return stub.getApplet(name)
    }

    val applets: Enumeration<Applet>
        get() = stub.applets

    fun getAudioClip(url: URL): AudioClip {
        return stub.getAudioClip(url)
    }

    val codeBase: URL?
        get() = stub.codeBase
    val documentBase: URL?
        get() = stub.documentBase

    fun getImage(url: URL): Image {
        return stub.getImage(url)
    }

    fun getParameter(name: String): String? {
        return stub.getParameter(name)
    }

    fun getStream(key: String): InputStream? {
        return stub.getStream(key)
    }

    val streamKeys: Iterator<String>
        get() = stub.streamKeys

    fun setStream(key: String, stream: InputStream) {
        stub.setStream(key, stream)
    }

    override fun setStub(stub: AppletStub) {
        this.stub = stub
    }

    override fun init() {}
    override fun start() {}
    override fun stop() {}
    override fun destroy() {}
    protected open fun showDocument(url: URL) {
        stub.showDocument(url)
    }

    protected open fun showDocument(url: URL, target: String) {
        stub.showDocument(url, target)
    }

    protected fun showStatus(status: String) {
        stub.showStatus(status)
    }

    protected fun setJMenuBar(bar: JMenuBar) {
        super.getRootPane().jMenuBar = bar
    }
}
