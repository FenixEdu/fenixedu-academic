<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<h2><bean:message key="label.shifts"/></h2>

<ul>
	<li>
		<html:link page="/manageExecutionCourse.do?method=manageShifts" paramId="executionCourseID" paramName="executionCourseID">
			<bean:message key="label.back"/>
		</html:link>
	</li>
</ul>

<logic:present name="shift">
	<bean:define id="shiftID" name="shift" property="idInternal"/>
	<p>
		<b><bean:message key="label.shift"/>:</b> <fr:view name="shift" property="nome"/>
	</p>
	<logic:empty name="registrations">
		<p><em><bean:message key="label.shifts.empty"/></em></p>
	</logic:empty>
	
	<logic:present name="registration">
		<p><span class="success0">O aluno <fr:view name="registration" property="person.name"/> foi removido do turno.</span></p>
	</logic:present>
	
	<logic:notEmpty name="registrations">
	<bean:define id="executionCourseID" name="executionCourseID"/>
	<p class="mbottom0">
		<html:link page="<%= "/manageExecutionCourse.do?method=removeAttendsFromShift&executionCourseID=" + executionCourseID + "&removeAll=true" %>" paramId="shiftID" paramName="shift" paramProperty="idInternal">
			<bean:message key="label.shifts.remove"/>
		</html:link>
	</p>
	<table class="tstyle1">
		<tr>
			<th>
				<bean:message key="label.name"/>
			</th>
			<th></th>
		<logic:iterate id="registration" name="registrations">
			<bean:define id="registrationID" name="registration" property="idInternal"/>
			<tr>
				<td><fr:view name="registration" property="person.name"/></td>
				<td><a href="<%= request.getContextPath() + "/teacher/manageExecutionCourse.do?method=removeAttendsFromShift&shiftID=" + shiftID + "&registrationID=" + registrationID + "&executionCourseID=" + executionCourseID %>"><bean:message key="label.remove"/></a></td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
	</table>
</logic:present>