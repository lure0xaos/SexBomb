module SexBomb.skin {
    requires kotlin.stdlib;

    requires SexBomb.services;

    exports gargoyle.sexbomb.skin;
    opens gargoyle.sexbomb.skin;

    provides gargoyle.sexbomb.services.SkinInfo with gargoyle.sexbomb.skin.MainSkin;
}
