package jfaerman.iot.poste;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StreamWebListener implements ServletContextListener {
	Thread worker = new Thread(new LightWorker()); 
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("DESTROY");		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Context INIT");
		worker.start();
	}
	
}
