package de.timfreiheit.plugin.buildtypelaunchericon

/**
 * Configuration values for the buildTypeLauncherIcon {} script block.
 */
class BuildTypeLauncherIconExtension {

    /**
     * location so search the icon
     * if true {@link #ic_launcher} must be located in the mipmap folders
     * if false {@link #ic_launcher} must be located in the drawable folders
     */
    boolean mipmap = true

    /**
     * the launcher icon name used in the manifest
     */
    String ic_launcher = "ic_launcher.png"

    /**
     * maps buildType names
     * this is useful when the buildType are not named ['debug', 'alpha', 'beta', 'gamma']
     */
    Map<String, String> buildTypeNameMapping = [:]

    /**
     * list of buildType names to run the plugin on
     * this list is checked after mapping the name using {@link #buildTypeNameMapping}
     */
    List<String> runOnBuildTypes = ['debug', 'alpha', 'beta', 'gamma']
}
