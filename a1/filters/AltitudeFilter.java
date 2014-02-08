package filters;


import framework.DataFormat;
import framework.FilterFramework;

/******************************************************************************************************************
* File:AltitudeFilter.java
* Course: 17655
* Project: Assignment 1
*
* Description:
*
* Parameters: 		None
* 
* Author: Ulyana Skl
*
* Internal Methods: 
* getID()
* getMeasurement()
* putID(int)
* putMeasurement(long)
*
******************************************************************************************************************/

public class AltitudeFilter extends FilterFramework
{
	public void run()
    {

		int id = 0;
		long measurement = 0;
		double altitude;
        // number of bytes read from the stream
        int bytesread = 0; 
        //  number of bytes written to the stream
        int byteswrite = 0;
		
		// Next we write a message to the terminal to let the world know we are alive...
		//System.out.print( "\n" + this.getName() + "::Altitude Reading ");
        try {
			while (true)
			{
				id = getID();
                bytesread += 4;
                measurement = getMeasurement();
                bytesread += 8;

                if (id == DataFormat.ID_ALTITUDE) {
                        // convert 8-bytes variable to double
                        altitude = Double.longBitsToDouble(measurement);
                        // convert feet to meters
                        altitude = 0.3048 * altitude;
                        // convert double to 8-bytes variable
                        measurement = Double.doubleToLongBits(altitude);
                } 

                putID(id);
                byteswrite += 4;
                putMeasurement(measurement);
                byteswrite += 8;
			} 
        }
        catch (EndOfStreamException e){
        	
        }
   } 
	
private int getID() throws EndOfStreamException {
	int id = 0;
	int i;
	byte databyte;
	for (i=0; i<DataFormat.ID_LENGTH; i++ )
	{
		databyte = ReadFilterInputPort();	
        //System.out.println(String.format("ID %x", databyte));
		id = id | (databyte & 0xFF);		

		if (i != DataFormat.ID_LENGTH-1)			
		{										
			id = id << 8;				
		} 
	}  
	return id;
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

private void putID(int id) {
    byte databyte;
    
    for (int i = DataFormat.ID_LENGTH; i > 0; --i) {

            databyte = (byte) (id >> ((i -1)*8) & 0xFF);
            WriteFilterOutputPort(databyte);
    }	
}

private void putMeasurement(long measurement) {
    
    byte databyte;
    
    for (int i = DataFormat.MEASUREMENT_LENGTH; i > 0; --i) {

            databyte = (byte) (measurement >> ((i-1)*8) & 0xFF);

            WriteFilterOutputPort(databyte);
    }
}

} 