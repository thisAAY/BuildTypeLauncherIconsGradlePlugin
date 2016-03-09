package de.timfreiheit.plugin.buildtypelaunchericon

enum Density  {

    /**
     * IMPORTANT: keep order. this is important when using {@link #getFromFolderName(java.lang.String)}
     */
    XXXHDPI("xxxhdpi", 4),
    XXHDPI("xxhdpi", 3),
    XHDPI("xhdpi", 2),
    HDPI("hdpi", 1.5f),
    MDPI("mdpi", 1),
    LDPI("ldpi", 0.75f);

    private String typeName;
    private float multiplier;

    Density(String typeName, float multiplier) {
        this.typeName = typeName;
        this.multiplier = multiplier
    }

    float getMultiplier() {
        return multiplier
    }

    String getTypeName() {
        return typeName
    }

    public static Density getFromFolderName(String folderName) {
        for (Density d : values()) {
            if (folderName.contains(d.typeName)) {
                return d;
            }
        }
        return MDPI;
    }
}