package gargoyle.sexbomb.skin2000

import gargoyle.sexbomb.services.SkinInfo
import java.net.URL

class Skin2000 : SkinInfo {
    override val name: String
        get() = "Skin"
    override val closed: URL
        get() = javaClass.getResource("closed.gif")!!
    override val flag: URL
        get() = javaClass.getResource("flag.gif")!!
    override val mine: URL
        get() = javaClass.getResource("mine.gif")!!
    override val mined: URL
        get() = javaClass.getResource("mined.gif")!!
    override val noMine: URL
        get() = javaClass.getResource("nomine.gif")!!
    override val open0: URL
        get() = javaClass.getResource("open0.gif")!!
    override val open1: URL
        get() = javaClass.getResource("open1.gif")!!
    override val open2: URL
        get() = javaClass.getResource("open2.gif")!!
    override val open3: URL
        get() = javaClass.getResource("open3.gif")!!
    override val open4: URL
        get() = javaClass.getResource("open4.gif")!!
    override val open5: URL
        get() = javaClass.getResource("open5.gif")!!
    override val open6: URL
        get() = javaClass.getResource("open6.gif")!!
    override val open7: URL
        get() = javaClass.getResource("open7.gif")!!
    override val open8: URL
        get() = javaClass.getResource("open8.gif")!!
    override val question: URL
        get() = javaClass.getResource("question.gif")!!
}
