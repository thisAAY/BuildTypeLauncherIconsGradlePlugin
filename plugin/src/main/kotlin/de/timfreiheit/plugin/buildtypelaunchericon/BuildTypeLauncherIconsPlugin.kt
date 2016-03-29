package de.timfreiheit.plugin.buildtypelaunchericon

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import java.io.File
import java.util.*

class BuildTypeLauncherIconsPlugin : Plugin<Project> {

    val supportedBuildTypes: ArrayList<String> = arrayListOf("debug", "alpha", "beta", "gamma");

    lateinit var config: BuildTypeLauncherIconExtension;

    override fun apply(project: Project) {

        config = project.extensions.create("buildTypeLauncherIcon", BuildTypeLauncherIconExtension::class.java)

        project.afterEvaluate {
            project.plugins.all {
                when (it) {
                    is AppPlugin -> configureAndroid(project,
                            project.extensions.getByType(AppExtension::class.java).applicationVariants)
                    is LibraryPlugin -> configureAndroid(project,
                            project.extensions.getByType(LibraryExtension::class.java).libraryVariants)
                }
            }
        }

    }

    private fun <T : BaseVariant> configureAndroid(project: Project, variants: DomainObjectSet<T>) {
        variants.forEach { variant ->

            var buildTypeName = variant.buildType.name;
            if (config.buildTypeNameMapping.containsKey(buildTypeName)) {
                buildTypeName = config.buildTypeNameMapping.get(buildTypeName)
            }

            // check if buildType or mapping is supported
            if (!supportedBuildTypes.contains(buildTypeName)) {
                return@forEach;
            }

            if (!config.runOnBuildTypes.contains(buildTypeName)) {
                return@forEach;
            }

            val outputDirectory = project.file("$project.buildDir/generated/res/${variant.flavorName}/${variant.buildType.name}/launcher_icons/")

            // add new resource folder to sourceSet with the highest priority
            // this makes sure the new icons will override the original one
            variant.sourceSets[variant.sourceSets.size - 1].resDirectories.add(outputDirectory)

            var files : FileCollection? = null;
            variant.sourceSets.forEach { sourceSet ->
                val icons = ArrayList<File>();
                for (f in sourceSet.resDirectories) {
                    searchIcons(icons, f)
                }
                val iconCollection = project.files(icons);
                files = files?.plus(iconCollection) ?: iconCollection
            }

            if (files?.isEmpty ?: true) {
                val source = if (config.mipmap) "mipmap" else "drawable"
                println("WARNING: launcher file not found: $config.ic_launcher in $source folders");
                return@forEach;
            }

            val taskName = "prepareLauncherIconsFor${variant.name.capitalize()}";
            var task = project.tasks.create(taskName, BuildTypeLauncherIconTask::class.java).apply {
                sources = files!!
                outputDir = outputDirectory
                isMipmap = true
                launcherName = config.ic_launcher
                buildType = buildTypeName
            }

            // register task to make it run before resource merging
            // add dummy folder because the folder is already added to an sourceSet
            // when using the folder defined in the argument the generated resources are at the lowest priority
            // and will cause an conflict with the existing once
            variant.registerResGeneratingTask(task, File(outputDirectory, "_dummy"))
        }
    }

    fun searchIcons(temp: MutableList<File>, dir: File) {
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile) {
            val nameCorrect = dir.absolutePath.endsWith(config.ic_launcher);
            val typeCorrect = dir.absolutePath.contains(if (config.mipmap) "mipmap" else "drawable")
            if (nameCorrect && typeCorrect) {
                temp.add(dir);
            }
            return;
        }
        val files = dir.listFiles() ?: return;
        for (f in files) {
            searchIcons(temp, f);
        }
    }
}