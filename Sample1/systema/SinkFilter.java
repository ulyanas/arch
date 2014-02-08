package systema;
/******************************************************************************************************************
* File:SinkFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This particular
* filter reads some input from the filter's input port and does the following:
*
*	1) It parses the input stream and "decommutates" the temperature ID
*	2) It parses the input steam for measurments and "decommutates" temperatures, storing the bits in a long word.
*
* This filter illustrates how to convert the byte stream data from the upstream filterinto useable data found in
* the stream: namely time (long type) and temperatures (double type).
*
*
* Parameters: 	None
*
* Internal Methods: None
*
******************************************************************************************************************/
import java.util.*;	
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

import framework.DataFormat;
import framework.FilterFramework;


public class SinkFilter extends FilterFramework
{
	public void run()
    {
		/************************************************************************************
		*	TimeStamp is used to compute time using java.util's Calendar class.
		* 	TimeStampFormat is used to format the time value so that it can be easily printed
		*	to the terminal.
		*************************************************************************************/

		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:DD:HH:mm:ss");

		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream

		long measurement = 0;
		 
		long timestamp = 0;
        boolean flagTime = false;
        double altitude = 0;
        boolean flagAltitude = false;
        double temperature = 0;
        boolean flagTemperature = false;
		 
		int id;					

		/*************************************************************
		*	First we announce to the world that we are alive...
		**************************************************************/

		//System.out.print( "\n" + this.getName() + "::Sink Reading\n");
		System.out.print("Time: \t\t\t Temperature (C): \t Altitude (m):\n");
		while (true)
		{
			try
			{
				/***************************************************************************
				// We know that the first data coming to this filter is going to be an ID and
				// that it is DataFormat.ID_LENGTH long. So we first decommutate the ID bytes.
				****************************************************************************/

				  if (flagTime && flagAltitude && flagTemperature) {
               
                      TimeStamp.setTimeInMillis(timestamp);
                      System.out.printf("%s\t", TimeStampFormat.format(TimeStamp.getTime()));
                      flagTime = false;

                      System.out.printf("%9.5f\t", temperature);

                      flagTemperature = false;

                      System.out.printf("%5.5f\t", altitude);
                      flagAltitude = false;

                      System.out.printf("\n");
              }

              while (!(flagTime && flagAltitude && flagTemperature)) {
                      id = getID();
                      measurement = getMeasurement();
                      switch (id) {
                      case DataFormat.ID_TIME:
                              timestamp = measurement;
                              flagTime = true;
                              break;
                      case DataFormat.ID_VELOCITY:
                              Double.longBitsToDouble(measurement);
                              break;
                      case DataFormat.ID_ALTITUDE:
                              altitude = Double.longBitsToDouble(measurement);
                              flagAltitude = true;
                              break;
                      case DataFormat.ID_TEMPERATURE:
                              temperature = Double.longBitsToDouble(measurement);
                              flagTemperature = true;
                              break;
                      case DataFormat.ID_PRESSURE:
                              Double.longBitsToDouble(measurement);
                              break;
                      case DataFormat.ID_ATTITUDE:
                              Double.longBitsToDouble(measurement);
                      default:
                              break;
                      }
              }
			}
			/*******************************************************************************
			*	The EndOfStreamExeception below is thrown when you reach end of the input
			*	stream (duh). At this point, the filter ports are closed and a message is
			*	written letting the user know what is going on.
			********************************************************************************/

			catch (EndOfStreamException e)
			{
				ClosePorts();
				//System.out.print( "\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread );		
				break;
			} 
		} 
   } 
	
	private long getMeasurement() throws EndOfStreamException {
		long measurement = 0;
		int i;
		byte databyte;
		for (i=0; i<DataFormat.MEASUREMENT_LENGTH; i++ )
		{
			databyte = ReadFilterInputPort();	
			measurement = measurement | (databyte & 0xFF);		

			if (i != DataFormat.MEASUREMENT_LENGTH-1)			
			{										
				 measurement = measurement << 8;			
			} 
		}     
	    
		return measurement;
	}
	
	private int getID() throws EndOfStreamException {
		int id = 0;
		int i;
		byte databyte;
		for (i=0; i<DataFormat.ID_LENGTH; i++ )
		{
			databyte = ReadFilterInputPort();	
			id = id | (databyte & 0xFF);		
			if (i != DataFormat.ID_LENGTH-1)			
			{										
				id = id << 8;				
			} 
		}  
		return id;
	}

} 