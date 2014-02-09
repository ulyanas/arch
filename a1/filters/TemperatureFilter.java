package filters;


import framework1.DataFormat;
import framework1.FilterFramework;

/******************************************************************************************************************
* File:TemperatureFilter.java
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

public class TemperatureFilter extends FilterFramework
{
	public void run()
    {

		int id = 0;
		long measurement = 0;
		double temperature;
		
		// Next we write a message to the terminal to let the world know we are alive...
		//System.out.print( "\n" + this.getName() + "::Temperature Reading ");
        try {
			while (true)
			{
				/*************************************************************
				*	Here we read a byte and write a byte
				*************************************************************/
		            id=getID();
		            measurement=getMeasurement();
		            
		            if (id == DataFormat.ID_TEMPERATURE) {
                        
                        temperature = Double.longBitsToDouble(measurement);
                        temperature = (5.0 / 9) * (temperature - 32);
                        measurement = Double.doubleToLongBits(temperature);
		            }    
		            putID(id);
                    putMeasurement(measurement);
		            //System.out.println(id);
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