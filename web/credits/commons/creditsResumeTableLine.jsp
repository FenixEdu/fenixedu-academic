<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tiles:importAttribute name="creditLineDTO" ignore="true"/>
<logic:present name="creditLineDTO">

		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="teachingDegreeCredits"/>
			</fmt:formatNumber>
		</td>			
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="supportLessonHours"/>
			</fmt:formatNumber>h
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="masterDegreeCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="tfcAdviseCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="thesesCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="institutionWorkingHours"/>
			</fmt:formatNumber>h				
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="otherCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="managementCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="serviceExemptionCredits"/>
			</fmt:formatNumber>
		</td>
			
</logic:present>

