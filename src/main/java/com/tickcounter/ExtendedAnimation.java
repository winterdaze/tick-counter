package com.tickcounter;

public class ExtendedAnimation
{
	public int delta;
	public int animation;
	public int weapon;
	public int lastTick = 0;
	public boolean firstHit = true;

	public ExtendedAnimation(int delta, int animation, int weapon, int currentTick)
	{
		this.delta = delta;
		this.animation = animation;
		this.weapon = weapon;
		this.lastTick = currentTick;
	}
}
