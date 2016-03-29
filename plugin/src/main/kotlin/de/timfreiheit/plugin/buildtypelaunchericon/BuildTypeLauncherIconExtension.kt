package de.timfreiheit.plugin.buildtypelaunchericon

import java.util.*

open class BuildTypeLauncherIconExtension {
    /**
     * location so search the icon
     * if true {@link #ic_launcher} must be located in the mipmap folders
     * if false {@link #ic_launcher} must be located in the drawable folders
     */
    var mipmap: Boolean = true

    /**
     * the launcher icon name used in the manifest
     */
    var ic_launcher: String = "ic_launcher.png"

    /**
     * maps buildType names
     * this is useful when the buildType are not named ['debug', 'alpha', 'beta', 'gamma']
     */
    var buildTypeNameMapping : Map<String, String> = HashMap()

    /**
     * list of buildType names to run the plugin on
     * this list is checked after mapping the name using {@link #buildTypeNameMapping}
     */
    var runOnBuildTypes : List<String> = arrayListOf("debug", "alpha", "beta", "gamma")
}