package net.sourceforge.fenixedu.presentationTier.servlets.ajax;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DeployNotifier;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class DeployNotifierServlet extends HttpServlet {

    private static DeployNotifier deployNotifier = null;
    private static ResourceBundle resourceBundle = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.setContentType("text/html");
	if (getDeployNotifier().getNotifyUsers()) {
	    StringBuffer notifyString = new StringBuffer("");
	    String responseString = null;
	    Integer estimateMinutesForDeploy = getDeployNotifier().getEstimateMinutesForDeploy();
	    notifyString.append("<div class=\"deploywarning\">");

	    if (estimateMinutesForDeploy > 0) {
		notifyString.append(getResourceBundle().getString("label.deploy.warning"));
		notifyString.append("</div>");
		MessageFormat messageFormat = new MessageFormat(notifyString.toString());
		responseString = messageFormat.format(new Object[] { estimateMinutesForDeploy, estimateMinutesForDeploy + 1 });
	    } else {
		notifyString.append(getResourceBundle().getString("label.deploy.warning.moment"));
		notifyString.append("</div>");
		responseString = notifyString.toString();
	    }
	    response.getWriter().write(responseString);
	}
    }

    private DeployNotifier getDeployNotifier() {
	if (deployNotifier == null) {
	    deployNotifier = RootDomainObject.getInstance().getDeployNotifier();
	}
	return deployNotifier;
    }

    private ResourceBundle getResourceBundle() {
	if (resourceBundle == null) {
	    resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
	}
	return resourceBundle;
    }
}
