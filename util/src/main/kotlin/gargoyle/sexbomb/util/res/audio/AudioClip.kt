package gargoyle.sexbomb.util.res.audio

import java.io.Closeable

interface AudioClip : Closeable {
    fun play()
    fun loop()
    fun stop()
}
