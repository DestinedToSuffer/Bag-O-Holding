package co.vangar.bagoholding.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class DataManager {

    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("Bag_O_Holding");

    private static File file;
    private static FileConfiguration customFile;

    public static void setup(){
        file = new File(plugin.getDataFolder(), "bag.yml");
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException e){
                System.out.println("Couldn't complete setup");
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        }catch(IOException e){
            System.out.println("Couldn't save file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
