package de.timfreiheit.plugin.buildtypelaunchericon

enum class Density(val typeName: String, val multiplier: Float) {

    /**
     * IMPORTANT: keep order. this is important when using {@link #getFromFolderName(java.lang.String)}
     */
    XXXHDPI("xxxhdpi", 4f),
    XXHDPI("xxhdpi", 3f),
    XHDPI("xhdpi", 2f),
    HDPI("hdpi", 1.5f),
    MDPI("mdpi", 1f),
    LDPI("ldpi", 0.75f);

    companion object {
        fun getFromFolderName(folderName: String): Density {
            return Density.values().find { folderName.contains(it.typeName) } ?: Density.MDPI;
        }
    }
}