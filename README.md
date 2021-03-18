BUNGEECORD BRANCH

DataHandler.initializeScheduledUpdate(ticks, [Optional: NameID]) will allow you to automatically update the yaml file every certain amount of ticks

DataHandler.update([Optional: NameID]) update the yaml file right now

I got fed up with having to deal with the tedious process of loading in configurations and files in my spigot plugin. Wrote a really quick class that should make it easier to manage files/configurations in your bukkit plugin.

Basic workflow to load in a yaml for usage: instanciate (optional) dataHandler.createDirectoryIfMissing (optional) dataHandler.copyTemplateIfMIssing dataHandler.addFile dataHandler.loadFileYaml

Please note that there is a branch that works with the BungeeCord API instead of the bukkit API

USAGE EXAMPLE:

DataHandler dataHandler = new DataHandler(this); Instanciate, "this" is JavaPlugin main class

dataHandler.createDirectoryIfMissing("plugins/MyPlugin"); Create directory if it doesn't exist

dataHandler.copyTemplateIfMissing("config.yml", "plugins/MyPlugin/config.yml"); Copy template from source of the .jar to a destination if it doesn't exist already

dataHandler.addFile("myconfig", "plugins/MyPlugin/config.yml"); Add a file to the system, first arg is the ID to be linked to the file, second is the path to the file

dataHandler.loadFileYAML("myconfig"); Cache the YAML data, use this also if you wanna reload the yaml data

dataHandler.setYAMLBooleanField("myconfig", "hello.test", true); in configuration path hello.test, set test field to true. This will automatically re-cache the yaml data

if(dataHandler.YAMLPathExists("myconfig", "Path.Of.goodness")){System.out.print("YAY");} Check if a path exists

dataHandler.deleteYAMLPath("myconfig", "hello.test"); Delete yaml field, this will also automatically update the yaml cache for that file

System.out.println(dataHandler.getYAMLStringField("myconfig", "fruit.banana")); Print out fruit.banana string field contents

Set sections = dataHandler.getConfigurationSections(dataNameID, "announcements"); Get a set of all the configuration sections


