<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue" %>
	
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
            <th><bean:message key="label.person.postCode" /></th>
            <td><bean:write name="contributor" property="areaCode"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.areaOfPostCode" /></th>
            <td><bean:write name="contributor" property="areaOfAreaCode"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.place" /></th>
            <td><bean:write name="contributor" property="area"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.addressParish" /></th>
            <td><bean:write name="contributor" property="parishOfResidence"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.addressMunicipality" /></th>
            <td><bean:write name="contributor" property="districtSubdivisionOfResidence"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.addressDistrict" /></th>
            <td><bean:write name="contributor" property="districtOfResidence"/></td>
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
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.adHocValue" property="adHocValue" /></td>
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
							<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.insuranceExecutionYearId" property="insuranceExecutionYearId" value="<%= ((InfoInsuranceValue)insuranceValueWithGratuity).getInfoExecutionYear().getIdInternal().toString() %>"/>
							<bean:message key="label.masterDegree.gratuity.payInsurance"/>
						</th>
					</tr>
				</logic:present>	
															
			</logic:present>
				
			
			<!-- Insurance Value -->
			<logic:present name="<%= SessionConstants.INSURANCE_VALUE %>" scope="request">
				<bean:define id="insuranceValue" name="<%= SessionConstants.INSURANCE_VALUE %>" scope="request"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.insuranceExecutionYearId" property="insuranceExecutionYearId" />
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
	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="pay"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuitySituationId" property="gratuitySituationId" />		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contributorNumber" property="contributorNumber" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="link.masterDegree.administrativeOffice.gratuity.pay"/>
		</html:submit>
	</html:form>
	
</center>