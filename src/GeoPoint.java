class GeoPoint {
		private static final double EARTH_RADIUS = 6371;
		private double longitude;
		private double lattitude;

		public GeoPoint(double foundLongitude, double foundLatitude) {
			this.longitude = foundLongitude;
			this.lattitude = foundLatitude;
		}
		public double distanceFrom(GeoPoint startP){
			return distanceInKm(startP,this);
		}
		private double distanceInKm(GeoPoint startP,GeoPoint endP) {
			double lat1 = startP.getLattitude() / 1E6;
			double lat2 = endP.getLattitude() / 1E6;
			double lon1 = startP.getLongitude() / 1E6;
			double lon2 = endP.getLongitude() / 1E6;
			double dLat = Math.toRadians(lat2 - lat1);
			double dLon = Math.toRadians(lon2 - lon1);
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			System.out.println("distance" + EARTH_RADIUS * c *1000000);
			return EARTH_RADIUS * c * 1000000;
		}
		public boolean isInRange(GeoPoint point,double rangeInKm){
			double i=0;
			i=(double)distanceInKm(point,this);
			if(i<=rangeInKm){
				return true;
			}else{
				return false;
			}
		}
		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLattitude() {
			return lattitude;
		}

		public void setLattitude(double lattitude) {
			this.lattitude = lattitude;
		}
		
	}
