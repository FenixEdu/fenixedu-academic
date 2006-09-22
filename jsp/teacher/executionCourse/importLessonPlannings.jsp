<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>		
	<bean:define id="showLessonPlannings">/manageExecutionCourse.do?method=lessonPlannings&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="submitURL">/manageExecutionCourse.do?method=submitDataToImportLessonPlannings&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="chooseExecutionPeriodUrl">/manageExecutionCourse.do?method=submitDataToImportLessonPlanningsPostBack&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	
	<h2><bean:message key="label.import.lessonPlannings.title"/></h2>

	<div class="infoop2">
		<bean:message key="label.lessonsPlanning.import.instructions"/>
	</div>
	
	<fr:form action="<%= showLessonPlannings %>">
		<fr:edit id="importLessonPlanningBean" name="importLessonPlanningBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings">
			<fr:destination name="postBack" path="<%= chooseExecutionPeriodUrl %>"/>
			<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle5 thlight"/>
			        <fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</fr:form>							

	<bean:define id="importLessonPlanningsUrl">/manageExecutionCourse.do?method=importLessonPlannings&executionCourseID=<bean:write name="executionCourseID"/></bean:define>				
	<logic:notEmpty name="importLessonPlanningBean" property="curricularYear">	
	 	<logic:notEmpty name="importLessonPlanningBean" property="executionPeriod">	
	 		<logic:notEmpty name="importLessonPlanningBean" property="executionDegree">	
				<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.course"/>:</strong></p>			
				<fr:edit id="importLessonPlanningBeanWithExecutionCourse" name="importLessonPlanningBean" schema="ListExecutionCoursesToImportLessonPlannings" 	action="<%= importLessonPlanningsUrl %>" >
					<fr:destination name="cancel" path="<%= showLessonPlannings %>"/>
					<fr:destination name="invalid" path="<%= submitURL %>"/>
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle5 thlight mtop0"/>
					        <fr:property name="columnClasses" value=",,tdclear"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
		
	<logic:notEmpty name="importLessonPlanningBean" property="importType">	
		<bean:define id="importLessonPlanningsWithShiftsUrl">/manageExecutionCourse.do?method=importLessonPlanningsBySummaries&executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
		<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.shift.import"/>:</strong></p>
		<fr:edit id="importLessonPlanningBeanWithShits" name="importLessonPlanningBean" schema="ListShiftsToImportLessonPlannings" action="<%= importLessonPlanningsWithShiftsUrl %>">			
			<fr:destination name="invalid" path="<%= submitURL %>"/>
			<fr:destination name="cancel" path="<%= showLessonPlannings %>"/>
			<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle5 thlight"/>
			        <fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>				
	
</logic:present>
