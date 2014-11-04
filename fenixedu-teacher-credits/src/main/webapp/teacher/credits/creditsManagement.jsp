<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/creditsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="processForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherOID" property="teacherOID"/>
	<h2 class="infoop"><bean:write name="infoCreditsTeacher" property="infoTeacher.infoPerson.nome"/></h2>
	<table cellpadding="5" cellspacing="1">
		<tr>
			<td class="listClasses" colspan="2"><b><bean:message key="label.tfc.students.number"/></b></td>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.tfcStudentsNumber" property="tfcStudentsNumber" size="5"/></td>
		</tr>
		<tr>
			<td class="listClasses" rowspan="2"><b><bean:message key="label.additional.credits"/></b></td>
			<td class="listClasses"><b><bean:message key="label.credits"/></b></td>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.additionalCredits" property="additionalCredits" size="5"/></td>
		</tr>
		<tr>
			<td class="listClasses"><b><bean:message key="label.additional.credits.reason	"/></b></td>
			<td class="listClasses"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.additionalCreditsJustification" property="additionalCreditsJustification" rows="2" cols="50"/></td>
		</tr>
	</table>	
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<bean:define id="link">
		/executionCourseShiftsPercentageManager.do?method=show&amp;teacherOID=<bean:write name="infoCreditsTeacher" property="infoTeacher.externalId"/>
	</bean:define>
	<tiles:insert definition="teacher-professor-ships">
		<tiles:put name="title" value="label.professorships"/>
		<tiles:put name="link" value="<%= link %>"/>
	</tiles:insert>	
</html:form>
