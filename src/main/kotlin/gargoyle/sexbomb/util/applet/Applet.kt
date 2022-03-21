package gargoyle.sexbomb.util.applet

interface Applet {
    fun setStub(stub: AppletStub)
    fun init()
    fun start()
    fun stop()
    fun destroy()
}
