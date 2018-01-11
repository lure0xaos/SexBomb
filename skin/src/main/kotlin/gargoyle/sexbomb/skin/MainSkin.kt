package gargoyle.sexbomb.skin

import gargoyle.sexbomb.services.SkinInfo
import java.net.URL

class MainSkin : SkinInfo {
    override val name: String = "Skin"
    override val closed: URL = javaClass.getResource("closed.gif")!!
    override val flag: URL = javaClass.getResource("flag.gif")!!
    override val mine: URL = javaClass.getResource("mine.gif")!!
    override val mined: URL = javaClass.getResource("mined.gif")!!
    override val noMine: URL = javaClass.getResource("nomine.gif")!!
    override val open0: URL = javaClass.getResource("open0.gif")!!
    override val open1: URL = javaClass.getResource("open1.gif")!!
    override val open2: URL = javaClass.getResource("open2.gif")!!
    override val open3: URL = javaClass.getResource("open3.gif")!!
    override val open4: URL = javaClass.getResource("open4.gif")!!
    override val open5: URL = javaClass.getResource("open5.gif")!!
    override val open6: URL = javaClass.getResource("open6.gif")!!
    override val open7: URL = javaClass.getResource("open7.gif")!!
    override val open8: URL = javaClass.getResource("open8.gif")!!
    override val question: URL = javaClass.getResource("question.gif")!!
}
