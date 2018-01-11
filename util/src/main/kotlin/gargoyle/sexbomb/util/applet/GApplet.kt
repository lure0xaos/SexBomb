package gargoyle.sexbomb.util.applet

import gargoyle.sexbomb.util.log.Log
import java.awt.Component
import java.awt.Dialog
import java.awt.Frame
import java.net.URL
import javax.swing.*
import kotlin.reflect.KClass

abstract class GApplet protected constructor() : JApplet() {

    internal lateinit var application: GFrame


    protected val rootFrame: Frame?
        get() {
            val parent = SwingUtilities.getRoot(this)
            return if (parent is Frame) {
                parent
            } else null
        }

    fun showDocument(url: URL) {
        appletContext.showDocument(url)
    }

    fun showDocument(url: URL, target: String) {
        appletContext.showDocument(url, target)
    }

    protected fun ask(message: String): Boolean =
        JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION

    open fun canExit(): Boolean = isApplication && ask(STR_EXIT)

    protected abstract fun doDestroy()
    protected abstract fun doInit()

    fun error(message: Throwable) {
        Log.error(message, message.localizedMessage)
        JOptionPane.showMessageDialog(
            this,
            "${message::class.simpleName}: ${message.localizedMessage}",
            title,
            JOptionPane.ERROR_MESSAGE
        )
    }

    fun exit() {
        application.exit()
    }

    val title: String
        get() = SwingUtilities.getRoot(this).let { if (it is Frame) it.title else javaClass.simpleName }

    override fun init() {
        isFocusable = true
        try {
            doInit()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    protected open fun doStart() {}
    protected open fun doStop() {}

    override fun start() {
        try {
            doStart()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    override fun stop() {
        try {
            doStop()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    override fun destroy() {
        try {
            doDestroy()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    protected val isApplication: Boolean
        get() = true

    protected fun prompt(message: String): String =
        JOptionPane.showInputDialog(this, message, title, JOptionPane.QUESTION_MESSAGE)

    companion object {
        private const val STR_EXIT = "Exit?"

        init {
            Log.debug("")
        }

        fun changeLookAndFeel(laf: String, component: Component?): Unit =
            try {
                var decor = UIManager.getLookAndFeel().supportsWindowDecorations
                if (component != null) {
                    decor = decor and (SwingUtilities.getRootPane(component).windowDecorationStyle != JRootPane.NONE)
                }
                UIManager.setLookAndFeel(laf)
                JFrame.setDefaultLookAndFeelDecorated(decor)
                JDialog.setDefaultLookAndFeelDecorated(decor)
                if (component != null) {
                    val frame = SwingUtilities.getWindowAncestor(component)
                    if (!frame.isDisplayable) {
                        if (frame is Frame) {
                            frame.isUndecorated = !decor
                        }
                        if (frame is Dialog) {
                            frame.isUndecorated = !decor
                        }
                    }
                }
                if (component != null) {
                    SwingUtilities.updateComponentTreeUI(component)
                } else {
                    Unit
                }
            } catch (e: Exception) {
                Log.error(e, e.localizedMessage)
            }

        fun run(clazz: KClass<out GApplet>, args: Array<String>): Unit =
            try {
                changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), null)
                val applet = clazz.constructors.first { it.parameters.isEmpty() }.call()
                GFrame(applet, args).showMe()
                changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), applet)
            } catch (e: InstantiationException) {
                Log.error(e, e.localizedMessage)
            } catch (e: IllegalAccessException) {
                Log.error(e, e.localizedMessage)
            }
    }

    init {
        isFocusable = true
    }

}
