package jfaerman.iot.poste;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/postewsk")
public class LightSocket {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public static Set<Session> sessions = new HashSet<Session>();

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected ... " + session.getId());
		sessions.add(session);
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		switch (message) {
		case "quit":
			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE,
						"Game ended"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			break;
		}
		return message;
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		sessions.remove(session);
		logger.info(String.format("Session %s closed because of %s",
				session.getId(), closeReason));
	}
}
