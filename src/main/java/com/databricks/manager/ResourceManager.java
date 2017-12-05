package com.databricks.manager;

import java.util.List;

/**
 * You should NOT modify this file for the assignment.
 *
 * This is the interface you want to implement. The resource manager should place and unplace
 * containers onto instances. The resource manager should take in a cloud provider, a fixed instance
 * size, and a fixed container size.
 */
public interface ResourceManager {

    /**
     * Places numContainers containers onto instances. If there are not enough resources with the
     * instances we currently have, request from the cloud provider for more instances. This method
     * should only return once all containers have been successfully placed. The implementation
     * of this method must be thread-safe.
     *
     * @param numContainers number of containers to place.
     * @return a list of containerIds which are IDs that are globally unique across all instances.
     */
    List<ContainerId> placeContainers(int numContainers);

    /**
     * Get the ID of the instance where the input container is placed on. The implementation of
     * this method must be thread-safe.
     *
     * @param containerId the container id.
     * @return id of the instance where container resides.
     * @throws IllegalArgumentException if this container does not exist (i.e., is not placed on an
     *                                  instance).
     */
    InstanceId getInstanceId(ContainerId containerId);

    /**
     * Unplace a container from its host instance. The implementation of this method must be
     * thread-safe.
     *
     * @param containerId the container id.
     * @return id of the instance where container resided.
     * @throws IllegalArgumentException if this container does not exist (i.e., is not placed on an
     *                                  instance).
     */
    InstanceId unplaceContainer(ContainerId containerId);
}
