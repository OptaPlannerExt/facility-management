package org.acme.facilitylocation.domain;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sumLong;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class FacilityLocationConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
        		fixedFacility(constraintFactory),
        		facilityFiberReq(constraintFactory),
        		facilityHeight(constraintFactory),
                facilityCapacity(constraintFactory),
                setupCost(constraintFactory),
                distanceFromFacility(constraintFactory)
        };
    }


    Constraint facilityFiberReq(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Consumer.class)
                .filter((facility) -> facility.getFacility().isFiberReq() == true)
                .penalizeConfigurableLong(
                        FacilityLocationConstraintConfiguration.FIBER_REQUIRED,
                        (facility) -> 5);
    }     

    
    Constraint facilityHeight(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Consumer.class)
                .filter((facility) ->  facility.getFacility().getHeight() >= 80)
                .rewardConfigurableLong(
                        FacilityLocationConstraintConfiguration.FACILITY_HEIGHT,
                        (facility)-> facility.getFacility().getSetupCost());
    } 
    
    
    Constraint fixedFacility(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Consumer.class)
        		.groupBy(Consumer::getFacility)
        		.filter((facility) -> facility.getFixedFacility() == true)
                .rewardConfigurableLong(
                		FacilityLocationConstraintConfiguration.FACILITY_FIXED, 
                		Facility::getSetupCost);
    }
    
    Constraint facilityCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Consumer.class)
                .groupBy(Consumer::getFacility, sumLong(Consumer::getDemand))
                .filter((facility, demand) -> demand > facility.getCapacity())
                .penalizeConfigurableLong(
                        FacilityLocationConstraintConfiguration.FACILITY_CAPACITY,
                        (facility, demand) -> demand - facility.getCapacity());
    }

    Constraint setupCost(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Consumer.class)
                .groupBy(Consumer::getFacility)
                .penalizeConfigurableLong(
                        FacilityLocationConstraintConfiguration.FACILITY_SETUP_COST,
                        Facility::getSetupCost);
    }

    Constraint distanceFromFacility(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Consumer.class)
                .filter(Consumer::isAssigned)
                .penalizeConfigurableLong(
                        FacilityLocationConstraintConfiguration.DISTANCE_FROM_FACILITY,
                        Consumer::distanceFromFacility);
    }   
    
}
