<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<tiles:importAttribute name="infoCredits" ignore="true"/>
<logic:present name="infoCredits">
		<b>
			<bean:write name="infoCredits" property="lessonsFormatted"/>						
		</b>
		<bean:message key="label.credits.lessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>	
			<bean:write name="infoCredits" property="masterDegreeCreditsFormatted"/>						
		</b>							
		<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
		<b>	
			<bean:write name="infoCredits" property="supportLessonsFormatted"/>
		</b>							
		<bean:message key="label.credits.supportLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,
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
