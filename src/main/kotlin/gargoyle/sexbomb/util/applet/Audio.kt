package gargoyle.sexbomb.util.applet

import java.net.URL

object Audio {
    fun newAudioClip(location: URL): AudioClip {
        return AudioClipImpl(location)
    }
}
