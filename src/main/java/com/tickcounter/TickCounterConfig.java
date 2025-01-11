package com.tickcounter;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup(TickCounterPlugin.CONFIG_GROUP)
public interface TickCounterConfig extends Config
{
	@ConfigItem(
		keyName = TickCounterPlugin.CONFIG_KEY_RESET_INSTANCE,
		name = "Reset on new instances",
		description = "",
		position = 1
	)
	default boolean instance()
	{
		return true;
	}

	@ConfigItem(
		keyName = TickCounterPlugin.CONFIG_KEY_INSTANCE_ONLY,
		name = "Only track inside instances",
		description = "The tick counter will only work inside raids and some PvM encounters",
		position = 2
	)
	default boolean instanceOnly()
	{
		return false;
	}

	@ConfigItem(
		keyName = "titleEnabled",
		name = "Show title bar",
		description = "",
		position = 3
	)
	default boolean titleEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "titleColor",
			name = "Title color",
			description = "",
			position = 4
	)
	default Color titleColor()
	{
		return Color.GREEN;
	}

	@Alpha
	@ConfigItem(
		keyName = "selfColor",
		name = "Your color",
		description = "",
		position = 5
	)
	default Color selfColor()
	{
		return Color.CYAN;
	}

	@Alpha
	@ConfigItem(
			keyName = "otherColor",
			name = "Other players color",
			description = "",
			position = 6
	)
	default Color otherColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
		keyName = "totalEnabled",
		name = "Show total ticks",
		description = "",
		position = 7
	)
	default boolean totalEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "totalColor",
		name = "Total color",
		description = "",
		position = 8
	)
	default Color totalColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "showZamorakianSpear",
		name = "Include Zamorakian Spear",
		description = "",
		position = 9
	)
	default boolean showZamorakianSpear()
	{
		return true;
	}
}
