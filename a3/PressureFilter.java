import java.util.ArrayList;

public class PressureFilter extends FilterFramework {
	public PressureFilter() {

	}

	public PressureFilter(int outPutPortNum) {
		super(outPutPortNum);
	}

	public void run() {
		int bytesread = 0;
		Record lastValidRecord = null;
		ArrayList<Record> wildRecordList = new ArrayList<Record>();
		while (true) {
			try {
				Record record = new Record();
				byte[] buffer = new byte[record.getRecordLength()];
				for (int i = 0; i <= buffer.length - 1; i++) {
					buffer[i] = ReadFilterInputPort();
					bytesread++;
				}
				record.setBuffer(buffer);
				// Wild Point
				if (record.getPressure() < 0.0
						|| (lastValidRecord != null && Math.abs(record
								.getPressure() - lastValidRecord.getPressure()) > 10)) {
					wildRecordList.add(record);
					byte[] bytes = record.generateByteSequence();
					for (int i = 0; i <= bytes.length - 1; i++)
						WriteFilterOutputPort(bytes[i], 1); // write to wild
															// point sink
				}
				// Normal Point
				else {
					// revise wild point
					if (wildRecordList.size() > 0)
						reviseWildPointList(wildRecordList, lastValidRecord,
								record);
					// set not wild point
					record.setExtra(0);
					byte[] bytes = record.generateByteSequence();
					for (int i = 0; i <= bytes.length - 1; i++)
						WriteFilterOutputPort(bytes[i], 0); // write to normal
															// point sink
					// clear wildRecordList and set lastValidRecord
					lastValidRecord = record;
					wildRecordList.clear();
				}

			} // try
			catch (EndOfStreamException e) {
				// revise wild point
				if (wildRecordList.size() > 0)
					reviseWildPointList(wildRecordList, lastValidRecord, null);
				ClosePorts();
				System.out.print("\n" + this.getName()
						+ "::Filter Exiting; bytes read: " + bytesread);
				break;

			} // catch

		} // while

	}

	// Revise wild point
	private void reviseWildPointList(ArrayList<Record> wildRecordList,
			Record lastValidRecord, Record nextValidRecord) {
		double lastValidPressure = lastValidRecord == null ? 0
				: nextValidRecord.getPressure();
		double nextValidPressure = nextValidRecord == null ? 0
				: lastValidRecord.getPressure();
		double validPressure = (lastValidPressure + nextValidPressure) / 2;
		for (Record wildRecord : wildRecordList) {
			wildRecord.setPressure(validPressure);
			wildRecord.setExtra(1); // set wild point
			byte[] bytes = wildRecord.generateByteSequence();
			for (int i = 0; i <= bytes.length - 1; i++)
				WriteFilterOutputPort(bytes[i], 0);// write to normal point sink
		}
	}
}
