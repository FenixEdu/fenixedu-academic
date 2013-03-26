<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<h2><bean:message key="label.shifts"/></h2>

<logic:notEmpty name="shifts">
	<table class="tstyle1 thlight tdcenter">
		<tr>
			<th><bean:message key="label.shift"/></th>
			<th><bean:message key="property.shift.capacity"/></th>
			<th><bean:message key="property.number.students.attending.course"/></th>
			<th></th>
		</tr>
		<logic:iterate id="shift" name="shifts">
		<tr>
			<td style="text-align:left"><fr:view name="shift" property="presentationName"/></td>
			<td><fr:view name="shift" property="lotacao"/></td>
			<td><fr:view name="shift" property="studentsCount"/></td>
			<bean:define id="executionCourseID" name="executionCourseID"></bean:define>
			<td><html:link page="<%= "/manageExecutionCourse.do?method=editShift&executionCourseID=" + executionCourseID %>" paramId="shiftID" paramName="shift" paramProperty="idInternal"><bean:message key="label.edit"/></html:link></td>
		</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="shifts">
	<p><em><bean:message key="label.shifts.nondefined"/></em></p>
</logic:empty>
