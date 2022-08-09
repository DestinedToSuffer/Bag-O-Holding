package co.vangar.bagoholding.bag;

import co.vangar.bagoholding.Bagoholding;
import co.vangar.bagoholding.files.DataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class bag implements Listener {

    @EventHandler
    public void rClickBackpack(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action act = e.getAction();

        if(act == Action.RIGHT_CLICK_BLOCK || act == Action.RIGHT_CLICK_AIR){
            if(utils.isBundle(p)){
                e.setCancelled(true);
                utils.openBag(p);
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();

        if(e.getView().getTitle().toLowerCase().contains(p.getDisplayName().toLowerCase())){
            utils.saveInv(p, inv);
        }
    }

    @EventHandler
    public void clickInv(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        if(e.getView().getTitle().toLowerCase().contains(p.getDisplayName().toLowerCase())){
            if(e.getCurrentItem() != null){
                if(e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE){
                    if(e.getCurrentItem().getItemMeta().isUnbreakable()){
                        e.setCancelled(true);
                    }
                } else if(e.getCurrentItem().getType() == Material.ENDER_EYE){
                    if(e.getCurrentItem().getItemMeta().isUnbreakable()){
                        e.setCancelled(true);
                        if(!e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("cost")){
                            if(!e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("max")){
                                p.closeInventory();
                                utils.upgradeMyBag(p, e.getClickedInventory());
                            }
                        }
                    }
                }
            }
        }
    }
}
