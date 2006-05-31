<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:messages id="messages" message="true">
	<span class="error"><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<bean:define id="degreeType" name="executionDegree" property="degreeCurricularPlan.degree.degreeType.name" />
	<bean:define id="degreeCurricularPlanID" name="executionDegree" property="degreeCurricularPlan.idInternal" />
	<bean:define id="executionDegreeID" name="executionDegree" property="idInternal" />

	<html:hidden property="method" value="editExecutionDegree"/>
	<html:hidden property="degreeType" value="<%= degreeType.toString() %>"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>" />
	<html:hidden property="executionDegreeID" value="<%= executionDegreeID.toString() %>"/>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.executionDegree"/></h3>
	
	<div class='simpleblock4'>
	<fieldset class='lfloat'>

	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.executionYear"/>:</label>
	<html:select property="executionYearID">
		<html:options collection="executionYears" property="idInternal" labelProperty="year" /> 
	</html:select>
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.campus"/>:</label>
	<html:select property="campusID">
		<html:options collection="campus" property="idInternal" labelProperty="name" /> 
	</html:select>
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.temporaryExamMap"/>:</label>
	<html:checkbox name="executionDegree" property="temporaryExamMap" value="true" />
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/> 1º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text maxlength="10" size="10" property="periodLessonsFirstSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text maxlength="10" size="10" property="periodLessonsFirstSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/> 1º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text maxlength="10" size="10" property="periodExamsFirstSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text maxlength="10" size="10" property="periodExamsFirstSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.lessons"/> 2º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text maxlength="10" size="10" property="periodLessonsSecondSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text maxlength="10" size="10" property="periodLessonsSecondSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.exams"/> 2º <bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/></label>
	<html:text maxlength="10" size="10" property="periodExamsSecondSemesterBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text maxlength="10" size="10" property="periodExamsSecondSemesterEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	<p><label><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.examsSpecialSeason"/></label>
	<html:text maxlength="10" size="10" property="periodExamsSpecialSeasonBegin"/>&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.to" />&nbsp;<html:text maxlength="10" size="10" property="periodExamsSpecialSeasonEnd"/>&nbsp;(dd/mm/yyyy)
	</p>
	
	</fieldset>
	</div>
		
	<html:submit styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>

</html:form>