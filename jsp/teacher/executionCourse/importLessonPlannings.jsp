<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>	
	<bean:define id="submitUrl">/manageExecutionCourse.do?method=listExecutionCoursesToImportLessonPlannings&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
	<bean:define id="showLessonPlannings">/manageExecutionCourse.do?method=lessonPlannings&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="postBackLink">/manageExecutionCourse.do?method=prepareImportLessonPlanningsPostBack&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="invalidLink">/manageExecutionCourse.do?method=prepareImportLessonPlanningsInvalid&executionCourseID=<bean:write name="executionCourseID"/></bean:define>	

	<h2><bean:message key="label.import.lessonPlannings.title"/></h2>

	<div class="infoop2">
		<bean:message key="label.lessonsPlanning.import.instructions"/>
	</div>
							
	<fr:edit id="importLessonPlanningBean" name="importLessonPlanningBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings"
		 action="<%= submitUrl %>">
		<fr:destination name="postBack" path="<%= postBackLink %>"/>
		<fr:destination name="invalid" path="<%= invalidLink %>"/>
		<fr:destination name="cancel" path="<%= showLessonPlannings %>"/>
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight"/>
		        <fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>

	<logic:notEmpty name="importLessonPlanningBean" property="curricularYear">	
	 	<logic:notEmpty name="importLessonPlanningBean" property="executionPeriod">	
	 		<logic:notEmpty name="importLessonPlanningBean" property="executionDegree">	
				<bean:define id="importLessonPlanningsUrl">/manageExecutionCourse.do?method=importLessonPlannings&executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
				<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.course"/>:</strong></p>			
				<fr:edit id="importLessonPlanningBeanWithExecutionCourse" name="importLessonPlanningBean" schema="ListExecutionCoursesToImportLessonPlannings" 
					action="<%= importLessonPlanningsUrl %>" >
					<fr:destination name="cancel" path="<%= showLessonPlannings %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle5 thlight mtop0"/>
					        <fr:property name="columnClasses" value=",,tdclear"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
		
	<logic:notEmpty name="noShifts">	
		<br/><p><bean:message key="label.no.shits.to.import.lessonPlannings"/></p>
	</logic:notEmpty>		
	<logic:notEmpty name="shifts">		
		<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.shift.import"/>:</strong></p>
		<html:form action="/manageExecutionCourse">
			<html:hidden property="method" value="importLessonPlanningsBySummaries"/>
			<html:hidden property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<table class="tstyle5 thlight mtop0" border="0">	
				<tr>
					<th><bean:message key="label.shift"/>:</th>		
					<td>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shiftID" >
							<html:options collection="shifts" property="value" labelProperty="label"/>
						</html:select>				
					</td>					
				</tr>				
			</table>
			<html:submit><bean:message key="label.submit"/></html:submit>
			<html:button property="cancel" onclick="this.form.method.value='lessonPlannings';this.form.submit()"><bean:message key="button.cancel"/></html:button>	
		</html:form>
	</logic:notEmpty>
	
</logic:present>
