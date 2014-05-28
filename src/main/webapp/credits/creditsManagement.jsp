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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/creditsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="processForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherOID" property="teacherOID"/>
	<table width="100%" cellpadding="5" cellspacing="1">
		<tr>
			<th class="listClasses-header" colspan="2"><b><bean:message key="label.tfc.students.number"/></b></th>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.tfcStudentsNumber" property="tfcStudentsNumber" size="5"/></td>
		</tr>
		<tr>
			<th class="listClasses-header" rowspan="2"><b><bean:message key="label.additional.credits"/></b></th>
			<th class="listClasses-header"><b><bean:message key="label.credits"/></b></th>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.additionalCredits" property="additionalCredits" size="5"/></td>
		</tr>
		<tr>
			<th class="listClasses-header"><b><bean:message key="label.additional.credits.reason"/></b></th>
			<td class="listClasses" style="text-align:left"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.additionalCreditsJustification" property="additionalCreditsJustification" rows="2" cols="60"/></td>
		</tr>
	</table>	
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<br />
	<br />
	<bean:define id="link">
		/executionCourseShiftsPercentageManager.do?method=show&amp;teacherOID=<bean:write name="creditsView" property="infoCredits.infoTeacher.externalId"/>
	</bean:define>
	<logic:empty name="creditsView" property="infoProfessorshipList">
		<span class="error"><!-- Error messages go here --><bean:message key="message.teacher.no.professorship"/></span>
	</logic:empty>
	<logic:notEmpty name="creditsView" property="infoProfessorshipList">
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<th class="listClasses-header"><bean:message key="label.professorships.acronym" />
				</th>
				<th class="listClasses-header"><bean:message key="label.professorships.name" />
				</th>
			</tr>
			<logic:iterate id="professorship" name="creditsView" property="infoProfessorshipList">
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="externalId">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="externalId">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</html:form>
