package core.luxury.material;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;

public class XItems implements Listener {

    public static void setLockedItem(Player player, ItemStack item, int slot) {
        Inventory inventory = player.getInventory();
        inventory.setItem(slot, item);
    }

    @SuppressWarnings("unused")
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.PLAYER) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                ItemStack currentItem = event.getCurrentItem();

                if (currentItem != null && currentItem.getType() == Material.SLIME_BALL) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
