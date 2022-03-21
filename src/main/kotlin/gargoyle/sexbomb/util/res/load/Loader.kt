package gargoyle.sexbomb.util.res.load

import java.net.URL

fun interface Loader<T> {
    fun load(url: URL): T
}
