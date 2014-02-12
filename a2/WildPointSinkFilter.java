import java.util.*;						// This class is used to interpret time words
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.
public class WildPointSinkFilter extends FilterFramework{
	public void run()
    {
		/************************************************************************************
		*	TimeStamp is used to compute time using java.util's Calendar class.
		* 	TimeStampFormat is used to format the time value so that it can be easily printed
		*	to the terminal.
		*************************************************************************************/

		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");
		
		String fileName = "./WildPoint.dat";	// Input data file.
		int bytesread = 0;					// Number of bytes read from the input file.
		DataOutputStream out = null;			// File stream reference.
		
		/*************************************************************
		*	First we announce to the world that we are alive...
		**************************************************************/

		
		try
		{
			/***********************************************************************************
			*	Here we open the file and write a message to the terminal.
			***********************************************************************************/

			out = new DataOutputStream(new FileOutputStream(fileName));
			System.out.println("\n" + this.getName() + "::WildPointSink reading file..." );

			/***********************************************************************************
			*	Here we read the data from the file and send it out the filter's output port one
			* 	byte at a time. The loop stops when it encounters an EOFExecption.
			***********************************************************************************/

			while(true)
			{
				Record record = new Record();
				byte[] buffer = new byte[record.getRecordLength()];
				for(int i=0; i<=buffer.length-1; i++){
					buffer[i] = ReadFilterInputPort();
					bytesread++;
				}
				record.setBuffer(buffer);
				//
				out.writeBytes(TimeStampFormat.format(record.getTimeStamp().getTime()));
				out.writeBytes("\t");
				out.writeBytes(String.format("%08.5f", record.getPressure()).replace('.', ':'));
				out.writeBytes("\n");
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
