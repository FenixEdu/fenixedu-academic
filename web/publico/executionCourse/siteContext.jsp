<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
 
<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>

<bean:define id="executionCourse" name="site" property="siteExecutionCourse" toScope="request"/>
<bean:define id="siteActionName" value="/executionCourse.do" toScope="request"/>
<bean:define id="siteContextParam" value="executionCourseID" toScope="request"/>
<bean:define id="siteContextParamValue" name="executionCourse" property="idInternal" toScope="request"/>
