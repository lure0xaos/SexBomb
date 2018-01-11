package gargoyle.sexbomb.util.applet

import kotlin.reflect.KClass

abstract class GXApplet protected constructor() : GApplet() {
    @Synchronized
    override fun init() {
        isFocusable = true
        try {
            doInit()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    @Synchronized
    override fun start() {
        try {
            doStart()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    @Synchronized
    override fun stop() {
        try {
            doStop()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    @Synchronized
    override fun destroy() {
        try {
            doDestroy()
        } catch (e: RuntimeException) {
            error(e)
        }
    }

    companion object {
        fun run(clazz: KClass<out GXApplet>, args: Array<String>) {
            GApplet.run(clazz, args)
        }
    }
}
