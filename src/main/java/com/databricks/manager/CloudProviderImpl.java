package com.databricks.manager;
import java.util.*;

public class CloudProviderImpl implements CloudProvider{
	private double instanceSizeGB;
	private Set<Instance> existedInstances;
	public CloudProviderImpl(double instanceSizeGB) {
		this.instanceSizeGB = instanceSizeGB;
		existedInstances = new HashSet<Instance>();
	}
	/**
     * Synchronously request a new instance. The size of the instance will depend on the classes
     * that implement this interface. Note that this call may be blocked for a long period of time
     * before it is fulfilled (i.e. a few minutes). The implementation of this method must be
     * thread-safe.
     */
	public Instance requestInstance() {
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		Instance newInstance = new InstanceImpl(instanceSizeGB);
		existedInstances.add(newInstance);
		return newInstance;
	}
	
	public synchronized Set<Instance> getAllInstances(){
		return new HashSet<Instance>(existedInstances);
	}
}
