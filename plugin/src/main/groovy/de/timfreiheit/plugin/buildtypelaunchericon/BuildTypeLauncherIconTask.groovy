package de.timfreiheit.plugin.buildtypelaunchericon

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import java.util.zip.ZipFile


class BuildTypeLauncherIconTask extends DefaultTask {

    private static Class clazz;

    @InputFiles
    FileCollection sources

    @Input
    String launcherName;

    @Input
    boolean isMipmap

    @Input
    String buildType

    /**
     * The output directory.
     */
    @OutputDirectory
    File outputDir

    @TaskAction
    def generateDrawables(IncrementalTaskInputs inputs) {
        clazz = getClass()

        //println("generateDrawableAction")
        //println(sources.files.toString())
        sources.forEach {

            def parent = it.toPath().parent.toFile()

            def outputParentDir = new File(outputDir, parent.name);

            def density = Density.getFromFolderName(outputParentDir.name)

            def output = new File(outputParentDir, launcherName)
            // println(output.toString())
            try {
                outputParentDir.mkdirs()
                output.createNewFile()
                //Files.copy(it, output)
                ImageProcessor.process(it, density, buildType, output)
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    /**
     * https://github.com/gfx/gradle-plugin-template/blob/master/plugin/src/main/groovy/com/github/gfx/gradle_plugin_template/FooTask.groovy
     * @param filename
     * @return
     */
    static String getResourceContent(String filename) {
        return getResourceStream(filename).getText("UTF-8")
    }

    /**
     * https://github.com/gfx/gradle-plugin-template/blob/master/plugin/src/main/groovy/com/github/gfx/gradle_plugin_template/FooTask.groovy
     * @param filename
     * @return
     */
    static InputStream getResourceStream(String filename) {
        URL templateFileUrl = BuildTypeLauncherIconTask.class.getClassLoader().getResource(filename)
        if (templateFileUrl == null) {
            throw new GradleException("[${this.tag}] File not found: $filename")
        }

        try {
            return templateFileUrl.openStream()
        } catch (IOException e) {
            // fallback to read JAR directly
            def connection = templateFileUrl.openConnection() as JarURLConnection
            def jarFile = connection.jarFileURL.toURI()
            ZipFile zip
            try {
                zip = new ZipFile(new File(jarFile))
            } catch (FileNotFoundException ex) {
                throw ex
            }
            return zip.getInputStream((zip.getEntry(filename)))
        }
    }

}