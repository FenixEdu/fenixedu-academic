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
<tiles:importAttribute name="infoCredits" ignore="true"/>
<logic:present name="infoCredits">
		<b>
			<bean:write name="infoCredits" property="lessonsFormatted"/>						
		</b>
		<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,		
		<b>	
			<bean:write name="infoCredits" property="supportLessonsFormatted"/>
		</b>							
		<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>
			<bean:write name="infoCredits" property="masterDegreeCreditsFormatted"/>						
		</b>							
		<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,		
		<b>
			<bean:write name="infoCredits" property="degreeFinalProjectStudentsFormatted"/>						
		</b>							
		<bean:message key="label.credits.degreeFinalProjectStudents.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>							
			<bean:write name="infoCredits" property="institutionWorkTimeFormatted"/>						
		</b>							
		<bean:message key="label.credits.institutionWorkTime.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>							
			<bean:write name="infoCredits" property="otherTypeCreditsFormatted"/>						
		</b>							
		<bean:message key="label.credits.otherTypeCreditLine.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<b>		
			<logic:equal name="infoCredits" property="containsManagementPositions" value="true">
				<bean:message key="label.yes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>				
			</logic:equal>					
			<logic:equal name="infoCredits" property="containsManagementPositions" value="false">
				<bean:message key="label.no" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>				
			</logic:equal>					
		</b>							
		<bean:message key="label.credits.managementPositions.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		<b>		
			<logic:equal name="infoCredits" property="containsServiceExemptionsSituations" value="true">
				<bean:message key="label.yes" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>				
			</logic:equal>					
			<logic:equal name="infoCredits" property="containsServiceExemptionsSituations" value="false">
				<bean:message key="label.no" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>				
			</logic:equal>					
		</b>							
		<bean:message key="label.credits.serviceExemptionSituations.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		
		
</logic:present>
<logic:notPresent name="infoCredits">
	--
</logic:notPresent>	
