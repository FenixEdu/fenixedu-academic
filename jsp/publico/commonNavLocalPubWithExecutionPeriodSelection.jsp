<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<ul>
<logic:present name="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>" scope="request">

	<html:form action="/index">
		<html:hidden  property="<%SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="choose"/>
		
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td nowrap="nowrap" width="125">
			    	<bean:message key="property.executionPeriod"/>:
			    </td>
			</tr>
			<tr>
			    <td nowrap="nowrap">
			    	<html:select property="index" size="1"
			    				 onchange='this.form.submit();'>
						<html:options property="value"
							labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"/>
					</html:select>
			    </td>
			</tr>
		</table>
	</html:form> 
	<br />
</logic:present>

	<li><html:link page="<%= "/index.do?method=prepare&amp;page=0&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.public.home"/> </html:link></li>
	<%--<li><html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.consult"/> </html:link></li>--%>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.classes.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.executionCourse.consult"/> </html:link></li>
	<li><html:link page="<%= "/prepareConsultRooms.do?executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" > <bean:message key="link.rooms.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" ><bean:message key="link.exams.consult"/> </html:link></li>

</ul>