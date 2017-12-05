package com.databricks.manager;

public class placeContainerThread implements Runnable{
	private ResourceManager rm;
	private int numContainers;
	public placeContainerThread(ResourceManager rm, int numberOfContainer) {
		this.rm = rm;
		this.numContainers = numberOfContainer;
	}
	public void run() {
		rm.placeContainers(numContainers);
	}
}
