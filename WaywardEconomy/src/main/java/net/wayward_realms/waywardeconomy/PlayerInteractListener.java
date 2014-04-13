package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerInteractListener implements Listener {

    private WaywardEconomy plugin;

    public PlayerInteractListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getClickedBlock().getState() instanceof Sign && event.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (event.getClickedBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest) {
                    Chest chest = (Chest) event.getClickedBlock().getRelative(BlockFace.DOWN).getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")) {
                        validateShopSign(sign, event.getPlayer());
                        if (sign.getLine(1).toLowerCase().contains("buy")) {
                            event.getPlayer().openInventory(chest.getInventory());
                        } else if (sign.getLine(1).toLowerCase().contains("sell")) {
                            if (event.getPlayer().getItemInHand().getType() == Material.matchMaterial(sign.getLine(1).split(" ")[1].replace(' ', '_'))) {
                                if (event.getPlayer().getItemInHand().getAmount() >= Integer.parseInt(sign.getLine(2).split(" ")[2])) {
                                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                    if (characterPluginProvider != null) {
                                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                        try {
                                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Sold " + Integer.parseInt(sign.getLine(1).split(" ")[2]) + " x " + event.getPlayer().getItemInHand().getType().toString().toLowerCase().replace('_', ' ') + " for " + Integer.parseInt(sign.getLine(2).replace("for ", "")) + " " + (Integer.parseInt(sign.getLine(2).replace("for ", "")) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()));
                                            plugin.transferMoney(characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))), characterPlugin.getActiveCharacter(event.getPlayer()), Integer.parseInt(sign.getLine(2).replace("for ", "")));
                                            ItemStack item = new ItemStack(event.getPlayer().getItemInHand());
                                            item.setAmount(Integer.parseInt(sign.getLine(1).split(" ")[2]));
                                            chest.getInventory().addItem(item);
                                            if (event.getPlayer().getItemInHand().getAmount() > Integer.parseInt(sign.getLine(1).split(" ")[2])) {
                                                event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - Integer.parseInt(sign.getLine(1).split(" ")[2]));
                                            } else {
                                                event.getPlayer().setItemInHand(null);
                                            }

                                        } catch (NumberFormatException ignored) {
                                        }
                                    }
                                } else {
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough " + event.getPlayer().getItemInHand().getType().toString().toLowerCase().replace('_', ' ') + "s to sell");
                                }
                            } else {
                                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding at least " + Integer.parseInt(sign.getLine(1).split(" ")[2]) + " " + Material.matchMaterial(sign.getLine(1).split(" ")[1].replace(' ', '_')).toString().toLowerCase().replace('_', ' ') + "s");
                            }
                        }
                    }
                }
            }
        }
    }

    private void validateShopSign(Sign sign, Player player) {
        if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                try {
                    if (characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))) != null) {
                        String playerName = characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))).getPlayer().getName();
                        if (!(sign.getLine(1).toLowerCase().contains("buy ") || sign.getLine(1).toLowerCase().contains("sell ")) || !(StringUtils.countMatches(sign.getLine(1), " ") == 2 && sign.getLine(1).toLowerCase().contains("sell "))) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                            return;
                        }
                        try {
                            Integer.parseInt(sign.getLine(1).split(" ")[sign.getLine(1).contains("buy") ? 1 : 2]);
                        } catch (NumberFormatException exception) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                            return;
                        }
                        if (sign.getLine(1).toLowerCase().contains("sell")) {
                            if (Material.matchMaterial(sign.getLine(1).split(" ")[1].replace(' ', '_')) == null) {
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                                return;
                            }
                        }
                        try {
                            if (!sign.getLine(2).contains("for ")) {
                                sign.setLine(2, "for " + Integer.parseInt(sign.getLine(2)));
                            } else {
                                Integer.parseInt(sign.getLine(2).replace("for ", ""));
                            }
                        } catch (NumberFormatException exception) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                        }
                    }
                } catch (NumberFormatException exception) {
                    player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get the shop owner to update accordingly.");
                }
            }
        }
    }

}
