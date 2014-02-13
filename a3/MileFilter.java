public class MileFilter extends FilterFramework {
	public void run() {
		int bytesread = 0;
		while (true) {
			try {
				Record record = new Record();
				byte[] buffer = new byte[record.getRecordLength()];
				for (int i = 0; i <= buffer.length - 1; i++) {
					buffer[i] = ReadFilterInputPort();
					bytesread++;
				}
				record.setBuffer(buffer);
				record.setAltitude(ConvertHelper.convertMileToMeters(record
						.getAltitude()));
				byte[] bytes = record.generateByteSequence();
				for (int i = 0; i <= bytes.length - 1; i++)
					WriteFilterOutputPort(bytes[i]);

			} // try
			catch (EndOfStreamException e) {
				ClosePorts();
				System.out.print("\n" + this.getName()
						+ "::Filter Exiting; bytes read: " + bytesread);
				break;

			} // catch

		} // while

	} // run
}
