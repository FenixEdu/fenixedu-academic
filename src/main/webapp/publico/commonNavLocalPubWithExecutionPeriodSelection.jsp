<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<ul>
<logic:present name="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>" scope="request">

	<html:form action="/index">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="<%="hidden." + PresentationConstants.EXECUTION_PERIOD_OID%>"  property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
		
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td nowrap="nowrap" width="125">
			    	<bean:message key="property.executionPeriod"/>:
			    </td>
			</tr>
			<tr>
			    <td nowrap="nowrap">
			    	<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1"
			    				 onchange='this.form.submit();'>
						<html:options property="value"
							labelProperty="label" 
							collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>"/>
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			    </td>
			</tr>
		</table>
	</html:form> 
	<br />
</logic:present>

	<li><html:link page="<%= "/index.do?method=prepare&amp;page=0&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.public.home"/> </html:link></li>
	<%--<li><html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" ><bean:message key="link.degree.consult"/> </html:link></li>--%>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.classes.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" > <bean:message key="link.executionCourse.consult"/> </html:link></li>
	<li><html:link page="<%= "/prepareConsultRooms.do?executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" > <bean:message key="link.rooms.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" ><bean:message key="link.exams.consult"/> </html:link></li>

</ul>