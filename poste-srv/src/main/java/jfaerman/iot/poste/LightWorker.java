package jfaerman.iot.poste;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

public class LightWorker implements Runnable {
	public void run() {
		String app_name = "poste";
		String stream_name = "poste-stream";
		String worker_name = "local-worker";
		KinesisClientLibConfiguration kinesisClientLibConfiguration =
                new KinesisClientLibConfiguration(app_name,
                        stream_name,
                        new DefaultAWSCredentialsProviderChain(),
                        worker_name);
        kinesisClientLibConfiguration.withInitialPositionInStream(InitialPositionInStream.LATEST);

        IRecordProcessorFactory recordProcessorFactory = new LightFactory();
        Worker worker = new Worker(recordProcessorFactory, kinesisClientLibConfiguration);

        System.out.printf("Running %s to process stream %s as worker %s...\n",
                app_name,
                stream_name,
                worker_name);

        int exitCode = 0;
        try {
            worker.run();
        } catch (Throwable t) {
            System.err.println("Caught throwable while processing data.");
            t.printStackTrace();
            exitCode = 1;
        }
        System.exit(exitCode);
	}
}
