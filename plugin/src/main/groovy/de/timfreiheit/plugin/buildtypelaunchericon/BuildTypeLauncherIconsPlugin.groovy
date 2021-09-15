package de.timfreiheit.plugin.buildtypelaunchericon

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection

class BuildTypeLauncherIconsPlugin implements Plugin<Project> {

    final List<String> supportedBuildTypes = ['debug', 'alpha', 'beta', 'gamma']

    void apply(Project project) {
        project.extensions.create('buildTypeLauncherIcon', BuildTypeLauncherIconExtension)

        project.afterEvaluate {
            BuildTypeLauncherIconExtension config = project.buildTypeLauncherIcon

            def variants = null
            if (project.android.hasProperty('applicationVariants')) {
                variants = project.android.applicationVariants
            } else if (project.android.hasProperty('libraryVariants')) {
                variants = project.android.libraryVariants
            } else {
                throw new IllegalStateException('Android project must have applicationVariants or libraryVariants!')
            }

            // Register our task with the variant's resources
            variants.all { variant ->

                def buildTypeName = variant.buildType.name;
                if (config.buildTypeNameMapping?.containsKey(buildTypeName)) {
                    buildTypeName = config.buildTypeNameMapping.get(buildTypeName)
                }

                // check if buildType or mapping is supported
                if (!supportedBuildTypes.contains(buildTypeName)) {
                    return;
                }

                if (!config.runOnBuildTypes.contains(buildTypeName)) {
                    return;
                }

                def outputDirectory = project.file("$project.buildDir/generated/res/$flavorName/$variant.buildType.name/launcher_icons/")

                // add new resource folder to sourceSet with the highest priority
                // this makes sure the new icons will override the original one
                variant.sourceSets.get(variant.sourceSets.size() - 1).res.srcDirs += outputDirectory

                FileCollection files = project.files()
                variant.sourceSets.each { sourceSet ->
                    List<File> icons = new ArrayList<>();
                    for (File f : sourceSet.res.srcDirs) {
                        //noinspection GroovyAssignabilityCheck
                        searchIcons(config, icons, f)
                    }
                    files = files + project.files(icons)
                }

                if (files.empty) {
                    String source = config.mipmap ? "mipmap" : "drawable"
                    println("WARNING: launcher file not found: $config.ic_launcher in $source folders");
                    return;
                }

                Task task = project.task("prepareLauncherIconsFor${variant.name.capitalize()}", type: BuildTypeLauncherIconTask) {
                    sources = files
                    outputDir = outputDirectory
                    isMipmap = true
                    launcherName = config.ic_launcher
                    buildType = buildTypeName
                }

                // register task to make it run before resource merging
                // add dummy folder because the folder is already added to an sourceSet
                // when using the folder defined in the argument the generated resources are at the lowest priority
                // and will cause an conflict with the existing once
                variant.registerResGeneratingTask(task, new File(outputDirectory, "_dummy"))
            }
        }

    }

    void searchIcons(BuildTypeLauncherIconExtension config, List<File> temp, File dir) {
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            boolean nameCorrect = dir.absolutePath.endsWith(config.ic_launcher);
            boolean typeCorrect = dir.absolutePath.contains(config.mipmap ? "mipmap" : "drawable")
            if (nameCorrect && typeCorrect) {
                temp.add(dir);
            }
            return;
        }
        List<File> files = dir.listFiles();
        if (files == null) {
            return;
        }
        for(File f : files) {
            searchIcons(config, temp, f);
        }
    }
}