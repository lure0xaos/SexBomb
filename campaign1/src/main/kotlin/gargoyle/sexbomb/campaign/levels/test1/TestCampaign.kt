package gargoyle.sexbomb.campaign.levels.test1

import gargoyle.sexbomb.campaign.levels.test1.test.TestLevel1
import gargoyle.sexbomb.services.CampaignInfo
import gargoyle.sexbomb.services.LevelInfo

class TestCampaign : CampaignInfo {
    override val name: String
        get() = "TestCampaign"
    override val levels: List<LevelInfo>
        get() = listOf(TestLevel1)
}
