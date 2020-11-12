import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

//Unfortunately java does not have type specialization so Generic templates cannot be used like in c++


public class DataHandler {
	
	public JavaPlugin plugin;
	
	//HASHMAPS:
	private HashMap<String, File> files = new HashMap<String, File>(); //NameID, Corresponding file
	private HashMap<String, YamlConfiguration> YAMLData = new HashMap<String, YamlConfiguration>(); //NameID, corresponding YAML data
	
	public DataHandler(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void addFile(String nameID, String filePath) {
		files.put(nameID, new File(filePath)); //Add new file to file path hashmap
	}
	
	public void update(String nameID) {
		this.saveYAML(nameID); //save file
		this.loadFileYAML(nameID); //reload cache
	}
	
	//Update all the files
	public void update() {
		for(String nameID : files.keySet()) {
			this.saveYAML(nameID); //save file
			this.loadFileYAML(nameID); //reload cache
		}
	}
	
	public void initializeScheduledUpdate(final int waitTicks, final String nameID) {
		
		new BukkitRunnable() {
			@Override
		    public void run() { 
				DataHandler.this.saveYAML(nameID); //save file
				DataHandler.this.loadFileYAML(nameID); //reload cache
					
				//repeat:
				DataHandler.this.initializeScheduledUpdate(waitTicks, nameID);
			}
		}.runTaskLater(plugin, waitTicks);   
	}
	
	//schedule update all the files
	public void initializeScheduledUpdate(final int waitTicks) {
		
		new BukkitRunnable() {
			@Override
		    public void run() { 
				for(String nameID : files.keySet()) {
					DataHandler.this.saveYAML(nameID); //save file
					DataHandler.this.loadFileYAML(nameID); //reload cache
				}
				
				//repeat:
				DataHandler.this.initializeScheduledUpdate(waitTicks);
			}
		}.runTaskLater(plugin, waitTicks);   
	}
	
	//Load a specific added file's yaml data
	public boolean loadFileYAML(String nameID) {
		if(files.containsKey(nameID)) {
			if(YAMLData.containsKey(nameID)) { //Delete from YAMLData if it already contains it
				YAMLData.remove(nameID);
			}
			
			//Now add it to YAMLData
			YAMLData.put(nameID, YamlConfiguration.loadConfiguration(files.get(nameID)));
			return(true);
		}
		return(false);
	}
	
	//for saving yaml data(private):
	private void saveYAML(String nameID) {
		try {
			YAMLData.get(nameID).save(files.get(nameID));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Does a specified path exist?
	public Boolean YAMLPathExists(String nameID, String YAMLpath) {
		return(YAMLData.get(nameID).contains(YAMLpath));
	}
	
	
	//SET(yaml):
	public void setYAMLStringField(String nameID, String YAMLpath, String value) {
		YAMLData.get(nameID).set(YAMLpath, value);
	}
	
	public void setYAMLBooleanField(String nameID, String YAMLpath, Boolean value) {
		YAMLData.get(nameID).set(YAMLpath, value);
	}
	
	public void setYAMLIntegerField(String nameID, String YAMLpath, Integer value) {
		YAMLData.get(nameID).set(YAMLpath, value);
	}
	
	public void setYAMLIntegerField(String nameID, String YAMLpath, Double value) {
		YAMLData.get(nameID).set(YAMLpath, value);
	}
	
	public void setYAMLListField(String nameID, String YAMLpath, List<?> value) { 	
		YAMLData.get(nameID).set(YAMLpath, value);
		this.saveYAML(nameID); //save file
		this.loadFileYAML(nameID); //reload cache
	}
	
	//GET VALUE(yaml):
	public String getYAMLStringField(String nameID, String YAMLpath) {
		return (YAMLData.get(nameID).getString(YAMLpath));
	}
	
	public boolean getYAMLBooleanField(String nameID, String YAMLpath) {
		return (YAMLData.get(nameID).getBoolean(YAMLpath));
	}
	
	public Integer getYAMLIntegerField(String nameID, String YAMLpath) {
		return (Integer) (YAMLData.get(nameID).getInt(YAMLpath));
	}
	
	public Double getYAMLDoubleField(String nameID, String YAMLpath) {
		return (double) (YAMLData.get(nameID).getDouble(YAMLpath));
	}
	
	public List<?> getYAMLListField(String nameID, String YAMLpath) {
		return(YAMLData.get(nameID).getList(YAMLpath)); //? wildcard, we don't know the type of list, use cast
	}
	
	//GET more(yaml):
	public Set<String> getConfigurationSections(String nameID, String YAMLpath) {
		Set<String> res = new HashSet<String>(); //empty

		try {
			res = YAMLData.get(nameID).getConfigurationSection(YAMLpath).getKeys(false);
		}
		catch(NullPointerException e) {
			//None was found
		}
		
		return(res);
	}
	 
	public Set<String> getConfigurationSections(String nameID, String YAMLpath, Boolean deep) { //overloaded method for deep search
		Set<String> res = new HashSet<String>(); //empty

		try {
			res = YAMLData.get(nameID).getConfigurationSection(YAMLpath).getKeys(deep);
		}
		catch(NullPointerException e) {
			//None was found
		}
		
		return(res);
	}
	
	//DELETE(yaml):
	public void deleteYAMLPath(String nameID, String YAMLpath) {
		YAMLData.get(nameID).set(YAMLpath, null);
	}
	
	//UTILITY, does not require you to add the file first
	//Creates a directory if it doesn't exist
	public void createDirectoryIfMissing(String dirPath) {
		File directory = new File(dirPath);
		if(!directory.exists()) {
			directory.mkdir();
		}
	}
	
	//UTILITY, does not require you to add the file first
	//Copys a default template file from the src of the jar to the specified resource folder(probably plugins/something) IF it doesnt exist
	public void copyTemplateIfMissing(String srcFilePath, String resourceFilePath) {
		File target = new File(resourceFilePath);
		
		if(!target.exists()) {
			try {
				Files.copy(plugin.getResource(srcFilePath), target.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
