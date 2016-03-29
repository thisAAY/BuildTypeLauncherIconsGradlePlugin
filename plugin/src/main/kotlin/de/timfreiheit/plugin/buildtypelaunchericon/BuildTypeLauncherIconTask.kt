package de.timfreiheit.plugin.buildtypelaunchericon


import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File

/**
 * tasks must not be final
 */
open class BuildTypeLauncherIconTask : DefaultTask() {

    @get:InputFiles
    lateinit var sources: FileCollection;

    @get:Input
    var isMipmap: Boolean = true;

    @get:Input
    lateinit var launcherName: String;

    @get:Input
    lateinit var buildType: String;

    /**
     * The output directory.
     */
    @get:OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun execute(inputs: IncrementalTaskInputs) {

        for (it in sources) {

            var parent = it.parentFile

            var outputParentDir = File(outputDir, parent.name);

            var density = Density.getFromFolderName(outputParentDir.name)

            var output = File(outputParentDir, launcherName)
            try {
                outputParentDir.mkdirs()
                output.createNewFile()
                ImageProcessor.process(it, density, buildType, output)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
