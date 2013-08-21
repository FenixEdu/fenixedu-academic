<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<jsp:include page="teacherCreditsStyles.jsp"/>


<em><bean:message key="label.managementFunctionNote" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></em>
<h3><bean:message key="label.managementFunctionNote" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<logic:present name="personFunctions">
	<bean:define id="userView" name="USER_SESSION_ATTRIBUTE"/>
	<bean:define id="teacher" name="userView" property="user.person.teacher"/>
	<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="teacher" property="person.username"/></bean:define>
	<table class="headerTable"><tr>
	<td><img src="<%= request.getContextPath() + url %>"/></td>
	<td ><fr:view name="teacher">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.Teacher">
			<fr:slot name="person.presentationName" key="label.name"/>
			<fr:slot name="currentWorkingDepartment.name" key="label.department" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="creditsStyle"/>
		</fr:layout>
	</fr:view></td>
	</tr></table>

	<br/><br/>
	<span class="error"><bean:message key="message.consultCCAD" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span>
	<br/>
	<logic:notEmpty name="personFunctions">
		<table class="tstyle2 thlight thleft mtop05 mbottom05">
			<tr>
				<th><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.beginDate" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.endDate" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.percentage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				<th><bean:message key="label.managementPosition.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			</tr>
		<logic:iterate id="personFunction" name="personFunctions">
			<tr>
				<td><bean:write name="personFunction" property="function.name"/></td>
				<td><bean:write name="personFunction" property="function.unit.name"/></td>
				<td><bean:write name="personFunction" property="beginDate"/></td>
				<td><bean:write name="personFunction" property="endDate"/></td>
				<% if(personFunction instanceof net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunctionShared){ %>
					<td align="center"><bean:write name="personFunction" property="percentage"/></td>
				<% } else { %>
					<td align="center">-</td>
				<% }%>
				<%-- --%>
				<td><bean:write name="personFunction" property="credits"/></td>
			</tr>
		</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>