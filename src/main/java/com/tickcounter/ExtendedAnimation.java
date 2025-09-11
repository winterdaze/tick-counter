package com.tickcounter;

public class ExtendedAnimation
{
	public int delta;
	public int animation;
	public int weapon;
	public int lastTick;

	public ExtendedAnimation(int delta, int animation, int weapon, int currentTick)
	{
		this.delta = delta;
		this.animation = animation;
		this.weapon = weapon;
		this.lastTick = currentTick;
	}

	public int eligibleAt()
	{
		return lastTick + delta;
	}
}
