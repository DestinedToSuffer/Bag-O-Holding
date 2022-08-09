package co.vangar.bagoholding;

import co.vangar.bagoholding.bag.bag;
import co.vangar.bagoholding.bag.utils;
import co.vangar.bagoholding.files.DataManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bagoholding extends JavaPlugin {
    private Bagoholding plugin = this;

    @Override
    public void onEnable() {
        // Plugin startup logic
        DataManager.setup();
        getServer().getPluginManager().registerEvents(new bag(), this);

        //Bundle
        ItemStack bundle = new ItemStack(Material.BUNDLE, 1);
        NamespacedKey bunKey = new NamespacedKey(plugin, "bundle");

        ShapedRecipe bundleRec = new ShapedRecipe(bunKey, bundle);
        bundleRec.shape("SRS","R*R","RRR");
        bundleRec.setIngredient('S', Material.STRING);
        bundleRec.setIngredient('R', Material.RABBIT_HIDE);

        getServer().addRecipe(bundleRec);

        //Backpack
        ItemStack backpack = utils.backpack();
        NamespacedKey bKey = new NamespacedKey(plugin, "backpack");

        ShapedRecipe backpackRec = new ShapedRecipe(bKey, backpack);
        backpackRec.shape("IBI","LBL","IBI");
        backpackRec.setIngredient('B', Material.BUNDLE);
        backpackRec.setIngredient('I', Material.IRON_INGOT);
        backpackRec.setIngredient('L', new RecipeChoice.ExactChoice(utils.logTypes()));

        getServer().addRecipe(backpackRec);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataManager.save();
    }
}
