import java.util.*;						// This class is used to interpret time words
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

public class NormalSinkFilter extends FilterFramework{
	public void run()
    {
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");
		
		int bytesread = 0;				// This is the number of bytes read from the stream
		String fileName = "./NormalPoint.dat";	// Output data file.
		DataOutputStream out = null;			// File stream reference.
		
		/*************************************************************
		*	First we announce to the world that we are alive...
		**************************************************************/ 
		System.out.println("\n" + this.getName() + "::NormalPointSink reading file..." );
		try
		{
			/***********************************************************************************
			*	Here we open the file and write a message to the terminal.
			***********************************************************************************/

			out = new DataOutputStream(new FileOutputStream(fileName));

			/***********************************************************************************
			*	Here we read the data from the file and send it out the filter's output port one
			* 	byte at a time. The loop stops when it encounters an EOFExecption.
			***********************************************************************************/

			while(true)
			{
				Record record = new Record();
				record.setHasExtra(true);
				byte[] buffer = new byte[record.getRecordLength()];
				for(int i=0; i<=buffer.length-1; i++){
					buffer[i] = ReadFilterInputPort();
					bytesread++;
				}
				record.setBuffer(buffer);
				// time
				out.writeBytes(TimeStampFormat.format(record.getTimeStamp().getTime()));
				out.writeBytes( "\t" );
				// temperature
				out.writeBytes(String.format("%09.5f", record.getTemperature()));
				out.writeBytes( "\t" );
				// altitude
				out.writeBytes(String.format("%012.5f", record.getAltitude()));
				out.writeBytes( "\t" );
				// pressure
				out.writeBytes(String.format("%08.5f", record.getPressure()).replace('.', ':'));
				if(record.getExtra() > 0) out.writeBytes("*");
				out.writeBytes( "\t" );			
				out.writeBytes( "\n" );
			} // while

		} //try

		/***********************************************************************************
		*	The following exception is raised when we hit the end of input file. Once we
		* 	reach this point, we close the input file, close the filter ports and exit.
		***********************************************************************************/

		catch (EndOfStreamException e)
		{
			System.out.print("\n" + this.getName() + "::End of file reached..." );
			try
			{
				out.close();
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Write file complete");

			}
		/***********************************************************************************
		*	The following exception is raised should we have a problem closing the file.
		***********************************************************************************/
			catch (Exception closeerr)
			{
				System.out.print("\n" + this.getName() + "::Problem closing input data file::" + closeerr);

			} // catch

		} // catch

		/***********************************************************************************
		*	The following exception is raised should we have a problem openinging the file.
		***********************************************************************************/

		catch ( IOException iox )
		{
			System.out.print("\n" + this.getName() + "::Problem reading input data file::" + iox );

		} // catch	
		

   } // run
}
