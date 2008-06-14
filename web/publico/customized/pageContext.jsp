
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>
