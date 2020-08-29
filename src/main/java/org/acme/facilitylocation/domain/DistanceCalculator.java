package org.acme.facilitylocation.domain;

import java.util.List;

public class DistanceCalculator {

    public static final double METERS_PER_DEGREE = 111_000;
	public List<Facility> FacilityDistanceCalculator(List<Facility> facilities, String units){
		
		double dist = new Double(0);
		
		for(Facility facility1: facilities) {
			if(facility1.isFiberReq()==true) {
				facility1.setFiberPullDistance(0);
				for(Facility facility2: facilities) {
					if(facility1.getId() != facility2.getId()) {
					dist = distance(facility1, facility2, units);
					if(facility1.getFiberPullDistance()== 0 || facility1.getFiberPullDistance() > dist) {
						facility1.setFiberPullDistance(dist);
					 }
					}
				}
			}
			facility1.setFiberPullTotalCost((long)Math.ceil(facility1.getFiberPullDistance()) * facility1.getFiberSetupUnitCost());
			
		}
		return facilities;
	}
	
	private static double distance(Facility facility1, Facility facility2, String unit) {
			double 	lat1 = facility1.getLocation().latitude;
			double lon1 = facility1.getLocation().longitude;
			double lat2 = facility2.getLocation().latitude;
			double lon2 = facility2.getLocation().longitude;
			
			if ((lat1 == lat2) && (lon1 == lon2)) {
				return 0;
			}
			else {
				double theta = lon1 - lon2;
				double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
				dist = Math.toDegrees(Math.acos(dist))  * 60 * 1.1515;
				if (unit.equals("K")) {
					dist = dist * 1.609344;
				} else if(unit.equals("m")) {
					dist = dist * 1.609344 * 1000;
				}else if (unit.equals("N")) {
					dist = dist * 0.8684;
				}
				return (dist);
			}
		}	
	
}








