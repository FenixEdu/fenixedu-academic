package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;

public class ProjectRequestUtil {

    public static BackendInstance getInstance(final HttpServletRequest request) {
	final String parameter = request.getParameter("backendInstance");
	return parameter == null || parameter.isEmpty() ? BackendInstance.IST : BackendInstance.valueOf(parameter);
    }

}
