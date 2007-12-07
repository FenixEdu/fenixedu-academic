<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>

<bean:define id="siteActionName" value="/pedagogicalCouncil/viewSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="unitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>
<bean:define id="unit" name="site" property="unit" toScope="request"/>
