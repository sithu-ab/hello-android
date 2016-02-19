# Hello World Android Tutorial

Android beginner tutorial from [Google Android Developer Training](http://developer.android.com/training/basics/firstapp/index.html)

## Requirement

[Android Studio](http://developer.android.com/sdk/index.html) (1.13GB+ download)

## Possible Issues during the First App setup and build 

This issues are based on my experience of the very first android app setup and build.

#### Issue 1: Error: Failed to resolve: com.android.support:appcompat-v7:15.+

In `\app\build.gradle (Module:app)`, `+` for `com.android.support:appcompat` is not recommended. 

- Install and use the latest version **API 23: Android 6.0 (Marshmallow)** (at the time of this writing). You can install it from the menu **Tools** > **Android** > **SDK Manager**.
- Then, change `compile 'com.android.support:appcompat-v7:15.+'` to `compile 'com.android.support:appcompat-v7:23.1.1'`

Reference Stackoverflow Q/A: [Failed to resolve: com.android.support:appcompat-v7:15.+](http://stackoverflow.com/a/33106648/1179841)

#### Issue 2: Error:(23, 17) Failed to resolve: junit:junit:4.12 

It could be solved by adding url for missing repository in `app/build.gradle (Module:app)`. It should be added above `buildTypes`, otherwise another error [buildTypes cannot be applied to groovy.lang.Closure](http://stackoverflow.com/a/29354337/1179841) could occur.

    android {
        ...
        repositories {
            maven { url 'http://repo1.maven.org/maven2' }
        }
        ...
    }
    
Reference Stackoverflow Q/A: [Error:(23, 17) Failed to resolve: junit:junit:4.12](http://stackoverflow.com/a/32566057/1179841).
 **Note**: Later it seems deleted by Android Studio when the app is built.

#### Issue 3: Error retrieving parent for item: No resource found that matches the given name ...

`compileSdkVersion` and `buildToolsVersion` must match the support library's (`com.android.support:appcompat`) major version in `\app\build.gradle (Module:app)`.

![build.gradle-settings](/issues/build.gradle-settings.jpg)

Reference Stackoverflow Q/A: 

- [AppCompat v7 r21 returning error in values.xml?](http://stackoverflow.com/a/26457138/1179841)
- [Error retrieving parent for item: No resource found that matches the given name..](http://stackoverflow.com/a/32075678/1179841)

#### Issue 4: Error in launching Android Virtual Device

![emulator-error.jpg](/issues/emulator-error.jpg)

This could be solved by

- installing **HAXM installer** on your SDK Manager (**Tools** > **Android** > **SDK Manager**).
- running `{SDK_FOLDER}\extras\intel\Hardware_Accelerated_Execution_Manager\intelhaxm-android.exe`. You can get your SDK folder location from the menu **File** > **Project Structure** > **SDK Location**.
- enabling "Virtualization Technology" on your BIOS

![emulator-accelarator-install.jpg](/issues/emulator-accelarator-install.jpg)

![vt-not-supported.jpg](/issues/vt-not-supported.jpg)

Reference Stackoverflow Q/A: [Error in launching AVD](http://stackoverflow.com/a/26380900/1179841)
