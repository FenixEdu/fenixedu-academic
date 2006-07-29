<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.transactions.InsuranceSituationDTO" %>
	
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.studentSituation"/></h2>

<center>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	
	<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
	<bean:define id="gratuitySituationsList" name="<%= SessionConstants.GRATUITY_SITUATIONS_LIST %>" scope="request"/>
	<bean:define id="insuranceSituationsList" name="<%= SessionConstants.INSURANCE_SITUATIONS_LIST %>" scope="request"/>
	
	<table border="0">
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.studentName"/>
			</th>
			<td align="left">
				<bean:write name="student" property="infoPerson.nome"/>
			</td>	
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.studentNumber"/>
			</th>
			<td align="left">
				<bean:write name="student" property="number"/>
			</td>				
		</tr>	
	</table>
	
	<br/><br/>
	
	<h2 align="center"><bean:message key="label.masterDegree.gratuity.gratuities"/></h2>
	
	<table border="0">
		<tr>
			<th><bean:message key="label.executionYear"/></th>
			<th><bean:message key="label.degree"/></th>
			<th><bean:message key="label.masterDegree.gratuity.notPayedValue"/></th>
			<th><bean:message key="label.masterDegree.gratuity.creditValue"/></th>
			<th><td>&nbsp;</td></th>
			<th><td>&nbsp;</td></th>
		</tr>
		
		
		<logic:iterate id="gratuitySituation" name="gratuitySituationsList" >
			<tr>
				<td>
					<bean:write name="gratuitySituation" property="infoGratuityValues.infoExecutionDegree.infoExecutionYear.year"/>
				</td>
				<td>
					<bean:write name="gratuitySituation" property="infoGratuityValues.infoExecutionDegree.infoDegreeCurricularPlan.name"/>
					 - 
					<bean:message name="gratuitySituation" property="infoStudentCurricularPlan.specialization.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
				<bean:define id="value" name="gratuitySituation" property="remainingValue" />
				<% 
					String remainingValue = value.toString();
					Double remainingValueDbl = new Double(remainingValue );
					
					if(remainingValueDbl.doubleValue() >= 0)
					{
									
				%>
					<td><bean:write name="gratuitySituation" property="remainingValue"/></td>
					<td>&nbsp;</td>
				<% 
					}
					else
					{
				%>								
					<td>&nbsp;</td>
					<td><%= remainingValue.substring(1) %></td>
				<% 
					}
				%>
				
				<bean:define id="linkGratuitySituationDetails">
					/gratuitySituationDetails.do?method=show&gratuitySituationId=<bean:write name="gratuitySituation" property="idInternal"/>&studentId=<bean:write name="student" property="idInternal"/>
				</bean:define>

				<td align="center">
					<html:link page="<%= linkGratuitySituationDetails %>" ><bean:message key="link.masterDegree.administrativeOffice.viewDetails"/></html:link>
				</td>
				
				<% 
					if(remainingValueDbl.doubleValue() > 0)
					{									
				%>
				
				<bean:define id="linkPayGratuity">
					/payGratuity.do?method=chooseContributor&gratuitySituationId=<bean:write name="gratuitySituation" property="idInternal"/>&studentId=<bean:write name="student" property="idInternal"/>
				</bean:define>

				<td align="center">
					<html:link page="<%= linkPayGratuity %>" ><bean:message key="link.masterDegree.administrativeOffice.gratuity.pay"/></html:link>
				</td>
				
				<% 
					}
				%>
			</tr>	

		</logic:iterate>
		
	</table>
	
	<br/><br/>
	
	<h2 align="center"><bean:message key="label.masterDegree.gratuity.insurances"/></h2>
	
	<table>
		<tr>
			<th><bean:message key="label.executionYear"/></th>
			<th><bean:message key="label.masterDegree.gratuity.payedValue"/></th>
			<th><bean:message key="label.masterDegree.insurance.annualValue"/></th>
			<th><td>&nbsp;</td></th>
		</tr>
		
		<logic:iterate id="insuranceSituation" name="insuranceSituationsList"  >
			<tr>
				<td>
					<bean:write name="insuranceSituation" property="infoExecutionYear.year"/>
				</td>
				<td>
					<bean:write name="insuranceSituation" property="payedValue"/>
				</td>
				<td>
					<bean:write name="insuranceSituation" property="anualValue"/>
				</td>
				<td>
					<bean:define id="payedValue" name="insuranceSituation" property="payedValue" />
					<logic:greaterThan  name="insuranceSituation" property="anualValue" value="<%= payedValue.toString() %>">
						<bean:define id="linkPayInsurance">
							/payGratuity.do?method=chooseContributor&insuranceExecutionYearId=<bean:write name="insuranceSituation" property="executionYearID"/>&studentId=<bean:write name="student" property="idInternal"/>
						</bean:define>
						<html:link page="<%= linkPayInsurance %>" ><bean:message key="link.masterDegree.administrativeOffice.gratuity.pay"/></html:link>
					</logic:greaterThan >
				</td>
			</tr>
		</logic:iterate>
	</table>
	
</center>