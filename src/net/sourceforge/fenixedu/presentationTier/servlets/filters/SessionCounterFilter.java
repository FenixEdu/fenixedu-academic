/*
 * Created on Aug 27, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

/**
 * @author com.stardeveloper
 *  
 */
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounterFilter implements HttpSessionListener {

	private static int activeSessions = 0;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		activeSessions++;
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		naturalDecrement(activeSessions);
	}

	public static int getActiveSessions() {
		return activeSessions;
	}

	private void naturalDecrement(int var) {
		if (var > 0) {
			var--;
		}
	}
}