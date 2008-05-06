package net.sourceforge.fenixedu.presentationTier.servlets;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;

public class ActionServletWrapper extends ActionServlet {

    private static class ServletConfigWrapper implements ServletConfig {

	private final ServletConfig servletConfig;

	public static final Map<String, String> parameterMap = new HashMap<String, String>();
	static {
	    parameterMap.put("config", "/WEB-INF/conf/struts-default.xml");
	    parameterMap.put("application", "resources.ApplicationResources");
	    parameterMap.put("config/resourceAllocationManager", "/WEB-INF/conf/struts-resourceAllocationManager.xml");
	    parameterMap.put("config/resourceManager", "/WEB-INF/conf/struts-resourceManager.xml");
	    parameterMap.put("config/student", "/WEB-INF/conf/struts-student.xml");
	    parameterMap.put("config/publico", "/WEB-INF/conf/struts-publico.xml");
	    parameterMap.put("config/candidate", "/WEB-INF/conf/struts-candidate.xml");
	    parameterMap.put("config/candidato", "/WEB-INF/conf/struts-masterDegreeCandidate.xml");        
	    parameterMap.put("config/posGraduacao", "/WEB-INF/conf/struts-masterDegreeAdministrativeOffice.xml");
	    parameterMap.put("config/teacher", "/WEB-INF/conf/struts-teacher.xml");
	    parameterMap.put("config/treasury", "/WEB-INF/conf/struts-treasury.xml");
	    parameterMap.put("config/coordinator", "/WEB-INF/conf/struts-coordinator.xml");
	    parameterMap.put("config/person", "/WEB-INF/conf/struts-person.xml");
	    parameterMap.put("config/person/functionalities", "/WEB-INF/conf/struts-functionalities.xml");        
	    parameterMap.put("config/employee", "/WEB-INF/conf/struts-employee.xml");
	    parameterMap.put("config/assiduousness", "/WEB-INF/conf/struts-assiduousness.xml");
	    parameterMap.put("config/manager", "/WEB-INF/conf/struts-manager.xml");
	    parameterMap.put("config/manager/functionalities", "/WEB-INF/conf/struts-functionalities.xml");
	    parameterMap.put("config/degreeAdministrativeOffice", "/WEB-INF/conf/struts-degreeAdministrativeOffice.xml");
	    parameterMap.put("config/credits", "/WEB-INF/conf/struts-credits.xml");
	    parameterMap.put("config/scientificCouncil", "/WEB-INF/conf/struts-scientificCouncil.xml");
	    parameterMap.put("config/operator", "/WEB-INF/conf/struts-operator.xml");
	    parameterMap.put("config/webSiteManager", "/WEB-INF/conf/struts-webSiteManager.xml");
	    parameterMap.put("config/facultyAdmOffice", "/WEB-INF/conf/struts-facultyAdmOffice.xml");
	    parameterMap.put("config/departmentAdmOffice", "/WEB-INF/conf/struts-departmentAdmOffice.xml");
	    parameterMap.put("config/gep", "/WEB-INF/conf/struts-gep.xml");
	    parameterMap.put("config/directiveCouncil", "/WEB-INF/conf/struts-directiveCouncil.xml");
	    parameterMap.put("config/delegate", "/WEB-INF/conf/struts-delegate.xml");
	    parameterMap.put("config/personnelSection", "/WEB-INF/conf/struts-personnelSection.xml");
	    parameterMap.put("config/projectsManagement", "/WEB-INF/conf/struts-projectsManagement.xml");
	    parameterMap.put("config/institucionalProjectsManagement", "/WEB-INF/conf/struts-projectsManagement.xml");
	    parameterMap.put("config/external", "/WEB-INF/conf/struts-external.xml");        
	    parameterMap.put("config/departmentMember", "/WEB-INF/conf/struts-departmentMember.xml");     
	    parameterMap.put("config/bolonhaManager", "/WEB-INF/conf/struts-bolonhaManager.xml");
	    parameterMap.put("config/SpaceManager", "/WEB-INF/conf/struts-spaceManager.xml");
	    parameterMap.put("config/researcher", "/WEB-INF/conf/struts-researcher.xml");
	    parameterMap.put("config/examCoordination", "/WEB-INF/conf/struts-examCoordinator.xml");
	    parameterMap.put("config/pedagogicalCouncil", "/WEB-INF/conf/struts-pedagogicalCouncil.xml");
	    parameterMap.put("config/alumni", "/WEB-INF/conf/struts-alumni.xml");
	    parameterMap.put("config/messaging", "/WEB-INF/conf/struts-messaging.xml");
	    parameterMap.put("config/parkingManager", "/WEB-INF/conf/struts-parkingManager.xml");  
	    parameterMap.put("config/academicAdminOffice", "/WEB-INF/conf/struts-academicAdminOffice.xml");         
	    parameterMap.put("config/library", "/WEB-INF/conf/struts-library.xml");   
	    parameterMap.put("config/internationalRelatOffice","/WEB-INF/conf/struts-internationalRelatOffice.xml");
	    parameterMap.put("config/identificationCardManager","/WEB-INF/conf/struts-identificationCardManager.xml");
	    parameterMap.put("config/publicRelations","/WEB-INF/conf/struts-publicRelations.xml");
	    parameterMap.put("debug", "3");
	    parameterMap.put("detail", "3");
	    parameterMap.put("validating", "true");
	}

	public ServletConfigWrapper(final ServletConfig servletConfig) {
	    this.servletConfig = servletConfig;
	}

	public String getInitParameter(final String name) {
	    final String parameter = parameterMap.get(name);
	    return parameter == null ? servletConfig == null ? null : servletConfig.getInitParameter(name) : parameter;
	}

	public Enumeration getInitParameterNames() {
	    return new Enumeration() {

		private final Enumeration enumeration = servletConfig == null ?
			null : servletConfig.getInitParameterNames();

		private final Iterator iterator = parameterMap.keySet().iterator();

		public boolean hasMoreElements() {
		    return iterator.hasNext() || (enumeration != null && enumeration.hasMoreElements());
		}

		public Object nextElement() {
		    return enumeration != null && enumeration.hasMoreElements() ?
			    enumeration.nextElement() : iterator.next();
		}

	    };
	}

	public ServletContext getServletContext() {
	    return servletConfig == null ? null : servletConfig.getServletContext();
	}

	public String getServletName() {
	    return servletConfig == null ? null : servletConfig.getServletName();
	}

    }

    public static ServletConfig servletConfig = null;

    @Override
    public void init(final ServletConfig config) throws ServletException {
	final ServletConfigWrapper servletConfigWrapper = new ServletConfigWrapper(config);
	super.init(servletConfigWrapper);
	this.servletConfig = config;
    }

}
