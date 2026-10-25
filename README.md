# No Limit Enchants (NeoForge 1.21.1)

## Build

Requires Java 21. Run `gradlew.bat clean build` on Windows. The built mod will be in `build/libs/`.

## Enchanting Infuser dependency

The development runtime declares Enchanting Infuser and its required Puzzles Lib library in Gradle.

For a regular Minecraft launcher/modpack profile, dependencies are not bundled into the No Limit Enchants JAR. Install **Puzzles Lib for NeoForge 1.21.1, version 21.1.38 or newer**, into the *same profile's* `mods` folder as Enchanting Infuser. Restart Minecraft afterwards. Use the NeoForge build, not Fabric or Forge.

Puzzles Lib download page: https://modrinth.com/mod/puzzles-lib/versions?g=1.21.1&l=neoforge

## Commands

Use `/nolimitenchants max <level>` as an operator/cheat-enabled player to set the global enchantment cap. The vanilla enchantment storage format still limits stored levels to 255.

## Compatibility fixes

- Enchantment levels above X are displayed as Roman numerals (for example, Efficiency XXVIII).
- Anvil results preserve over-level enchantments from either input instead of downgrading them to their vanilla level. Equal-level merges can still add one level, up to the configured global cap. Existing levels are not lowered if the configured cap is later reduced.
- Provides Roman-numeral language entries for `enchantment.level.11` through `enchantment.level.255` in English and Russian. This fixes screens such as Enchanting Infuser that translate those keys directly rather than calling `Enchantment.getFullname()`.

