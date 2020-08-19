This is a similar project to my BukkitDataHandler(https://github.com/IgnisOwl/BukkitDataHandler), but with the intention of dramatically speeding up performance. 
It will refrain from reading or writing to the file until **DataHandler.update(*[Optional: NameID]*)** is called. If a String is supplied it will only update the specified file that corresponds to the given nameID, otherwise all the files will be updated.

**DataHandler.initializeScheduledUpdate(*ticks*, *[Optional: NameID]*)** will allow you to automatically update the file every certain amount of ticks


