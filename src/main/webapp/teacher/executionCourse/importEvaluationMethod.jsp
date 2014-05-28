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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<logic:present name="executionCourse">

	<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>	
	<bean:define id="submitUrl">/manageEvaluationMethod.do?method=listExecutionCoursesToImportEvaluationMethod&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>
	<bean:define id="showEvaluationMethod">/manageEvaluationMethod.do?method=evaluationMethod&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="postBackLink">/manageEvaluationMethod.do?method=prepareImportEvaluationMethodPostBack&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="invalidLink">/manageEvaluationMethod.do?method=prepareImportBiliographicReferencesInvalid&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>	

	<h2><bean:message key="link.import.evaluationMethod"/></h2>
	
	<fr:form action="<%= showEvaluationMethod %>">						
		<fr:edit id="importContentBean" name="importContentBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings"
			 action="<%= submitUrl %>">
			<fr:destination name="postBack" path="<%= postBackLink %>"/>			
			<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle5 thlight thright"/>
			        <fr:property name="columnClasses" value=",,"/>
			</fr:layout>
		</fr:edit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</fr:form>

	<logic:notEmpty name="importContentBean" property="curricularYear">	
	 	<logic:notEmpty name="importContentBean" property="executionPeriod">	
	 		<logic:notEmpty name="importContentBean" property="executionDegree">	
				<bean:define id="importEvaluationMethodUrl">/manageEvaluationMethod.do?method=importEvaluationMethod&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>		
				<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.course"/></strong></p>
				<fr:edit id="importContentBeanWithExecutionCourse" name="importContentBean" schema="ListExecutionCoursesToImportContent" 
					action="<%= importEvaluationMethodUrl %>" >
					<fr:destination name="cancel" path="<%= showEvaluationMethod %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
					        <fr:property name="columnClasses" value=",,"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>

	<logic:notEmpty name="shifts">		
		<p class="mtop1 mbottom05"><strong><bean:message key="label.choose.shift"/></strong></p>
		<html:form action="/manageExecutionCourse">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="importLessonPlanningsBySummaries"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<table class="tstyle5 thright thlight mtop05">	
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
			<html:button bundle="HTMLALT_RESOURCES" altKey="button.cancel" property="cancel" onclick="this.form.method.value='evaluationMethod';this.form.submit()"><bean:message key="button.cancel"/></html:button>	
		</html:form>
	</logic:notEmpty>
	
</logic:present>
