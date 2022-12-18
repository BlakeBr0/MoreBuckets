# More Buckets [![](http://cf.way2muchnoise.eu/full_298760_downloads.svg)](https://minecraft.curseforge.com/projects/more-buckets) 

Adds some more buckets to Minecraft.

## Download

The official release builds can be downloaded from the following websites.

- [Blake's Mods](https://blakesmods.com/more-buckets/download)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/more-buckets)
- [Modrinth](https://modrinth.com/mod/more-buckets)

## Development

To use this mod in a development environment, you will need to add the following to your `build.gradle`.

```groovy
repositories {
    maven {
        url 'https://maven.blakesmods.com'
    }
}

dependencies {
    implementation fg.deobf('com.blakebr0.cucumber:Cucumber:<minecraft_version>-<mod_version>')
    implementation fg.deobf('com.blakebr0.morebuckets:MoreBuckets:<minecraft_version>-<mod_version>')
}
```

## License

[MIT License](./LICENSE)
