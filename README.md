BuildTypeLauncherIcons
======

Gradle plugin which generate launcher icons depending on the selected build type.   
Currently 'debug', 'alpha', 'beta' and 'gamma' are supported.   

Example
------------

![original](images/ic_launcher_original.png)
![debug](images/ic_launcher_debug.png)
![alpha](images/ic_launcher_alpha.png)
![beta](images/ic_launcher_beta.png)
![gamma](images/ic_launcher_gamma.png)


Installation
------------

Add the following to your `build.gradle`:

```gradle

buildscript {
    repositories {
        // ...
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.github.timfreiheit:BuildTypeLauncherIconsGradlePlugin:X.X.X'
    }
}

apply plugin: 'com.android.application'

// Make sure to apply this plugin *after* the Android plugin
apply plugin: 'de.timfreiheit.buildtypelaunchericon.plugin'

```

Usage
------------


```gradle

buildTypeLauncherIcon {
    mipmap = true // default
    ic_launcher = "ic_launcher.png" // default
    buildTypeNameMapping = ['gamma2': 'gamma'] // if needed
    runOnBuildTypes = ['debug', 'alpha', 'beta', 'gamma'] // default
}

```