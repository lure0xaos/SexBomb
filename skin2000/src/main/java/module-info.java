@SuppressWarnings("JavaModuleNaming")
module SexBomb.skin2000 {
    requires kotlin.stdlib;

    exports gargoyle.sexbomb.skin2000;

    requires SexBomb.services;
    provides gargoyle.sexbomb.services.SkinInfo with gargoyle.sexbomb.skin2000.Skin2000;
}
