<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
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
