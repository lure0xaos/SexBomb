package gargoyle.sexbomb.res

import java.net.URL

object Help {
    private const val RES_COPYRIGHT = "copyright.html"
    private const val RES_HELP = "help.html"
    private const val RES_TITLE = "title.gif"
    fun getCopyrightUrl(): URL = Help::class.java.getResource(RES_COPYRIGHT)!!
    fun getHelpUrl(): URL = Help::class.java.getResource(RES_HELP)!!
    fun getTitleUrl(): URL = Help::class.java.getResource(RES_TITLE)!!
}
