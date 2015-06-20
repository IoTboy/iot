package jfaerman.iot.poste;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;

public class LightFactory implements IRecordProcessorFactory{

	public IRecordProcessor createProcessor() {
		return new LightProcessor();
	}

}
