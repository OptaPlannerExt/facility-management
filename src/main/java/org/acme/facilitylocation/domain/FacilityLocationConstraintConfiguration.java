package org.acme.facilitylocation.domain;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

@ConstraintConfiguration
public class FacilityLocationConstraintConfiguration {

    static final String FACILITY_CAPACITY = "facility capacity";
    static final String FACILITY_SETUP_COST = "facility setup cost";
    static final String FACILITY_FIXED = "facility fixed";
    static final String DISTANCE_FROM_FACILITY = "distance from facility";
    static final String FIBER_REQUIRED = "new fiber provisioning required for facility";
    static final String FACILITY_HEIGHT = "facility height";
    
    

    @ConstraintWeight(FACILITY_FIXED)
    HardSoftLongScore facilityFixed = HardSoftLongScore.ofHard(10);
    
    @ConstraintWeight(FIBER_REQUIRED)
    HardSoftLongScore fiberRequired = HardSoftLongScore.ofSoft(1);

    @ConstraintWeight(FACILITY_HEIGHT)
    HardSoftLongScore facilityHeight = HardSoftLongScore.ofSoft(1);    

    @ConstraintWeight(FACILITY_CAPACITY)
    HardSoftLongScore facilityCapacity = HardSoftLongScore.ofHard(1);
    
    @ConstraintWeight(FACILITY_SETUP_COST)
    HardSoftLongScore facilitySetupCost = HardSoftLongScore.ofSoft(2);

    @ConstraintWeight(DISTANCE_FROM_FACILITY)
    HardSoftLongScore distanceFromFacility = HardSoftLongScore.ofSoft(5);
}
