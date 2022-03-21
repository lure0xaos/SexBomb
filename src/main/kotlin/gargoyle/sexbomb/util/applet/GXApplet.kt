package gargoyle.sexbomb.util.applet

import gargoyle.sexbomb.SexBomb
import gargoyle.sexbomb.util.res.Resources
import kotlin.reflect.KClass

abstract class GXApplet protected constructor(resources: Resources) : G0Applet(resources) {
    protected abstract fun doDestroy()
    protected abstract fun doInit()
    protected abstract fun doStart()
    protected abstract fun doStop()
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
        fun run(clazz: KClass<SexBomb>, args: Array<String>) {
            run0(clazz, args)
        }
    }
}
