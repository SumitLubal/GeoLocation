import java.awt.event.InvocationEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.opencsv.CSVWriter;

public class GeoLocator implements Runnable {
	ArrayList<GeoPoint> pointsInDatabase;

	public GeoLocator() {
		new Thread(this).start();
	}

	public static void main(String a[]) {
		new GeoLocator();
	}

	@Override
	public void run() {
		ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
		GeoPoint startpoints = new GeoPoint(0, 0);
		Scanner sc = new Scanner(System.in);
		double radius = 0;
		int count;
		//System.out.println(new GeoPoint(73.79765, 18.55503).distanceFrom(new GeoPoint(73.76555, 18.56906)));
		System.out.println(new GeoPoint(73.79765, 18.55503).isInRange(new GeoPoint(73.76555, 18.56906), 1));
		System.out.println("Enter start point long");
		startpoints.setLongitude(sc.nextDouble());
		System.out.println("Enter start point lat");
		startpoints.setLattitude(sc.nextDouble());
		System.out.println("Enter range (in KM)");
		radius = sc.nextDouble();
		System.out.println("Enter generation count");
		count = sc.nextInt();
		// generate data
		generateInRangeData(startpoints, radius, count, points, true);
		//generateInRangeData(startpoints, radius, count, points, false);
		System.out.println("here is list of gen values....");
		for (int i = 0; i < points.size(); i++) {
			//System.out.println("Long: " + points.get(i).getLongitude() + " lat: " + points.get(i).getLattitude());
		}
		// dump in csv
		try {
			dumpincsv(points, "points.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// find in range values
		while (true) {
			GeoPoint point = new GeoPoint(0, 0);
			double range = 0;
			System.out.println("Enter Long for reference point");
			point.setLongitude(sc.nextDouble());
			System.out.println("Enter Latt for reference point");
			point.setLattitude(sc.nextDouble());
			System.out.println("Enter Range to detect");
			range = sc.nextDouble();
			ArrayList<GeoPoint> pointsInRange = findPointsInRangeOf(points,point, range);
			System.out.println("Points in range are"+pointsInRange.size());
			for (int i = 0; i < pointsInRange.size(); i++) {
				//System.out.println("Long: " + pointsInRange.get(i).getLongitude() + " lat: " + pointsInRange.get(i).getLattitude());
			}
			try {
				dumpincsv(pointsInRange, "inrange.csv");
				System.out.println("Dumped success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private ArrayList<GeoPoint> findPointsInRangeOf(ArrayList<GeoPoint>points, GeoPoint point, double range) {
		ArrayList<GeoPoint> foundPoints = new ArrayList<GeoPoint>();
		for(int i=0;i<points.size();i++){
			if(point.isInRange(points.get(i), range)){
				foundPoints.add(points.get(i));
			}
		}
		return foundPoints;
	}

	private void dumpincsv(ArrayList<GeoPoint> points, String string) throws IOException {
		ArrayList<String[]> converted = new ArrayList<String[]>();
		CSVWriter writer = new CSVWriter(new FileWriter(string));
		for (int i = 0; i < points.size(); i++) {
			converted.add(("" + points.get(i).getLongitude() + "," + points.get(i).getLattitude()).split(","));
		}
		writer.writeAll(converted);
		writer.close();
	}

	private void generateInRangeData(GeoPoint startpoints, double radius, int count, ArrayList<GeoPoint> points,
			boolean inRange) {
		for (int i = 0; i < count; i++) {
			points.add(getRandomLocationWithinRange(startpoints, radius, inRange));
		}
	}

	public static GeoPoint getRandomLocationWithinRange(GeoPoint start, double radius, boolean isInRange) {
		Random random = new Random();
		double x0 = start.getLongitude();
		double y0 = start.getLattitude();
		// convert to meter
		radius = radius * 1000;
		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;
		double u = 0;
		double v = 0;
		if (isInRange) {
			u = random.nextDouble();
			v = random.nextDouble();
		} else {
			u = random.nextDouble() + radius;
			v = random.nextDouble() + radius;
		}
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(y0);

		double foundLongitude = new_x + x0;
		double foundLatitude = y + y0;
		System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude);
		return (new GeoPoint(foundLongitude, foundLatitude));
	}
}
