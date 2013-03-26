package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;

public class ProjectRequestUtil {

    public static BackendInstance getInstance(final HttpServletRequest request) {
        String parameter = request.getParameter("backendInstance");
        if (parameter == null || parameter.isEmpty()) {
            parameter = (String) request.getAttribute("backendInstance");
        }
        if (parameter == null || parameter.isEmpty()) {
            throw new Error("project.instance.backend.not.specified");
        }
        return BackendInstance.valueOf(parameter);
    }

}
