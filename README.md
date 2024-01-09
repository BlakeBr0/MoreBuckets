# More Buckets

<p align="left">
    <a href="https://blakesmods.com/more-buckets" alt="Downloads">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/morebuckets/downloads&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/more-buckets" alt="Latest Version">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/morebuckets/version&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/more-buckets" alt="Minecraft Version">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/morebuckets/mc_version&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/wiki/morebuckets" alt="Wiki">
        <img src="https://img.shields.io/static/v1?label=wiki&message=view&color=brightgreen&style=for-the-badge" />
    </a>
</p>

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
