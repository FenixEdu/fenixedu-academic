<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="DataBeans.InfoGratuitySituation" %>
<%@ page import="DataBeans.InfoInsuranceValue" %>
<%@ page import="DataBeans.InfoExecutionYear" %>
	
<bean:define id="title" name="<%= SessionConstants.PAGE_TITLE %>" scope="request" type="java.lang.String"/>
	
<h2 align="center"><bean:message key="<%= title %>"/></h2>
<center>
	<span class="error"><html:errors/></span>
	<br/><br/>
	
	<bean:define id="contributor" name="<%= SessionConstants.CONTRIBUTOR %>" scope="request"/>
	<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
			
	<html:form action="/payGratuity.do" >
			
		<table>
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
				<td><bean:write name="student" property="infoPerson.nome"/></td>	
			</tr>
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
				<td><bean:write name="student" property="number"/></td>				
			</tr>	
			
			<tr align="left">
				<td>&nbsp;</td>
			</tr>
			
			<!-- Contributor Number -->
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" />: </th>
				<td><bean:write name="contributor" property="contributorNumber"/></td>
			</tr>
			<!-- Contributor Name -->
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.contributorName" />: </th>
				<td><bean:write name="contributor" property="contributorName"/></td>
			</tr>
			<!-- Contributor Address -->
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" />: </th>
				<td><bean:write name="contributor" property="contributorAddress"/></td>
			</tr>
			
			<tr align="left">
				<td>&nbsp;</td>
			</tr>
			
			<!-- Gratuity Situation -->
			<logic:present name="<%= SessionConstants.GRATUITY_SITUATION %>" scope="request">
				<bean:define id="gratuitySituation" name="<%= SessionConstants.GRATUITY_SITUATION %>" scope="request"/>
				<tr align="left">
					<th><bean:message key="label.executionYear"/>: </th>
					<td><bean:write name="gratuitySituation" property="infoGratuityValues.infoExecutionDegree.infoExecutionYear.year"/></td>
				</tr>
				<tr align="left">
					<th><bean:message key="label.degree"/>: </th>
					<td><bean:write name="gratuitySituation" property="infoGratuityValues.infoExecutionDegree.infoDegreeCurricularPlan.name"/></td>
				</tr>	
				<tr align="left">
					<th><bean:message key="label.masterDegree.gratuity.notPayedValue"/>: </th>
					<td><bean:write name="gratuitySituation" property="remainingValue"/></td>
				</tr>

				<tr align="left">
					<th><bean:message key="label.masterDegree.gratuity.adHocValue"/>: </th>
					<td><html:text property="adHocValue" /></td>
				</tr>
				
					
				<!-- Insurance Value -->
				<logic:present name="<%= SessionConstants.INSURANCE_VALUE_WITH_GRATUITY %>" scope="request">
					<bean:define id="insuranceValueWithGratuity" name="<%= SessionConstants.INSURANCE_VALUE_WITH_GRATUITY %>" scope="request"/>
					<tr align="left"><td>&nbsp;</td></tr>
					<tr align="left">
						<th><bean:message key="label.masterDegree.gratuity.insuranceNotPayedValue"/>: </th>
						<td><bean:write name="insuranceValueWithGratuity" property="annualValue"/></td>
					</tr>	
					<tr align="left">
						<th colspan="2">
							<html:checkbox property="insuranceExecutionYearId" value="<%= ((InfoInsuranceValue)insuranceValueWithGratuity).getInfoExecutionYear().getIdInternal().toString() %>"/>
							<bean:message key="label.masterDegree.gratuity.payInsurance"/>
						</th>
					</tr>
				</logic:present>	
															
			</logic:present>
				
			
			<!-- Insurance Value -->
			<logic:present name="<%= SessionConstants.INSURANCE_VALUE %>" scope="request">
				<bean:define id="insuranceValue" name="<%= SessionConstants.INSURANCE_VALUE %>" scope="request"/>
				<html:hidden property="insuranceExecutionYearId" />
				<tr align="left">
					<th><bean:message key="label.executionYear"/>: </th>
					<td><bean:write name="insuranceValue" property="infoExecutionYear.year"/></td>
				</tr>
				<tr align="left">
					<th><bean:message key="label.masterDegree.gratuity.notPayedValue"/>: </th>
					<td><bean:write name="insuranceValue" property="annualValue"/></td>
				</tr>	
			</logic:present>	
					
			<tr align="left">
				<td>&nbsp;</td>
			</tr>
			
		</table>
	
		<html:hidden property="method" value="pay"/>
		<html:hidden property="gratuitySituationId" />		
		<html:hidden property="studentId" />
		<html:hidden property="contributorNumber" />
		<html:hidden property="page" value="2"/>
		
		<html:submit styleClass="inputbutton">
			<bean:message key="link.masterDegree.administrativeOffice.gratuity.pay"/>
		</html:submit>
	</html:form>
	
</center>