<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="messages" message="true">
	<span class="error"><bean:write name="messages" /></span>
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
	
	<div class='simpleblock4'>
	<fieldset class='lfloat'>

	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.executionYear"/>:</label>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID">
		<html:options collection="executionYears" property="idInternal" labelProperty="year" /> 
	</html:select>
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.campus"/>:</label>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.campusID" property="campusID">
		<html:options collection="campus" property="idInternal" labelProperty="name" /> 
	</html:select>
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.temporaryExamMap"/>:</label>
	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.temporaryExamMap" name="executionDegree" property="temporaryExamMap" value="true" />
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/> 1� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsFirstSemesterBegin" maxlength="10" size="10" property="periodLessonsFirstSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsFirstSemesterEnd" maxlength="10" size="10" property="periodLessonsFirstSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/> 1� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsFirstSemesterBegin" maxlength="10" size="10" property="periodExamsFirstSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsFirstSemesterEnd" maxlength="10" size="10" property="periodExamsFirstSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/> 2� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsSecondSemesterBegin" maxlength="10" size="10" property="periodLessonsSecondSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodLessonsSecondSemesterEnd" maxlength="10" size="10" property="periodLessonsSecondSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/> 2� <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSecondSemesterBegin" maxlength="10" size="10" property="periodExamsSecondSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSecondSemesterEnd" maxlength="10" size="10" property="periodExamsSecondSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.examsSpecialSeason"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSpecialSeasonBegin" maxlength="10" size="10" property="periodExamsSpecialSeasonBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodExamsSpecialSeasonEnd" maxlength="10" size="10" property="periodExamsSpecialSeasonEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionNormalSeason1"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodGradeSubmissionNormalSeasonFirstSemesterEnd" maxlength="10" size="10" property="periodGradeSubmissionNormalSeasonFirstSemesterEnd"/>(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionNormalSeason2"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodGradeSubmissionNormalSeasonSecondSemesterEnd" maxlength="10" size="10" property="periodGradeSubmissionNormalSeasonSecondSemesterEnd"/>(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.gradeSubmissionSpecialSeason"/></label>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.periodGradeSubmissionSpecialSeasonEnd" maxlength="10" size="10" property="periodGradeSubmissionSpecialSeasonEnd"/>(dd/mm/yyyy)
	</p>
	
	</fieldset>
	</div>
		
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>

</html:form>

<br/>
<br/>
<fr:edit name="executionDegree"
		type="net.sourceforge.fenixedu.domain.ExecutionDegree"
		schema="net.sourceforge.fenixedu.domain.ExecutionDegree.annotation"
		/>