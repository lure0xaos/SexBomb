@SuppressWarnings("JavaModuleNaming")
module SexBomb.skin95 {
    requires kotlin.stdlib;

    exports gargoyle.sexbomb.skin95;

    requires SexBomb.services;
    provides gargoyle.sexbomb.services.SkinInfo with gargoyle.sexbomb.skin95.Skin95;
}
