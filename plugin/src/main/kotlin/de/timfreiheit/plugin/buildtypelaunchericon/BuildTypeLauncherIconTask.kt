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

    @InputFiles
    lateinit var sources: FileCollection;

    @Input
    var isMipmap: Boolean = true;

    @Input
    lateinit var launcherName: String;

    @Input
    lateinit var buildType: String;

    /**
     * The output directory.
     */
    @OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun generateDrawables(inputs: IncrementalTaskInputs) {
        for (it in sources) {
            var parent = it.toPath().parent.toFile()

            var outputParentDir = File(outputDir, parent.name);

            var density = Density.getFromFolderName(outputParentDir.name)

            var output = File(outputParentDir, launcherName)
            // println(output.toString())
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
