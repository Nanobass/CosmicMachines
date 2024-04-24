# Flux Example Mod
A simple example mod showing the main feature of flux. This is restricted to fabric loader methods but runs on Quilt near perfectly.

### Running Your Mod
In order to run this example mod you need to look at the gradle tasks. In the tasks you should see a folder named `runs`, click on it. After that you can see the `runFabric` and `runQuilt` buttons. Click on any of those tasks and you will run your mod.

### Exporting/Sharing Your Mod
To share your mod look at the folder named `shadow` in gradle tasks. Open that folder and click `shadowJar` and that will build and export it.
To find your exported build look in the `build/libs` folder in your file tree/explorer.

If you want to customize your mods or update dependencies you can mess with that in the file `gradle.properties`.
Also to change your mod name look in the `settings.gradle` file.