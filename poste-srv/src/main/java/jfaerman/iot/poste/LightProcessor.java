package jfaerman.iot.poste;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

import javax.websocket.Session;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;

public class LightProcessor implements IRecordProcessor {
	private final CharsetDecoder decoder = Charset.forName("UTF-8")
			.newDecoder();

	@Override
	public void initialize(String arg0) {}

	@Override
	public void processRecords(List<Record> records,
			IRecordProcessorCheckpointer check) {
		try {
			for (Record record : records) {
				String data = decoder.decode(record.getData()).toString();
				for (Session session : LightSocket.sessions) {
					session.getBasicRemote().sendText(data);
				}
			}
			check.checkpoint();
		} catch (Exception e) {
			System.out.println("OOOPS!");
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown(IRecordProcessorCheckpointer arg0, ShutdownReason arg1) {}

}
