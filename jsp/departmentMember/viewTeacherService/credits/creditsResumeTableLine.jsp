<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<tiles:importAttribute name="creditLineDTO" ignore="true"/>
<logic:present name="creditLineDTO">

		<td><bean:write name="creditLineDTO" property="teachingDegreeCredits"/></td>		
<%--		<b>	
			<bean:write name="creditLineDTO" property="masterDegreeCreditsFormatted"/>						
		</b>							
		<bean:message key="label.credits.masterDegreeLessons.code" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>,  --%>
		
		<td><bean:write name="creditLineDTO" property="supportLessonHours"/>h</td>
		<td><bean:write name="creditLineDTO" property="tfcAdviseCredits"/></td>
		<td><bean:write name="creditLineDTO" property="institutionWorkingHours"/>h</td>
		<td><bean:write name="creditLineDTO" property="otherCredits"/></td>
		<td><bean:write name="creditLineDTO" property="managementCredits"/></td>
		<td><bean:write name="creditLineDTO" property="serviceExemptionCredits"/></td>
			
</logic:present>

