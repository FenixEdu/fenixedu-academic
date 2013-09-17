
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>
