package com.databricks.manager;

import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * The resource manager should place and unplace containers onto instances. The resource manager
 * should take in a cloud provider, a fixed instance size, and a fixed container size.
 */
public class ResourceManagerImpl implements ResourceManager {

    // ADD YOUR CODE HERE.
	private double instanceSizeGB;
	private double containerSizeGB;
	private int containerPerInstance;
	private AtomicInteger availableNumContainers = new AtomicInteger(0);
	private CloudProvider provider;
	private Map<InstanceId, Instance> database;
	private Map<ContainerId, InstanceId> containerIdToInstanceIdMap;
    /**
     * Constructor for the resource manager.
     *
     * @param provider the cloud provider that we request instances from.
     * @param instanceSizeGB the size of all instances that is provided by the cloud provider.
     * @param containerSizeGB the size of all containers we want to place on instances.
     */
    public ResourceManagerImpl(CloudProvider provider, double instanceSizeGB, double containerSizeGB) {
        // ADD YOUR CODE HERE.
    		this.provider = provider;
    		this.instanceSizeGB = instanceSizeGB;
    		this.containerSizeGB = containerSizeGB;
    		containerPerInstance = (int)(this.instanceSizeGB / this.containerSizeGB);
    		database = new HashMap<InstanceId, Instance>();
    		containerIdToInstanceIdMap = new HashMap<ContainerId, InstanceId>();
    }

    /**
     * Places numContainers containers onto instances. If there are not enough resources with the
     * instances we currently have, request from the cloud provider for more instances. This method
     * should only return once all containers have been successfully placed. This method is
     * thread-safe.
     *
     * @param numContainers number of containers to place.
     * @return a list of containerIds which are IDs that are globally unique across all instances.
     */
    public List<ContainerId> placeContainers(int numContainers) {
        // ADD YOUR CODE HERE.
    		List<ContainerId> result = new ArrayList<ContainerId>();
    		while (numContainers > 0) {
    			Set<Instance> instances = provider.getAllInstances();
    			for (Instance ins : instances) {
    				int slots = (int)(ins.getRemainingMemoryGB() / containerSizeGB);
    				if (slots == 0) continue;
    				ContainerId cid = ins.placeContainer(containerSizeGB * Math.min(numContainers, slots));
    				availableNumContainers.addAndGet(-Math.min(numContainers, slots));
    				containerIdToInstanceIdMap.put(cid, ins.getInstanceId());
    				database.put(ins.getInstanceId(), ins);
    				result.add(cid);
    				if (slots >= numContainers) return result;
    				numContainers -= slots;
    			}
    			applyNewInstance(numContainers);
    		}
    		return result;
    }
    
    /**
     * Apply for new instances when the input number of containers can't fit in the current instances. It should
     * request instances in parallel.
     * 
     * @param numContainers Number of containers to put on instances
     */
    private synchronized void applyNewInstance(int numContainers){
    		if (numContainers >= availableNumContainers.get()) {
    			int num = numContainers/containerPerInstance + (numContainers % containerPerInstance == 0 ? 0 : 1);
    			ExecutorService pool = Executors.newFixedThreadPool(num);
        		ExecutorCompletionService<Instance> services = new ExecutorCompletionService<Instance>(pool);
        		int count = num;
        		while (count-- > 0) {
        			services.submit(new Callable<Instance>(){
        				public Instance call() throws Exception{
        					return provider.requestInstance();
        				}
        			});
        		}
        		int complete = 0;
        		while (complete < num) {
        			try {
        				Future<Instance> future = services.take();
        				future.get();
        			} catch (Exception e) {
        				System.err.println("Unable to get a future instance");
        			}
        			complete++;
        		}
        		availableNumContainers.addAndGet(complete * containerPerInstance);
    		}
    }

    /**
     * Get the ID of the instance where the input container is placed on.
     * This method is thread-safe.
     *
     * @param containerId the container id.
     * @return id of the instance where container resides.
     * @throws IllegalArgumentException if this container does not exist (i.e., is not placed on an
     *                                  instance).
     */
    public InstanceId getInstanceId(ContainerId containerId) {
        // ADD YOUR CODE HERE.
    		InstanceId id = containerIdToInstanceIdMap.get(containerId);
    		if (containerId == null || id == null) {
    			throw new IllegalArgumentException("No container with id " + containerId + " exist on the instance.");
    		}
    		return id;
    }

    /**
     * Unplace a container from its host instance. This method is thread-safe.
     *
     * @param containerId the container id.
     * @return id of the instance where container resided.
     * @throws IllegalArgumentException if this container does not exist (i.e., is not placed on an
     *                                  instance).
     */
    public InstanceId unplaceContainer(ContainerId containerId) {
        // ADD YOUR CODE HERE.
    		InstanceId id = getInstanceId(containerId);
    		if (id == null) {
    			throw new IllegalArgumentException("No container with id " + containerId + " exist on the instance.");
    		}
    		Instance ins = database.get(id);
    		double beforeMemory = ins.getRemainingMemoryGB();
    		ins.unplaceContainer(containerId);
    		availableNumContainers.getAndAdd((int)((ins.getRemainingMemoryGB() - beforeMemory) / containerSizeGB));
    		return id;
    }
}
