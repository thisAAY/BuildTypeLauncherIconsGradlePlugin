package de.timfreiheit.plugin.buildtypelaunchericon

import org.gradle.api.GradleException
import java.io.*
import java.net.JarURLConnection
import java.util.zip.ZipFile

/**
 * https://github.com/gfx/gradle-plugin-template/blob/master/plugin/src/main/groovy/com/github/gfx/gradle_plugin_template/FooTask.groovy
 * @param filename
 * @return
 */
fun getResourceContent(filename: String): String {
    return getResourceStream(filename).getText("UTF-8")
}

/**
 * https://github.com/gfx/gradle-plugin-template/blob/master/plugin/src/main/groovy/com/github/gfx/gradle_plugin_template/FooTask.groovy
 * @param filename
 * @return
 */
fun getResourceStream(filename: String): InputStream {
    var templateFileUrl = BuildTypeLauncherIconTask::class.java.classLoader.getResource(filename)
            ?: throw GradleException("[BuildTypeLauncherIconTask] File not found: $filename")

    try {
        return templateFileUrl.openStream()
    } catch (e: IOException) {
        // fallback to read JAR directly
        var connection = templateFileUrl.openConnection() as JarURLConnection
        var jarFile = connection.jarFileURL.toURI()
        var zip: ZipFile
        try {
            zip = ZipFile(File(jarFile))
        } catch (ex: FileNotFoundException) {
            throw ex
        }
        return zip.getInputStream((zip.getEntry(filename)))
    }
}



private fun InputStream.getText(charset: String): String {
    val reader = BufferedReader(InputStreamReader(this, charset));
    return reader.readText();
}