import java.util.ArrayList;
import java.util.Calendar;

public class MergeFilter extends FilterFramework {
	private int inputPortNum;
	private int outputPortNum;

	public MergeFilter() {
		super(2, 1);
		this.inputPortNum = 2;
		this.outputPortNum = 1;
	}

	public MergeFilter(int inputPortNum, int outputPortNum) {
		super(inputPortNum, outputPortNum);
		this.inputPortNum = inputPortNum;
		this.outputPortNum = outputPortNum;
	}

	public void run() {
		int bytesread = 0;
		// List indicating whether current port is opened
		ArrayList<Boolean> portIsOpen = new ArrayList<Boolean>();
		// List of data inside the port
		ArrayList<Record> portRecords = new ArrayList<Record>();
		for (int i = 0; i < this.inputPortNum; i++) {
			portIsOpen.add(true); // Initially all ports are open
			portRecords.add(null); // There is no data inside them
		}
		// Indicates whether there are still any ports
		boolean haveOpenPorts = true;

		while (haveOpenPorts) {
			// We will set haveOpenPorts to true if any port is open
			haveOpenPorts = false;
			Record minRecord = null;
			int minPortNum = -1;

			// Find port, where data has minimal time stamp
			for (int portNum = 0; portNum < this.inputPortNum; portNum++) {
				if (portIsOpen.get(portNum)) {
					haveOpenPorts = true;
					if (portRecords.get(portNum) == null) {
						try {
							portRecords.set(portNum, new Record());
							byte[] portBuffer = new byte[portRecords.get(
									portNum).getRecordLength()];
							for (int i = 0; i <= portBuffer.length - 1; i++) {
								portBuffer[i] = ReadFilterInputPort(portNum);
								bytesread++;
							}
							portRecords.get(portNum).setBuffer(portBuffer);
							if (minRecord != null
									&& portRecords.get(portNum).getTimeStamp()
											.equals(minRecord.getTimeStamp())) {
								portRecords.set(portNum, null);
							}
						} // try
						catch (EndOfStreamException e) {
							portRecords.set(portNum, null);
							portIsOpen.set(portNum, false);
						} // catch
					} // if

					// If we could read the record
					if (portRecords.get(portNum) != null) {
						// Compare time stamp of the current port with minimum
						// time stamp
						if (minRecord == null
								|| portRecords.get(portNum).getTimeStamp()
										.before(minRecord.getTimeStamp())) {
							minRecord = portRecords.get(portNum);
							minPortNum = portNum;
						} // if
					} // if

				} // if
			} // for

			if (minRecord != null) {
				portRecords.set(minPortNum, null);

				byte[] bytes = minRecord.generateByteSequence();
				for (int i = 0; i <= bytes.length - 1; i++) {
					WriteFilterOutputPort(bytes[i], 0);
				} // for
			} // if
		} // while

		// If there are no open ports - we will stop working and close all
		// ports.
		ClosePorts();
		System.out.print("\n" + this.getName()
				+ "::Filter Exiting; bytes read: " + bytesread);
	}
}
