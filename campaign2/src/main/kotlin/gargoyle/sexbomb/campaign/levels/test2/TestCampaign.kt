package gargoyle.sexbomb.campaign.levels.test2

import gargoyle.sexbomb.campaign.levels.test2.test1.TestLevel1
import gargoyle.sexbomb.campaign.levels.test2.test2.TestLevel2
import gargoyle.sexbomb.services.CampaignInfo
import gargoyle.sexbomb.services.LevelInfo

class TestCampaign : CampaignInfo {
    override val name: String
        get() = "TestCampaign"
    override val levels: List<LevelInfo>
        get() = listOf(TestLevel1, TestLevel2)
}
