<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<ul>
	<logic:present name="<%= SessionConstants.EXECUTION_PERIOD%>" scope="request">
		<h4 align="center"><bean:write name="<%= SessionConstants.EXECUTION_PERIOD%>" property="name" scope="request"/> - <bean:write name="<%= SessionConstants.EXECUTION_PERIOD%>" property="infoExecutionYear.year" scope="request"/></h4><br />
	</logic:present>
	
	<li><html:link page="<%= "/index.do?method=prepare&amp;page=0&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.public.home"/> </html:link></li>
	<li><html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.classes.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.executionCourse.consult"/> </html:link></li>
	<li><html:link page="<%= "/prepareConsultRooms.do?executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" > <bean:message key="link.rooms.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" ><bean:message key="link.exams.consult"/> </html:link></li>
</ul>