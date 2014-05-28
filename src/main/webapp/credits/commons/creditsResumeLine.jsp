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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<tiles:importAttribute name="creditLineDTO" ignore="true"/>
<logic:present name="creditLineDTO">
		<b>
			<bean:write name="creditLineDTO" property="teachingDegreeCredits"/>						
		</b>
		<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>	
			<bean:write name="creditLineDTO" property="supportLessonHours"/>
		</b>							
		<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>	
			<bean:write name="creditLineDTO" property="masterDegreeCredits"/>						
		</b>							
		<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,		
		<b>
			<bean:write name="creditLineDTO" property="tfcAdviseCredits"/>						
		</b>
		<bean:message key="label.credits.thesis.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,		
		<b>
			<bean:write name="creditLineDTO" property="thesesCredits"/>						
		</b>							
		<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>							
			<bean:write name="creditLineDTO" property="institutionWorkingHours"/>						
		</b>							
		<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>							
			<bean:write name="creditLineDTO" property="otherCredits"/>						
		</b>							
		<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<b>						
			<bean:write name="creditLineDTO" property="managementCredits"/>				
		</b>							
		<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<b>			
			<bean:write name="creditLineDTO" property="serviceExemptionCredits"/>				
		</b>							
		<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	
		
</logic:present>
<logic:notPresent name="creditLineDTO">
	--
</logic:notPresent>	
