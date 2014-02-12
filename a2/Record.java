import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;


public class Record {
	private int measurementLength = 8;		// This is the length of all measurements (including time) in bytes
	private int idLength = 4;				// This is the length of IDs in the byte stream
	private int recordLength = 72;
	private int numOfMeasurement = 6;
	
	private byte[] buffer;
	private Calendar TimeStamp = Calendar.getInstance();
	private double velocity;
	private double altitude;
	private double pressure;
	private double temperature;
	private double attitude;
	private boolean hasExtra;
	private double extra;
	
	
	class RecordLengthNotMatchException extends Exception {
		
		static final long serialVersionUID = 1; // the version for serializing

		RecordLengthNotMatchException () { super(); }

		RecordLengthNotMatchException(String s) { super(s); }
		

	} // class
	
	public Record(){
	}
	public Record(byte[] buffer){
		this.buffer = buffer; 
		convert();
	}
	public Record(byte[] buffer, boolean hasExtra){
		this.buffer = buffer; 
		this.hasExtra = hasExtra;
		convert();
	}
	public void setBuffer(byte[] buffer){
		this.buffer = buffer; 
		convert();		
	}
	public int getRecordLength(){
		return recordLength;
	}
	public double getVelocity() {
		return velocity;
	}
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getAttitude() {
		return attitude;
	}
	public void setAttitude(double attitude) {
		this.attitude = attitude;
	}
	public Calendar getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(Calendar timeStamp) {
		TimeStamp = timeStamp;
	}
	public boolean hasExtra() {
		return hasExtra;
	}
	public double getExtra(){
		return extra;
	}
	public void setHasExtra(boolean hasExtra){
		this.hasExtra = hasExtra;
		numOfMeasurement++;
		recordLength = recordLength + idLength + measurementLength;
		return;
	}
	public void setExtra(double extra) {
		if(!hasExtra){
			hasExtra = true;
			numOfMeasurement++;
			recordLength = recordLength + idLength + measurementLength;
		}
		this.extra = extra;
	}
	public byte[] generateByteSequence(){
		ArrayList<Byte> list = new ArrayList<Byte>();
		for(int i=0; i<=numOfMeasurement-1; i++){
			byte[] idBytes= new byte[4];
			java.nio.ByteBuffer.wrap(idBytes).putInt(i);
			for(int j=0; j<=idBytes.length-1; j++) list.add(idBytes[j]);
			
			byte[] measurementBytes = new byte[8];
			if(i == 0) java.nio.ByteBuffer.wrap(measurementBytes).putLong(TimeStamp.getTimeInMillis());
			else if(i == 1) java.nio.ByteBuffer.wrap(measurementBytes).putDouble(velocity);
			else if(i == 2) java.nio.ByteBuffer.wrap(measurementBytes).putDouble(altitude);
			else if(i == 3) java.nio.ByteBuffer.wrap(measurementBytes).putDouble(pressure);
			else if(i == 4) java.nio.ByteBuffer.wrap(measurementBytes).putDouble(temperature);
			else if(i == 5) java.nio.ByteBuffer.wrap(measurementBytes).putDouble(attitude);
			else if(i == 6) java.nio.ByteBuffer.wrap(measurementBytes).putDouble(extra);
			for(int j=0; j<=measurementBytes.length-1; j++) list.add(measurementBytes[j]);
		}		
		byte[] bytes = new byte[recordLength];
		for(int i=0; i<=bytes.length-1; i++) bytes[i] = list.get(i);
		return bytes;
	}
	
	private void convert(){
//		if(record.length != recordLength) throw new RecordLengthNotMatchException("Record Length Not Match");
		int index = 0;
		for(int i=0; i<=numOfMeasurement-1; i++){
			int id = getID(index);
			index += idLength;
			long measurement = getMeasurement(index);
			index += measurementLength;
			
			if(id == 0) TimeStamp.setTimeInMillis(measurement);
			else if(id == 1) velocity = Double.longBitsToDouble(measurement);
			else if(id == 2) altitude = Double.longBitsToDouble(measurement);
			else if(id == 3) pressure = Double.longBitsToDouble(measurement);
			else if(id == 4) temperature = Double.longBitsToDouble(measurement);
			else if(id == 5) attitude = Double.longBitsToDouble(measurement);
			else if(id == 6) extra = Double.longBitsToDouble(measurement);
		}
		return;
	}
	
	private int getID(int start){
		int id = 0;
		for (int i=start; i<start+idLength; i++ )
		{
			byte databyte = buffer[i];	// This is where we read the byte from the stream...
			id = id | (databyte & 0xFF);		// We append the byte on to ID...
			if (i != start+idLength-1)				// If this is not the last byte, then slide the
			{									// previously appended byte to the left by one byte
				id = id << 8;					// to make room for the next byte we append to the ID
			}
		}
		return id;
	}
	
	private long getMeasurement(int start){
		long measurement = 0;
		for (int i=start; i<start+measurementLength; i++ ){
			byte databyte = buffer[i];
			measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...
			if (i != start+measurementLength-1)					// If this is not the last byte, then slide the
			{												// previously appended byte to the left by one byte
				measurement = measurement << 8;				// to make room for the next byte we append to the											// measurement
			} // if
		} // if
		return measurement;
	}
}
