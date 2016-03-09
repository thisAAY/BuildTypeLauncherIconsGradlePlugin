package de.timfreiheit.plugin.buildtypelaunchericon

/**
 * Configuration values for the victor {} script block.
 */
class BuildTypeLauncherIconExtension {

    String ic_launcher = "ic_launcher.png"

    boolean mipmap = true

    List<String> runOnBuildTypes = ['debug', 'alpha', 'beta']

    Map<String, String> buildTypeNameMapping = [:]
}
