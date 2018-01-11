@SuppressWarnings("JavaModuleNaming")
module SexBomb.campaign1 {
    requires kotlin.stdlib;

    exports gargoyle.sexbomb.campaign.levels.test1;

    requires SexBomb.services;
    provides gargoyle.sexbomb.services.CampaignInfo with gargoyle.sexbomb.campaign.levels.test1.TestCampaign;
}
