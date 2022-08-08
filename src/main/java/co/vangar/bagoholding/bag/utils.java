package co.vangar.bagoholding.bag;

import co.vangar.bagoholding.files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class utils {

    public static boolean hasBag(Player p){
        if(DataManager.get().get(p.getUniqueId().toString()) != null){
            if(DataManager.get().getBoolean(p.getUniqueId().toString() + ".hasBag")){
                return true;
            } else return false;
        } else return false;
    }

    public static void openBag(Player p){
        if(hasBag(p)){
            reopen(p);
        } else {
            firstTime(p);
        }
    }

    public static void upgradeMyBag(Player p, Inventory bInv){
        int size = DataManager.get().getInt(p.getUniqueId().toString() + ".size");

        if(size == 18){
            size = 27;
        } else if(size == 27){
            size = 36;
        } else if(size == 36){
            size = 45;
        }

        Inventory newInv = Bukkit.createInventory(p, size, p.getDisplayName() + "'s Bag o' Holding");

        for(int i = 0; i < bInv.getSize(); i++){
            if(bInv.getItem(i) != null){
                if(!bInv.getItem(i).getItemMeta().isUnbreakable()){
                    newInv.setItem(i, bInv.getItem(i));
                } else {

                }
            }
        }

        removeUpgradeItems(p, size);
        DataManager.get().set(p.getUniqueId().toString() + ".size", newInv.getSize());
        newInv = upgradeRow(p, newInv);
        saveInv(p, newInv);
        p.openInventory(newInv);
    }

    public static void removeUpgradeItems(Player p, int size){
        Material item1 = null;
        Material item2 = null;
        Material item3 = null;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        if(size == 27){
            item1 = Material.GOLD_INGOT;
            item2 = Material.GOLD_BLOCK;
            item3 = Material.DIAMOND;
        } else if(size == 36){
            item1 = Material.DIAMOND;
            item2 = Material.DIAMOND_BLOCK;
            item3 = Material.NETHERITE_INGOT;
        } else if(size == 45){
            item1 = Material.NETHERITE_INGOT;
            item2 = Material.NETHERITE_BLOCK;
            item3 = Material.NETHER_STAR;
        }

        for(int i = 0; i < p.getInventory().getSize(); i++){
            if(p.getInventory().getItem(i) != null){
                if(p.getInventory().getItem(i).getType() == item1 && i1 < 4){
                    if(p.getInventory().getItem(i).getAmount() > 4){
                        p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 4);
                        i1 = 4;
                    } else {
                        int toRemove = 4 - i1;
                        i1 = i1 + p.getInventory().getItem(i).getAmount();
                        p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - toRemove);
                    }
                } else if(p.getInventory().getItem(i).getType() == item2 && i2 < 4){
                    if(p.getInventory().getItem(i).getAmount() > 4){
                        p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 4);
                        i2 = 4;
                    } else {
                        int toRemove = 4 - i2;
                        i2 = i2 + p.getInventory().getItem(i).getAmount();
                        p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - toRemove);
                    }
                } else if(p.getInventory().getItem(i).getType() == item3 && i3 < 1){
                    p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 1);
                    i3 = 1;
                }
            }
        }
    }

    public static Inventory upgradeRow(Player p, Inventory inv){
        ItemStack barrier = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta bMeta = barrier.getItemMeta();
        bMeta.setDisplayName(ChatColor.BLACK + "");
        bMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bMeta.setUnbreakable(true);
        bMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        barrier.setItemMeta(bMeta);

        ItemStack eye = eyeUpgrade(p);

        int size = inv.getSize();

        for(int i = size-1; i > size-10; i--){
            if(i == size-5){
                inv.setItem(i, eye);
            } else {
                inv.setItem(i, barrier);
            }
        }

        return inv;
    }

    public static ItemStack eyeUpgrade(Player p){
        Integer size = DataManager.get().getInt(p.getUniqueId().toString() + ".size");

        ItemStack upgrade = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta uMeta = upgrade.getItemMeta();

        uMeta.setUnbreakable(true);
        uMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        List<String> lore = new ArrayList<>();

        if(canUpgrade(p)){
            uMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Upgrade!");
            uMeta.addEnchant(Enchantment.LURE, 1, true);
            uMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            uMeta.setDisplayName(ChatColor.RED + "Upgrade Cost");
        }

        if(size == 18){
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------");
            lore.add(ChatColor.YELLOW + " Upgrade to Tier 2 ");
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------");
            lore.add(ChatColor.YELLOW + "4x Gold Ingots");
            lore.add(ChatColor.YELLOW + "4x Gold Blocks");
            lore.add(ChatColor.YELLOW + "1x Diamond");
        } else if(size == 27){
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------");
            lore.add(ChatColor.YELLOW + " Upgrade to Tier 3 ");
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------");
            lore.add(ChatColor.YELLOW + "4x Diamonds");
            lore.add(ChatColor.YELLOW + "4x Diamond Blocks");
            lore.add(ChatColor.YELLOW + "1x Netherite Ingot");
        } else if(size == 36){
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------");
            lore.add(ChatColor.YELLOW + " Upgrade to Tier 4 ");
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------");
            lore.add(ChatColor.YELLOW + "4x Netherite Ingots");
            lore.add(ChatColor.YELLOW + "4x Netherite Blocks");
            lore.add(ChatColor.YELLOW + "1x Nether Star");
        } else {
            uMeta.setDisplayName(ChatColor.GOLD + "Max Upgrade");
            uMeta.addEnchant(Enchantment.LURE, 1, true);
            uMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        uMeta.setLore(lore);
        upgrade.setItemMeta(uMeta);
        return upgrade;
    }

    public static boolean canUpgrade(Player p){
        Integer size = DataManager.get().getInt(p.getUniqueId().toString() + ".size");
        Material item1 = null;
        Material item2 = null;
        Material item3 = null;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        if(size == 18){
            item1 = Material.GOLD_INGOT;
            item2 = Material.GOLD_BLOCK;
            item3 = Material.DIAMOND;
        } else if(size == 27){
            item1 = Material.DIAMOND;
            item2 = Material.DIAMOND_BLOCK;
            item3 = Material.NETHERITE_INGOT;
        } else if(size == 36){
            item1 = Material.NETHERITE_INGOT;
            item2 = Material.NETHERITE_BLOCK;
            item3 = Material.NETHER_STAR;
        } else return false;

        ItemStack[] contents = p.getInventory().getContents();

        for(ItemStack is : contents){
            if(is != null){
                if(is.getType() == item1) i1 = i1 + is.getAmount();
                if(is.getType() == item2) i2 = i2 + is.getAmount();
                if(is.getType() == item3) i3 = i3 + is.getAmount();
            }
        }

        if(i1 >= 4 && i2 >= 4 && i3 >=1){
            return true;
        } else return false;
    }

    public static void firstTime(Player p){
        Inventory inv = Bukkit.createInventory(p, 18, p.getDisplayName() + "'s Bag o' Holding");
        DataManager.get().set(p.getUniqueId().toString() + ".hasBag", true);
        DataManager.get().set(p.getUniqueId().toString() + ".size", inv.getSize());
        inv = upgradeRow(p, inv);
        saveInv(p, inv);
        p.openInventory(inv);
    }

    public static void reopen(Player p){
        Integer size = DataManager.get().getInt(p.getUniqueId().toString() + ".size");
        Inventory inv = Bukkit.createInventory(p, size, p.getDisplayName() + "'s Bag o' Holding");
        for(int i = 0; i < size; i++){
            inv.setItem(i, DataManager.get().getItemStack(p.getUniqueId().toString() + ".content." + i));
        }
        inv = upgradeRow(p, inv);
        p.openInventory(inv);
    }

    public static void saveInv(Player p, Inventory inv){
        for(int i = 0; i < inv.getSize(); i++){
            DataManager.get().set(p.getUniqueId().toString() + ".content." + i, inv.getItem(i));
        }
        DataManager.save();
    }

    public static boolean isBundle(Player p){
        if(p.getInventory().getItemInMainHand() != null){
            if(p.getInventory().getItemInMainHand().getType() == Material.BUNDLE){
                if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LURE)){
                    return true;
                } else return false;
            } else return false;
        } else return false;
    }

    public static List<ItemStack> logTypes(){
        List<ItemStack> logs = new ArrayList<>();
        logs.add(new ItemStack(Material.ACACIA_LOG));
        logs.add(new ItemStack(Material.BIRCH_LOG));
        logs.add(new ItemStack(Material.DARK_OAK_LOG));
        logs.add(new ItemStack(Material.JUNGLE_LOG));
        logs.add(new ItemStack(Material.MANGROVE_LOG));
        logs.add(new ItemStack(Material.OAK_LOG));
        logs.add(new ItemStack(Material.SPRUCE_LOG));
        return logs;
    }

    public static ItemStack backpack(){
        ItemStack b = new ItemStack(Material.BUNDLE, 1);
        BundleMeta bMeta = (BundleMeta) b.getItemMeta();

        bMeta.setDisplayName(ChatColor.AQUA + "Bag o' Holding");
        bMeta.addEnchant(Enchantment.LURE, 1, true);
        bMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        b.setItemMeta(bMeta);

        return b;
    }
}
