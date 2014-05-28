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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>		
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
