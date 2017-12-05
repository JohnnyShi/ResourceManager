package com.databricks.manager;

import static org.junit.Assert.assertEquals;
import java.util.*;
import org.junit.Test;
/**
 * This suite should test basic functionality of ResourceManagerImpl implementation.
 */
public class ResourceManagerImplTest {
	private static double instanceSize = 60;
	private static CloudProviderImpl cp;
	
	@Test
	public void testPlaceContainerOneTime() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		rm.placeContainers(7);
		assertEquals(1, cp.getAllInstances().size());
	}
	
	@Test
	public void testPlaceContainerMultipleTimesOnSameInstance() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		rm.placeContainers(2);
		rm.placeContainers(1);
		rm.placeContainers(3);
		rm.placeContainers(3);
		assertEquals(1, cp.getAllInstances().size());
	}
	
	@Test
	public void testCreateNecessaryNewInstance() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		rm.placeContainers(7);
		rm.placeContainers(5);
		assertEquals(2, cp.getAllInstances().size());
	}
	
	@Test
	public void testUnplaceContainerOneTime() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		List<ContainerId> list = rm.placeContainers(7);
		rm.unplaceContainer(list.get(0));
		rm.placeContainers(5);
		assertEquals(1, cp.getAllInstances().size());
	}
	
	@Test
	public void testParallelInstanceRequest() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		rm.placeContainers(600);
		assertEquals(60, cp.getAllInstances().size());
	}
	
	@Test
	public void testHighVolumnPlace() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		rm.placeContainers(10);
		rm.placeContainers(20);
		rm.placeContainers(30);
		rm.placeContainers(40);
		assertEquals(10, cp.getAllInstances().size());
	}
		
	@Test
	public void testPlaceAndUnplaceContainerManyTimes() {
		cp = new CloudProviderImpl(instanceSize);
		ResourceManagerImpl rm = new ResourceManagerImpl(cp, instanceSize, 6);
		List<ContainerId> list = new ArrayList<ContainerId>();
		List<Integer> nums = Arrays.asList(1,2,3,4);
		for (int i = 0; i < 20; i++) {
			Collections.shuffle(nums);
			list.clear();
			for (int j = 0; j < nums.size(); j++) {list.addAll(rm.placeContainers(nums.get(j)));}
			for (int j = 0; j < nums.size(); j++) {rm.unplaceContainer(list.get(nums.get(j)-1));}
		}
		assertEquals(1, cp.getAllInstances().size());
	}
}
