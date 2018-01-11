package gargoyle.sexbomb.campaign.levels.test1.test

import gargoyle.sexbomb.services.LevelInfo
import java.net.URL

object TestLevel1 : LevelInfo {
    override val name: String = "TestLevel 1"
    override val width: Int = 10
    override val height: Int = 10
    override val mines: Int = 10
    override val cover: URL? = javaClass.getResource("cover.jpg")
    override val image: URL? = javaClass.getResource("image.jpg")
}
