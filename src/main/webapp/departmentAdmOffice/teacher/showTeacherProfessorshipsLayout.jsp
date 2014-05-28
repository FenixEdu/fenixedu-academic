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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:importAttribute/>
<tiles:useAttribute id="executionCourseLink" name="executionCourseLink" classname="java.lang.String"/>
<tiles:useAttribute id="paramId" name="paramId" classname="java.lang.String"/>
<bean:parameter id="executionPeriodId" name="executionPeriodId"/>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherId"/><br />
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="infoTeacher" paramProperty="teacherId">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<logic:notEmpty name="detailedProfessorshipList" >
	<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%" cellpadding="5" border="0">
			<tr>
				<th class="listClasses-header" style="text-align:left"><bean:message key="label.execution-course.name" />
				</th>
				<th class="listClasses-header" style="text-align:left"><bean:message key="label.execution-course.degrees" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.execution-course.responsibleFor" />
				</th>
				<th class="listClasses-header"><bean:message key="label.execution-period" />
				</th>
			</tr>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses" style="text-align:left">
						<html:link page="<%= executionCourseLink %>" paramId="<%= paramId %>" paramName="infoExecutionCourse" paramProperty="externalId">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="infoDegreeList" name="detailedProfessorship" property="infoDegreeList" />
						<bean:size id="degreeSizeList" name="infoDegreeList"/>
						<logic:iterate id="infoDegree" name="infoDegreeList" indexId="index">
							<bean:write name="infoDegree" property="sigla" /> 
							<logic:notEqual name="degreeSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
							,
							</logic:notEqual>
						</logic:iterate>
					</td>
					<td class='listClasses'>
						<logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
							<bean:message key="label.yes"/>
						</logic:equal>
						<logic:notEqual name="detailedProfessorship" property="responsibleFor" value="true">
							<bean:message key="label.no"/>						
						</logic:notEqual>
					</td>
					<td class="listClasses">
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>