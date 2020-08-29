package org.acme.facilitylocation.domain;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

/**
 * Facility satisfies consumers' demand. Cumulative demand of all consumers assigned to this facility must not exceed
 * the facility's capacity. This requirement is expressed by the {@link FacilityLocationConstraintProvider#facilityCapacity
 * facility capacity} constraint.
 */
// This is a shadow planning entity, not a genuine planning entity, because it has a shadow variable (consumers).
@PlanningEntity
public class Facility {

    private long id;
    private Location location;
    private long setupCost;
    private long height;
    private boolean fiberReq;
    private long fiberSetupUnitCost;
    private long fiberPullTotalCost;
	private double fiberPullDistance;
	private boolean fixedFacility;
    private long capacity;

    @InverseRelationShadowVariable(sourceVariableName = "facility")
    private List<Consumer> consumers = new ArrayList<>();

    public Facility() {
    }

    public Facility(long id, Location location, long setupCost, 
    				boolean fixedFacility, long height, 
    				long capacity, boolean fiberReq, long fiberSetupUnitCost) {
        this.id = id;
        this.location = location;
        this.setupCost = setupCost;
        this.fixedFacility = fixedFacility;
        this.height = height;
        this.capacity = capacity;
        this.fiberReq = fiberReq;
        this.fiberSetupUnitCost = fiberSetupUnitCost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getSetupCost() {
        return setupCost;
    }

    public void setSetupCost(long setupCost) {
        this.setupCost = setupCost;
    }

    public boolean getFixedFacility() {
        return fixedFacility;
    }

    public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public void setFixedFacility(boolean fixedFacility) {
        this.fixedFacility = fixedFacility;
    }


    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getUsedCapacity() {
        return consumers.stream().mapToLong(Consumer::getDemand).sum();
    }

    public boolean isUsed() {
        return !consumers.isEmpty();
    }

    public boolean isFiberReq() {
		return fiberReq;
	}

	public void setFiberReq(boolean fiberReq) {
		this.fiberReq = fiberReq;
	}

	public long getFiberSetupUnitCost() {
		return fiberSetupUnitCost;
	}

	public void setFiberSetupUnitCost(long fiberSetupUnitCost) {
		this.fiberSetupUnitCost = fiberSetupUnitCost;
	}    

    public double getFiberPullDistance() {
		return fiberPullDistance;
	}

	public void setFiberPullDistance(double fiberPullDistance) {
		this.fiberPullDistance = fiberPullDistance;
	}	

    public long getFiberPullTotalCost() {
		return fiberPullTotalCost;
	}

	public void setFiberPullTotalCost(long fiberPullTotalCost) {
		this.fiberPullTotalCost = fiberPullTotalCost;
	}	
	
    @Override
    public String toString() {
        return "Facility " + id +
                " ($" + (setupCost + height)
                + ", " + capacity + " cap)";
    }
}
