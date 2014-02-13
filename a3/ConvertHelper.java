public class ConvertHelper {
	public static double convertFtoC(double temperature) {
		temperature = (temperature - 32) * 5 / 9;
		return temperature;
	}

	public static double convertMileToMeters(double altitude) {
		altitude = altitude * 0.3048;
		return altitude;
	}
}
