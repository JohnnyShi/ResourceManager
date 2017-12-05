# Databricks Coding Assignment: Resource Manager

For the integrity of the process, please do not distribute or make public the assignment. Thank you!

### Prerequisites

1. Java 7 or higher.
2. Maven 3.3 or higher (not a strict prerequisite)

### Background

Suppose you want to offer a service that sets up cloud-based Spark clusters for users. One thing you
will need is __a resource manager__ to assign Spark workers to the cloud instances you acquire. Based on
the size of the requested cluster, the resource manager should **request a set of instances from a**
**cloud provider and place containers (lightweight virtual machines) on these instances** (where each
container runs a Spark worker). When there is a request to shut down a cluster, the resource manager
should unplace containers from instances.

### Instructions

The goal of this assignment is to implement a resource manager. The resource manager should take in
three parameters: **the cloud provider**, **the memory size in GB** of the instances that the cloud provider
provides, and the **memory size in GB of the containers** you want to place on the instances. It should then respond to requests for new containers by **managing a pool of available instances**, tracking the
containers that have already been placed on them, and requesting new instances as necessary. Your
teammate has already implemented an interface for placing and unplacing containers on a single
instance.

### Requirements

1. The resource manager should create the *minimal* number of instances when placing multiple
   containers. For example, if the instance size is 60 GB and the container size is 6 GB, placing up
   to 10 containers should only result in a single new instance.
   * The resource manager should **NOT make any instance requests when there is enough space on an**
     **existing instance *or a pending instance** (i.e., an instance that has been requested but has not
     yet been returned by the cloud provider)*. For example, if all instances are full and a user
     requests another container, the resource manager should request a new instance on which to
     place that container. If, **while waiting for that new instance to become available, the resource**
     **manager receives another request to place a container, it should not request another**
     **instance, since it will eventually be able to place the container on the pending instance.**
2. `placeContainers` should __return within *constant time*__ with respect to the *number of instances*
   requested by the call, the *number of containers* requested by the call, and the *number of
   concurrent* `placeContainers` *calls*. This constant time should be dominated by the time for the
   Cloud Provider to create one instance. You may assume that the Cloud Provider is scalable, i.e.
   the time for the Cloud Provider to create one instance is the same as the time to create multiple
   instances.
3. The resource manager must __load the current state of the world from the cloud provider on startup__.
4. Your code should have good readability and modularity. It should be easy to understand what your
   code is doing and why.
5. Your submission should include unit tests that test all functionality of your code (i.e., in a
   suite called ResourceManagerImplTest.java). We recommend using JUnit and a mocking library
   (Mockito is a good one) for these tests.
6. Your unit tests should run very quickly (on the order of seconds).
7. Your implementation must be in Java.

### Assumptions

1. There will be no failures when making requests to the cloud provider for instances.
2. The cloud provider creates instances in parallel, i.e., the time for the cloud provider to create
   one instance is the same as the time to create 10 instances.
3. The resource manager's state (i.e., the instances and the containers objects) can fit in memory.
4. You will never have to handle more than 100 simultaneous requests across all of the resource
   manager's methods.
5. You may assume that there will only be a single instance of the resource manager running at any
   given time.
6. The resource manager can hold onto instances from the cloud provider even after it has unplaced
   all containers from those instances.

### Submission

After completion of the assignment, please zip the folder, and email us the zip file as your
submission. Please do NOT upload the assignment or your submission on any public location.


**Databricks Confidential and Proprietary**
