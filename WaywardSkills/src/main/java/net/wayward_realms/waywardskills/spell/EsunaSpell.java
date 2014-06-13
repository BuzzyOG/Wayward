package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EsunaSpell extends SpellBase {

    private int radius = 8;

    public EsunaSpell() {
        setName("Esuna");
        setManaCost(5);
        setCoolDown(5);
        setType(SkillType.MAGIC_HEALING);
    }

    @Override
    public boolean use(Player player) {
        for (LivingEntity entity : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
            if (player.getLocation().distanceSquared(entity.getLocation()) <= radius * radius) {
                for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                    if (potionEffectType != null) {
                        entity.addPotionEffect(new PotionEffect(potionEffectType, 0, 0), true);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= getManaCost()) {
            for (StatusEffect statusEffect : StatusEffect.values()) {
                fight.setStatusTurns(defending, statusEffect, 0);
            }
            fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " cured " + defending.getName() + "'s status effects");
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " attempted to cure " + defending.getName() + "'s status effects, but did not have enough mana!");
            return false;
        }
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Esuna");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_HEALING) >= 1;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return canUse(characterPlugin.getActiveCharacter(player));
        }
        return false;
    }

}
