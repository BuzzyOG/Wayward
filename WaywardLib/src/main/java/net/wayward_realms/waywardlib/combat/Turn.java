package net.wayward_realms.waywardlib.combat;

import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a turn in a fightWaywardPlugin
 *
 */
public interface Turn {

    /**
     * Gets the fight this turn occurs in
     *
     * @return the fight
     */
    public Fight getFight();

    /**
     * Gets the combatant performing the attack in the turn
     *
     * @return the attacker
     */
    public Combatant getAttacker();

    /**
     * Sets the combatant performing the attack in the turn
     *
     * @param attacker the attacker to set
     */
    public void setAttacker(Combatant attacker);

    /**
     * Gets the combatant defending against the attack in the turn
     *
     * @return the defender
     */
    public Combatant getDefender();

    /**
     * Sets the combatant defending against the attack in the turn
     *
     * @param defender the defender to set
     */
    public void setDefender(Combatant defender);

    /**
     * Gets the weapon being used to attack in the turn
     *
     * @return the weapon
     */
    public ItemStack getWeapon();

    /**
     * Sets the weapon being used to attack in the turn
     *
     * @param weapon the weapon to set
     */
    public void setWeapon(ItemStack weapon);

    /**
     * Gets the skill being used in the turn
     *
     * @return the skill
     */
    public Skill getSkill();

    /**
     * Sets the attack being used in the turn
     *
     * @param skill the skill to set
     */
    public void setSkill(Skill skill);

}
