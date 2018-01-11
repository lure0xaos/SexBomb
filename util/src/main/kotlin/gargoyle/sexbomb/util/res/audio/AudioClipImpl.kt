package gargoyle.sexbomb.util.res.audio

import java.io.BufferedInputStream
import java.net.URL
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

internal class AudioClipImpl(private val location: URL) : AudioClip {
    private val clip: Clip = AudioSystem.getClip().apply {
        location.openConnection().getInputStream().let { stream ->
            BufferedInputStream(stream).let { bufferedInputStream ->
                AudioSystem.getAudioInputStream(bufferedInputStream).use { stream1 -> open(stream1) }
            }
        }
    }

    override fun play() {
        if (clip.isActive) clip.stop()
        if (clip.isOpen) {
            clip.framePosition = 0
            clip.start()
        }
    }

    override fun loop() {
        if (clip.isActive) clip.stop()
        if (clip.isOpen) clip.loop(Clip.LOOP_CONTINUOUSLY)
    }

    override fun stop() {
        if (clip.isActive) clip.stop()
    }

    override fun close() {
        if (clip.isActive) clip.stop()
        if (clip.isOpen) clip.close()
    }
}
