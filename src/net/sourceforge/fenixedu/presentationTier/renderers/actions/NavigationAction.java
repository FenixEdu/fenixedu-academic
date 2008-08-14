package net.sourceforge.fenixedu.presentationTier.renderers.actions;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.struts.action.Action;

public abstract class NavigationAction extends Action {
    public static final String NAVIGATION_SHOW = "show";

    protected IUserView getUserView(HttpServletRequest request) {
	return (IUserView) request.getSession().getAttribute("UserView");
    }

    protected Class getGivenType(HttpServletRequest request) throws ClassNotFoundException {
	String name = getCompleteTypeName(request.getParameter("type"));

	return Class.forName(name);
    }

    protected int getGivenOid(HttpServletRequest request) {
	return new Integer(request.getParameter("oid"));
    }

    protected String getGivenSchema(HttpServletRequest request) {
	return request.getParameter("schema");
    }

    protected String getGivenLayout(HttpServletRequest request) {
	return request.getParameter("layout");
    }

    protected DomainObject getTargetObject(HttpServletRequest request) throws FenixFilterException, FenixServiceException,
	    ClassNotFoundException {
	Integer oid = getGivenOid(request);
	Class type = getGivenType(request);
	return RootDomainObject.getInstance().readDomainObjectByOID(type, oid);
    }

    protected String getCompleteTypeName(String typeName) {
	if (typeName.startsWith("net.sourceforge.fenixedu.domain.")) {
	    return typeName;
	} else {
	    return "net.sourceforge.fenixedu.domain." + typeName;
	}
    }

    public static String getTypeName(Class type) {
	String typeName = type.getName();

	if (typeName.startsWith("net.sourceforge.fenixedu.domain.")) {
	    typeName = typeName.substring("net.sourceforge.fenixedu.domain.".length());
	}

	return typeName;
    }
}
