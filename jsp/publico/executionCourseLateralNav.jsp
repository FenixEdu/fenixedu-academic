<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="sections" name="component" property="sections"/>

<ul>
	<li><html:link page="<%= "/viewSite.do?method=firstPage&amp;objectCode=" + pageContext.findAttribute("executionCourseCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.inicialPage"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.announcements"/></html:link></li>
	
	<li><html:link page="<%= "/viewSite.do" + "?method=summaries&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.summaries.public"/></html:link></li>
	
	<li><html:link page="<%= "/viewSite.do?method=evaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.evaluationMethod"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do?method=bibliography&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.bibliography"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=timeTable&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.executionCourse.timeTable"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=shifts&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.executionCourse.shifts"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=evaluation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.evaluation"/></html:link></li>

	<li><html:link page="<%= "/viewSite.do" + "?method=viewExecutionCourseProjects&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>">
		<bean:message key="link.groupings"/></html:link></li>

	<logic:notEmpty name="sections" >
		<logic:present name="infoSection" >
<%-- 			<li><html:link page="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>"></html:link></li>
--%>
			<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="infoSection" />
		</logic:present>
		<logic:notPresent name="infoSection" >
			<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" />
		</logic:notPresent>		
	</logic:notEmpty>
	
	<logic:notEmpty name="component" property="mail" >	
		<bean:define id="siteMail" name="component" property="mail" />
		<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail") %>">
			<div class="email"><p><bean:write name="siteMail" /></p></div>
		</html:link>
	</logic:notEmpty>
</ul>
