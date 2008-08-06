<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="messages" message="true">
	<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<bean:define id="degreeType" name="executionDegree" property="degreeCurricularPlan.degree.degreeType.name" />
	<bean:define id="degreeCurricularPlanID" name="executionDegree" property="degreeCurricularPlan.idInternal" />
	<bean:define id="executionDegreeID" name="executionDegree" property="idInternal" />

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
					<html:options collection="executionYears" property="idInternal" labelProperty="year" /> 
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.campus"/>: </th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.campusID" property="campusID">
					<html:options collection="campus" property="idInternal" labelProperty="name" /> 
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