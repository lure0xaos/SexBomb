module SexBomb {
    requires kotlin.stdlib;
    requires kotlin.stdlib.jdk7;
    requires kotlin.stdlib.jdk8;

    requires java.desktop;
    requires java.logging;

    requires SexBomb.util;

    exports gargoyle.sexbomb;
    opens gargoyle.sexbomb to kotlin.reflect;

    requires SexBomb.services;
    uses gargoyle.sexbomb.services.CampaignInfo;
    uses gargoyle.sexbomb.services.SkinInfo;
    requires SexBomb.campaign1;
    requires SexBomb.campaign2;
    requires SexBomb.skin;
    requires SexBomb.skin95;
    requires SexBomb.skin2000;
}
