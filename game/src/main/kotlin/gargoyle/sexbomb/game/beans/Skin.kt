package gargoyle.sexbomb.game.beans

import gargoyle.sexbomb.services.SkinInfo
import gargoyle.sexbomb.util.applet.GApplet
import java.io.Serializable

class Skin(applet: GApplet, info: SkinInfo) : Serializable {
    val name = info.name
    val closed = applet.getImage(info.closed)
    val flag = applet.getImage(info.flag)
    val mine = applet.getImage(info.mine)
    val mined = applet.getImage(info.mined)
    val noMine = applet.getImage(info.noMine)
    val open0 = applet.getImage(info.open0)
    val open1 = applet.getImage(info.open1)
    val open2 = applet.getImage(info.open2)
    val open3 = applet.getImage(info.open3)
    val open4 = applet.getImage(info.open4)
    val open5 = applet.getImage(info.open5)
    val open6 = applet.getImage(info.open6)
    val open7 = applet.getImage(info.open7)
    val open8 = applet.getImage(info.open8)
    val question = applet.getImage(info.question)
}
