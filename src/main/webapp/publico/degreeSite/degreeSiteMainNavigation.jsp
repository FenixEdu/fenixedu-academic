<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject" %>
<%@ page import="net.sourceforge.fenixedu.domain.Degree" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<%
	net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext currentContext = (net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext) net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext.getCurrentContext(request);
	net.sourceforge.fenixedu.domain.DegreeSite selectedContainer = (net.sourceforge.fenixedu.domain.DegreeSite) currentContext.getSelectedContainer();	
	net.sourceforge.fenixedu.domain.Degree degree = selectedContainer.getDegree();

	if (degree != null) {
	    request.setAttribute("site", degree.getSite());
	}
%>

<fr:view name="site" type="net.sourceforge.fenixedu.domain.Site" layout="side-menu"/>
