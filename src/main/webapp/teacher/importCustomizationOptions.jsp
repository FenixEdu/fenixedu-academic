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
	<bean:define id="showCustomizationOptions">/alternativeSite.do?method=prepareCustomizationOptions&amp;objectCode=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="submitURL">/alternativeSite.do?method=submitDataToImportCustomizationOptions&amp;objectCode=<bean:write name="executionCourseID"/></bean:define>	
	<bean:define id="chooseExecutionPeriodUrl">/alternativeSite.do?method=submitDataToImportCustomizationOptionsPostBack&amp;objectCode=<bean:write name="executionCourseID"/></bean:define>	
	
	<h2><bean:message key="label.import.customizationOptions.title"/></h2>
	
	<fr:form action="<%= showCustomizationOptions %>">
		<fr:edit id="importContentBean" name="importContentBean" schema="ChooseDegreePeriodAndCurricularYearToImportLessonPlannings">
			<fr:destination name="postBack" path="<%= chooseExecutionPeriodUrl %>"/>
			<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle5 thlight"/>
			        <fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
		<html:cancel><bean:message key="button.cancel"/></html:cancel>
	</fr:form>							

	<bean:define id="importCustomizationOptionsUrl">/alternativeSite.do?method=importCustomizationOptions&objectCode=<bean:write name="executionCourseID"/></bean:define>				
	<logic:notEmpty name="importContentBean" property="curricularYear">	
	 	<logic:notEmpty name="importContentBean" property="executionPeriod">	
	 		<logic:notEmpty name="importContentBean" property="executionDegree">	
				<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.course"/>:</strong></p>			
				<fr:edit id="importContentBeanWithExecutionCourse" name="importContentBean" schema="ListExecutionCoursesToImportContent" action="<%= importCustomizationOptionsUrl %>" >
					<fr:destination name="cancel" path="<%= showCustomizationOptions %>"/>
					<fr:destination name="invalid" path="<%= submitURL %>"/>
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle5 thlight mtop0"/>
					        <fr:property name="columnClasses" value=",,tdclear"/>
					</fr:layout>
				</fr:edit>						
			</logic:notEmpty>
		</logic:notEmpty>
	</logic:notEmpty>
	
</logic:present>
