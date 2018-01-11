@SuppressWarnings("JavaModuleNaming")
module SexBomb.campaign2 {
    requires kotlin.stdlib;

    exports gargoyle.sexbomb.campaign.levels.test2;

    requires SexBomb.services;
    provides gargoyle.sexbomb.services.CampaignInfo with gargoyle.sexbomb.campaign.levels.test2.TestCampaign;
}
