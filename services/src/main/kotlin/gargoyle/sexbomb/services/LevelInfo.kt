package gargoyle.sexbomb.services

import java.net.URL

interface LevelInfo {
    val name: String
    val width: Int
    val height: Int
    val mines: Int
    val cover: URL?
    val image: URL?
}
