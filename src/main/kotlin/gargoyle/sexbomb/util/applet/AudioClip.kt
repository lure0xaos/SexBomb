package gargoyle.sexbomb.util.applet

import java.io.Closeable

interface AudioClip : Closeable {
    fun play()
    fun loop()
    fun stop()
    override fun close() {}
}
