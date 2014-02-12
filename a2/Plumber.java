
public class Plumber {
	   public static void main( String argv[])
	   {
			/****************************************************************************
			* Here we instantiate three filters.
			****************************************************************************/

			SourceFilter sourceFilter = new SourceFilter();
			MileFilter mileFilter = new MileFilter();
			TemperatureFilter temperatureFilter = new TemperatureFilter();
			PressureFilter pressureFilter = new PressureFilter(2);
			NormalSinkFilter normalSinkFilter = new NormalSinkFilter();
			WildPointSinkFilter wildPointSinkFilter = new WildPointSinkFilter();
			
			/****************************************************************************
			* Here we connect the filters starting with the sink filter (Filter 1) which
			* we connect to Filter2 the middle filter. Then we connect Filter2 to the
			* source filter (Filter3).
			****************************************************************************/

			mileFilter.Connect(sourceFilter); 
			temperatureFilter.Connect(mileFilter);
			pressureFilter.Connect(temperatureFilter);
			normalSinkFilter.Connect(pressureFilter, 0);
			wildPointSinkFilter.Connect(pressureFilter, 1);
			

			/****************************************************************************
			* Here we start the filters up. All-in-all,... its really kind of boring.
			****************************************************************************/

			sourceFilter.start();
			temperatureFilter.start();
			mileFilter.start();
			pressureFilter.start();
			normalSinkFilter.start();
			wildPointSinkFilter.start();

	   } // main
}
