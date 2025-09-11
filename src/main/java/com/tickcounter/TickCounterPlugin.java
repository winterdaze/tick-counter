package com.tickcounter;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.MenuAction;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Tick Counter"
)
public class TickCounterPlugin extends Plugin
{
	public static final String CONFIG_GROUP = "tickcounter";
	public static final String CONFIG_KEY_RESET_INSTANCE = "resetInstance";
	public static final String CONFIG_KEY_INSTANCE_ONLY = "instanceOnly";

	private static final int SPOTANIM_BLOOD_RAGER = 2792;

	@Inject
	private OverlayManager overlayManager;
	@Inject
	private TickCounterConfig config;
	@Inject
	private Client client;

	@Provides
	TickCounterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TickCounterConfig.class);
	}
	@Inject
	private TickCounterOverlay overlay;

	Map<String, Integer> activity = new HashMap<>();

	private HashMap<Player, ExtendedAnimation> extendedAnims = new HashMap<>();
	boolean instanced = false;
	boolean prevInstance = false;

	private boolean resetOnInstance = true;
	private boolean trackInstanceOnly = false;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);

		resetOnInstance = config.instance();
		trackInstanceOnly = config.instanceOnly();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		activity.clear();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		if (!(e.getActor() instanceof Player) || (trackInstanceOnly && !instanced))
			return;
		Player p = (Player) e.getActor();
		int weapon = -1;
		int animation = p.getAnimation();
		if (p.getPlayerComposition() != null)
			weapon = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
		int delta = 0;
		int currentTick = client.getTickCount();
		ExtendedAnimation curExtAnim = extendedAnims.get(p);
		ExtendedAnimation extAnim = null;

		switch (animation)
		{
			case 7617: // rune knife
			case 8194: // dragon knife
			case 8291: // dragon knife spec
			case 5061: // blowpipe
			case 10656: // blazing blowpipe
				if (weapon == 12926 || weapon == 28688)
				{
					extAnim = new ExtendedAnimation(2, animation, weapon, currentTick);
				}
				else
				{
					delta = 2;
				}
				break;
			case 2323: // Event rpg
			case 9964: // Webweaver bow spec
			case 2075: // Karil's crossbow auto
			case 7618: // Chinchompas
			case 11057: // Eclipse atlatl
			case 11060: // Eclipse atlatl spec
			case 12397: // Eye of Ayak
				if (weapon == 31113) {
					extAnim = new ExtendedAnimation(3, animation, weapon, currentTick);
				}
				else
				{
					delta = 3;
				}
				break;
			case 426: // bow shoot
				if (weapon == 11235 || weapon == 12765 || weapon == 12766 || weapon == 12767 || weapon == 12768 || weapon == 27853) // dark bow
					delta = 8;
				else if (weapon == 20997) // twisted bow
					delta = 5;
				else if (weapon == 25865 || weapon == 25867 || weapon == 25869 || weapon == 25884 || weapon == 25886 || weapon == 25888 || weapon == 25890 || weapon == 25892 || weapon == 25894 || weapon == 25896)
					// Bow of Faerdhinen
					delta = 4;
				else // shortbow
					delta = 3;
				break;
			case 376: // dds poke
			case 377: // dds slash
			case 422: // punch
			case 423: // kick
			case 386: // lunge
			case 390: // generic slash
				if (weapon == 24219) // swift blade
				{
					delta = 3;
					break;
				}
				if (weapon == 26219 || weapon == 27246) // Osmumten's Fang & Osmunten's Fang (or)
				{
					delta = 5;
					break;
				}
			case 1062: // dds spec
			case 1067: // claw stab
			case 1074: // msb spec
			case 1658: // Whip
			case 9963: // Ursine Chainmace Spec
			case 380: // spear slash
			case 382: // spear crush
			case 381: // Keris partisan stab
			case 419: // Keris partisan crush
			case 9961: // Accursed sceptre spec
			case 245: // Wildy Maces
			case 414: // Wand melee auto
			case 1132: // Saradomin Sword Special Attack
			case 1133: // Saradomin's Blessed Sword Special Attack
			case 11275: // Voidwaker Special Attack
			case 11140: // Burning claws Special Attack
			case 10501: // Warped sceptre
			case 2890: // arclight spec
			case 11138: // emberlight spec
			case 3294: // abby dagger slash
			case 3297: // abby dagger poke
			case 3298: // bludgeon attack
			case 3299: // bludgeon spec
			case 3300: // abby dagger spec
			case 4198: // bone dagger spec
			case 7514: // claw spec
			case 9858: // Venator bow
			case 10989: // Dual macuahuitl
				if (client.getLocalPlayer().hasSpotAnim(SPOTANIM_BLOOD_RAGER)) // Blood moon effect
				{
					delta = 3;
					break;
				}
			case 7515: // d sword spec
			case 8145: // rapier stab
			case 8288: // dhl stab
				if (weapon == 24219) // swift blade
				{
					delta = 3;
					break;
				}
			case 8289: // dhl slash
			case 8290: // dhl crush
			case 4503: // inquisitor's mace crush
			case 12342: // earthbound tecpatl
				delta = 4;
				break;
			case 1710: // zamorakian spear stab
			case 1711: // zamorakian spear crush
			case 1712: // zamorakian spear slash
				if (weapon == 28988) // Blue moon spear
				{
					delta = 5;
					break;
				}
				if (config.showZamorakianSpear())
				{
					delta = 4;
					break;
				}
				else
				{
					delta = 0;
					break;
				}
			case 393: // staff bash
				if (weapon == 13652 || weapon == 28039 || weapon == 29577)
				{ // claw scratch
					delta = 4;
					break;
				}
			case 395: // axe autos
			case 400: // pick smash
				if (weapon == 24417) // inquisitor's mace stab
				{
					delta = 4;
					break;
				}
			case 11430: // trident cast and wave spells w/staff
				if (weapon == 25731 || weapon == 22323 || weapon == 22288 || weapon == 22292 || weapon == 12899 || weapon == 11907 || weapon == 11905) // tridents
				{
					delta = 4;
					break;
				}
			case 708: // iban's blast
			case 710: // bind/snare/entangle
			case 9144: // strike/bolt/blast
			case 718: // vulnerability
			case 724: // crumble undead
			case 11429: // wave spells
			case 1161: // bind/snare/entangle w/ staff
			case 11423: // strike/bolt/blast w/ staff
			case 1165: // vulnerability w/ staff
			case 1166: // crumble undead w/ staff
			case 1576: // magic dart
			case 9145: // surge spells
				if (weapon == 24423) // harmonised staff
				{
					delta = 4;
					break;
				}
			case 7552: // generic crossbow
				if (weapon == 28869 || weapon == 10156) // Hunters' Sunlight crossbow & Hunters' crossbow
				{
					delta = 3;
					break;
				}
				if (weapon == 8880) // Dorgeshuun crossbow
				{
					delta = 4;
					break;
				}
			case 2062: // Verac's flail
				if (weapon == 28792) // Bone mace
				{
					delta= 4;
					break;
				}
			case 2068: // Torag's hammers,
				if (weapon == 29084 || weapon == 29889) // Sulphur blades, Glacial temotli
				{
					delta = 4;
					break;
				}
			case 429: // Leaf-bladed spear crush
			case 8972: // Undead grasp
			case 7004: // Zombie axe slash
			case 3852: // Zombie axe crush
			case 1058: // wildy sceptres melee auto
			case 1064: // dspear spec
			case 10091: // rush and blitz spell cast
			case 10092: // burst and barrage spell cast
			case 2082: // Guthan's warspear crush
			case 2080: // Guthan's warspear stab
			case 2081: // Guthan's warspear slash
			case 6147: // ancient mace spec
			case 7511: // dinh's attack
			case 8056: // scythe swing
			case 8010: // Blisterwood flail
			case 9168: // Zaryte crossbow
			case 9471: // Osmumten's fang stab
			case 9493: // Tumeken's shadow
			case 10171: // Soulreaper axe crush auto
			case 10172: // Soulreaper axe auto
			case 10173: // Soulreaper axe spec
			case 11222: // Osmumten's fang Special
			case 12394: // Eye of Ayak Special
				if (weapon == 31113) {
					extAnim = new ExtendedAnimation(5, animation, weapon, currentTick);
				}
				else
				{
					delta = 5;
				}
				break;
			case 401:
				if (weapon == 13576) // dwh bop
					delta = 6;
				else if (weapon == 23360) // ham joint
					delta = 3;
				else // used by pickaxe and axe
					delta = 5;
				break;
			case 1378: // Dragon warhammer spec
			case 7045: // Godsword Slash
			case 7054: // Godsword Smash
			case 7055: // Godsword Block
				if (weapon == 12809 || weapon == 11838) // Saradomin Sword + Blessed Variant
				{
					delta = 4;
					break;
				}
			case 2078: // Ahrim's staff
			case 7516: // Elder maul attack
			case 7555: // ballista attack
			case 7556: // ballista spec
			case 7638: // zgs spec
			case 7640: // sgs spec
			case 7642: // bgs spec
			case 7643: // bgs spec
			case 7644: // ags spec
			case 5865: // barrelchest anchor
			case 5870: // barrelchest anchor spec
			case 10427: // ags spec (deadman)
			case 10914: // Ralos Basic Attack
			case 10923: // Ralos Special Attack
			case 11124: // Elder maul spec
				delta = 6;
				break;
			case 428: // Crystal halberd stab
			case 440: // Crystal halberd slash
				if (weapon == 29796 || weapon == 4158) // Noxious halberd & Leaf-bladed spear
				{
					delta = 5;
					break;
				}
			case 2066: // Dharok's greataxe crush
			case 2067: // Dharok's greataxe slash
			case 10079: // felling axe autos
			case 1203: // chally spec
				delta = 7;
				break;
			case 9544: // Keris of Corruption Spec
				delta = 8;
				break;
			case -1: // Animation reset
				delta = -1;
				break;
		}
		if (delta > 0)
		{
			String name = p.getName();
			this.activity.put(name, this.activity.getOrDefault(name, 0) + delta);
		}
		else if (extAnim != null) // new extended animation (blowpipe/ayak)
		{
			extendedAnims.put(p, extAnim);
		}

		// Extended animation reset (ie stop attacking or changed to non-tracking animation)
		if (curExtAnim != null && (delta != 0 || extAnim != null))
		{
			// track pending activity if eligible
			if (currentTick >= curExtAnim.eligibleAt() || extAnim != null)
			{
				logExtendedActivity(p, currentTick, curExtAnim);
			}

			// remove if animation changed or reset
			if (delta != 0)
			{
				extendedAnims.remove(p);
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		final int currentTick = client.getTickCount();

		for (Map.Entry<Player, ExtendedAnimation> entry : extendedAnims.entrySet())
		{
			ExtendedAnimation extAnim = entry.getValue();
			if (currentTick >= extAnim.eligibleAt())
			{
				logExtendedActivity(entry.getKey(), currentTick, extAnim);
			}
		}

		prevInstance = instanced;
		instanced = client.isInInstancedRegion();
		if (resetOnInstance && !prevInstance && instanced)
		{
			reset();
		}
	}

	private void logExtendedActivity(Player p, int currentTick, ExtendedAnimation extAnim)
	{
		String name = p.getName();
		int activity = this.activity.getOrDefault(name, 0).intValue();
		this.activity.put(name, activity + extAnim.delta);
		extAnim.lastTick = currentTick;
	}

	@Subscribe
	public void onOverlayMenuClicked(OverlayMenuClicked event)
	{
		if (event.getEntry().getMenuAction() == MenuAction.RUNELITE_OVERLAY &&
			event.getEntry().getTarget().equals("Tick counter") &&
			event.getEntry().getOption().equals("Reset"))
		{
			reset();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(CONFIG_GROUP))
		{
			return;
		}

		if (event.getKey().equals(CONFIG_KEY_RESET_INSTANCE))
		{
			resetOnInstance = config.instance();
		}

		if (event.getKey().equals(CONFIG_KEY_INSTANCE_ONLY))
		{
			trackInstanceOnly = config.instanceOnly();

			// "Instance only" has been toggled and the player is not in an instance so we can reset the counter
			if (trackInstanceOnly && !instanced)
			{
				log.debug("Instance only enabled, resetting tick counter");
				reset();
			}
		}
	}

	private void reset()
	{
		activity.clear();
		extendedAnims.clear();
	}
}
