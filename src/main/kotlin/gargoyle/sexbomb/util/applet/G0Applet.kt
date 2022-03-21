package gargoyle.sexbomb.util.applet

import gargoyle.sexbomb.SexBomb
import gargoyle.sexbomb.util.log.Log
import gargoyle.sexbomb.util.res.Resources
import java.awt.Component
import java.awt.Dialog
import java.awt.Frame
import java.net.URL
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JRootPane
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import kotlin.reflect.KClass

abstract class G0Applet protected constructor(protected val resources: Resources) : JApplet() {
    lateinit var application: GFrame

    init {
        isFocusable = true
    }

    private fun ask(message: String): Boolean {
        return JOptionPane.showConfirmDialog(
            this, message, title, JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION
    }

    fun canExit(): Boolean {
        return isApplication() && ask(STR_EXIT)
    }

    protected fun changeLookAndFeel(laf: String) {
        changeLookAndFeel(laf, this)
    }

    fun error(message: Throwable) {
        JOptionPane.showMessageDialog(
            this,
            String.format("%s: %s", message.javaClass.simpleName, message.localizedMessage),
            title,
            JOptionPane.ERROR_MESSAGE
        )
        Log.error(message.localizedMessage, message)
    }

    fun exit() {
        application.exit()
    }

    protected val rootFrame: Frame?
        get() {
            val parent = SwingUtilities.getRoot(this)
            return if (parent is Frame) {
                parent
            } else null
        }
    val title: String
        get() {
            val parent = SwingUtilities.getRoot(this)
            return if (parent is Frame) {
                parent.title
            } else javaClass.simpleName
        }

    protected fun isApplication(): Boolean {
        return true
    }

    public override fun showDocument(url: URL) {
        appletContext.showDocument(url)
    }

    override fun showDocument(url: URL, target: String) {
        appletContext.showDocument(url, target)
    }

    companion object {
        private const val STR_EXIT = "Exit?"
        fun run0(clazz: KClass<SexBomb>, args: Array<String>) {
            try {
                changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), null)
                val applet = clazz.constructors.first { it.parameters.isEmpty() }.call()
                GFrame(applet, applet.resources, args).showMe()
                changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), applet)
            } catch (e: InstantiationException) {
                Log.fatal(e.localizedMessage, e)
            } catch (e: IllegalAccessException) {
                Log.fatal(e.localizedMessage, e)
            }
        }

        private fun changeLookAndFeel(laf: String, component: Component?) {
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
                }
            } catch (e: ClassNotFoundException) {
                Log.warn(e.localizedMessage, e)
            } catch (e: UnsupportedLookAndFeelException) {
                Log.warn(e.localizedMessage, e)
            } catch (e: IllegalAccessException) {
                Log.warn(e.localizedMessage, e)
            } catch (e: InstantiationException) {
                Log.warn(e.localizedMessage, e)
            }
        }
    }
}
