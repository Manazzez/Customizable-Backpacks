# Backpack Plugin

A Minecraft Spigot plugin that adds customizable backpacks to the game, allowing players to expand their inventory.

## Installation

1. Download the latest release of the Backpack plugin from the [releases page](https://github.com/Manazzez/Customizable-Backpacks/releases).
2. Place the downloaded `.jar` file in your server's `plugins` directory.
3. Restart the server to load the plugin.
4. Edit the configuration files in `plugins/Backpack` to customize the settings.

## Usage

### Commands

- `/bp` - Access the backpacks menu to open or edit your backpacks.
- `/bp view <target>` - Allows people with the `backpacks.admninViewMenu` permission to see other's backpack's
- `/bp reload` - Allows people with the `backpacks.admninViewMenu` permission to reload the config files 
- `/bp access <target> <backpack>` - Allows you to access someone's backpack if you're allowed.
- `/bp purchase` - Purchase new backpacks, the limit is 14.
- `/bp open <backpack>` - Open your backpacks with their number or name.
- `/bp rename <backpack> ~ <new name>` - Rename your backpacks.
- `/bp lore <backpack> ~ <lore ... lore>` - Add subtext to the backpack icons in the menu.
- `/bp expand <backpack>` - Expand non-expanded backpacks to 54 slots.
- `/bp reset <backpack>` - Reset the customization on any of your backpacks.

### Permissions

- `backpacks.adminViewMenu` - Allows usage of all backpack commands, including admin features.

## Configuration

The plugin uses YAML configuration files located in the `plugins/Backpack` directory. Edit `Contents.yml` and `Settings.yml` to customize the plugin settings.

