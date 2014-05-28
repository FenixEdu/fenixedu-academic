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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:messages id="messages" message="true">
	<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<bean:define id="degreeType" name="executionDegree" property="degreeCurricularPlan.degree.degreeType.name" />
	<bean:define id="degreeCurricularPlanID" name="executionDegree" property="degreeCurricularPlan.externalId" />
	<bean:define id="executionDegreeID" name="executionDegree" property="externalId" />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editExecutionDegree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= degreeType.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= executionDegreeID.toString() %>"/>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.executionDegree"/></h3>
	
	<table class="tstyle4 thlight thright mtop025">
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.executionYear"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID">
					<html:options collection="executionYears" property="externalId" labelProperty="year" /> 
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.campus"/>: </th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.campusID" property="campusID">
					<html:options collection="campus" property="externalId" labelProperty="name" /> 
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.temporaryExamMap"/>: </th>
			<td><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.temporaryExamMap" name="executionDegree" property="temporaryExamMap" value="true" /></td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/> 1º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsFirstSemesterBegin" maxlength="10" size="10" property="periodLessonsFirstSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsFirstSemesterEnd" maxlength="10" size="10" property="periodLessonsFirstSemesterEnd"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/> 1º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsFirstSemesterBegin" maxlength="10" size="10" property="periodExamsFirstSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsFirstSemesterEnd" maxlength="10" size="10" property="periodExamsFirstSemesterEnd"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/> 2º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsSecondSemesterBegin" maxlength="10" size="10" property="periodLessonsSecondSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsSecondSemesterEnd" maxlength="10" size="10" property="periodLessonsSecondSemesterEnd"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/> 2º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSecondSemesterBegin" maxlength="10" size="10" property="periodExamsSecondSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSecondSemesterEnd" maxlength="10" size="10" property="periodExamsSecondSemesterEnd"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.examsSpecialSeason"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSpecialSeasonBegin" maxlength="10" size="10" property="periodExamsSpecialSeasonBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSpecialSeasonEnd" maxlength="10" size="10" property="periodExamsSpecialSeasonEnd"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionNormalSeason1"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodGradeSubmissionNormalSeasonFirstSemesterEnd" maxlength="10" size="10" property="periodGradeSubmissionNormalSeasonFirstSemesterEnd"/>(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionNormalSeason2"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodGradeSubmissionNormalSeasonSecondSemesterEnd" maxlength="10" size="10" property="periodGradeSubmissionNormalSeasonSecondSemesterEnd"/>(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionSpecialSeason"/></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.periodGradeSubmissionSpecialSeasonEnd" maxlength="10" size="10" property="periodGradeSubmissionSpecialSeasonEnd"/>(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<th></th>
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.save"/></html:submit></td>
		</tr>
	</table>

</html:form>

<br/>
<br/>
<fr:edit name="executionDegree" type="net.sourceforge.fenixedu.domain.ExecutionDegree"
		 schema="net.sourceforge.fenixedu.domain.ExecutionDegree.annotation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
	    <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>