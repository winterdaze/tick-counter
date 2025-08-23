package com.tickcounter;

public class ExtendedAnimation
{
	public int delta;
	public int weapon;
	public int lastTick = 0;

	ExtendedAnimation(int delta, int weapon)
	{
		this.delta = delta;
		this.weapon = weapon;
	}
}
