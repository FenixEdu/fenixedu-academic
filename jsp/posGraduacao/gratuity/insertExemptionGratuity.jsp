<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="Util.ExemptionGratuityType" %>


<span class="error"><html:errors/></span>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></h2>

<logic:present name="studentCurricularPlan">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="student" /></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.number" />&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.infoPerson.nome" /><br>
			<b><bean:message key="title.studentCurricularPlan"/></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;
			<bean:define id="initialDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.initialDate" />		
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
			<logic:notEmpty name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
				<bean:define id="endDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate" />	
			-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
			</logic:notEmpty>
		</td>
	</tr>
</table>
<p />
<table width='100%'>
	<tr>
		<td valign='middle'><b><bean:message key="label.exemptionGratuity.value" /><p /></b></td>		
		<td><p /><p /><p /><p /><p /><p /><p /><p /><p /><td>
		<td valign='middle'><b><bean:message key="label.exemptionGratuity.justification" /><p /></b></td>
	</tr>
	<tr>
		<td valign='middle'>
			<!-- PERCENTAGE OF EXEMPTION -->
			<table width='100%'>
				<logic:iterate id="percentage" name="percentageOfExemption">
					<tr>
						<td>
							<bean:write name="percentage" />%&nbsp;
						</td>
					<tr>
				</logic:iterate>
			</table>
		</td>
		<td><p /><p /><p /><p /><p /><p /><p /><p /><p /><td>
		<td>
			<!-- JUSTIFICATION OF EXEMPTION -->			
			<table width='100%'>
				<logic:iterate id="exemptionGratuity" name="exemptionGratuityList">
					<tr>						
						<td>
							<bean:define id="exemptionGratuityName" name="exemptionGratuity" property="name"/>
							<bean:define id="exemptionGratuityNameKEY" value="<%= "label.exemptionGratuity."+exemptionGratuityName.toString() %>"/>
														
							<bean:message name="exemptionGratuityNameKEY"/>&nbsp;
						</td>
					<tr>
				</logic:iterate>			
			</table>
		</td>
	</tr>	
</table>
</logic:present>