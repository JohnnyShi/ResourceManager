package com.databricks.manager;

/**
 * You should NOT modify this file for the assignment.
 *
 * API provided by an engineer on your team that represents ID if the instance. This is just a
 * wrapper class for the long representing a instance ID.
 */
public class InstanceId {

    private Long id;

    public InstanceId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstanceId)) {
            return false;
        }

        InstanceId other = (InstanceId) object;
        return getId().equals(other.getId());
    }
}
