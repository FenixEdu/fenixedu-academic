<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="Util.ExemptionGratuityType" %>

<span class="error"><html:errors/></span>

<logic:present name="noGratuityValues">
	<logic:equal name="noGratuityValues" value="true">
		<br /><span class="error"><bean:message key="error.impossible.noGratuityValues" /></span>
	</logic:equal>
</logic:present>

<logic:present name="studentCurricularPlan">

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></h2>

<bean:define id="studentCurricularPlanID" name="studentCurricularPlan" property="idInternal" />
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

<html:form action="/manageExemptionGratuity" >
<html:hidden property="page" value="1" />
<html:hidden property="method" value="insertExemptionGratuity" />
<html:hidden property="studentCurricularPlanID" value="<%=pageContext.findAttribute("studentCurricularPlanID").toString()%>" />
<html:hidden property="executionYear"  value="<%=pageContext.findAttribute("executionYear").toString()%>" />
<logic:present name="gratuitySituationID">
	<html:hidden property="gratuitySituationID" value="<%= ""+request.getAttribute("gratuitySituationID")%>" />
</logic:present>
<logic:present name="gratuityValuesID">
	<html:hidden property="gratuityValuesID" value="<%= ""+request.getAttribute("gratuityValuesID")%>" />
</logic:present>

<table border="0" width='100%'>
	<tr>
		<td valign='middle'><b><bean:message key="label.exemptionGratuity.value" /><p /></b></td>	
		<td valign='middle'><b><bean:message key="label.exemptionGratuity.justification" /><p /></b></td>
	</tr>
	<tr>
		<td valign="top">
			<!-- PERCENTAGE OF EXEMPTION -->
			<table border="0" width='100%'>
				<logic:iterate id="percentage" name="percentageOfExemption">
					<tr>
						<td>
							<bean:write name="percentage" />%&nbsp;
						</td>
						<td>
							<html:radio property="valueExemptionGratuity" value="<%= percentage.toString() %>"/>
						</td>
					</tr>
				</logic:iterate>
				<tr>
					<td>
						<bean:message key="label.masterDegree.gratuity.anotherOne" />&nbsp;
					</td>
					<td>
						<html:radio property="valueExemptionGratuity" value="-1"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<html:text property="otherValueExemptionGratuity" size="4"/>%
					</td>
				</tr>												
			</table>
		</td>
		<td>
			<!-- JUSTIFICATION OF EXEMPTION -->			
			<table border="0" width='100%'>
				<logic:iterate id="exemptionGratuity" name="exemptionGratuityList">
					<logic:notEqual name="exemptionGratuity" property="value" value="10">
					<tr>						
						<td>
							<bean:define id="exemptionGratuityName" name="exemptionGratuity" property="name"/>
							<bean:define id="exemptionGratuityNameKEY" value="<%= "label.exemptionGratuity."+exemptionGratuityName.toString() %>"/>
							<bean:message name="exemptionGratuityNameKEY"/>&nbsp;
						</td>
						<td>								
							<html:radio property="justificationExemptionGratuity" idName="exemptionGratuity" value="value"/>
						</td>
					</tr>
					</logic:notEqual>
				</logic:iterate>	
				<tr>
					<td>
						<bean:message key="label.masterDegree.gratuity.anotherOne" />&nbsp;
					</td>
					<td>																							
						<html:radio property="justificationExemptionGratuity" value="<%= String.valueOf(ExemptionGratuityType.OTHER.getValue()) %>"/>
					</td>
				</tr>	
				<tr>
					<td colspan="2">																							
						<html:text property="otherJustificationExemptionGratuity" size="40"/>
					</td>
				</tr>					
			</table>
		</td>
	</tr>			
	<tr><td  colspan="2" ><br /><br /><br /></td></tr>
	<tr>
		<td colspan="2" valign='middle' align="center">	
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />		
			<html:submit styleClass="inputbutton">
				<bean:message key="button.masterDegree.gratuity.submit" />
			</html:submit>				
			</html:form>
		
			<html:form action="/manageExemptionGratuity" >
				<html:hidden property="method" value="removeExemptionGratuity" />
				<html:hidden property="studentCurricularPlanID" value="<%=pageContext.findAttribute("studentCurricularPlanID").toString()%>" />
				<html:hidden property="executionYear"  value="<%=pageContext.findAttribute("executionYear").toString()%>" />
				<logic:present name="gratuitySituationID">
					<html:hidden property="gratuitySituationID" value="<%= ""+request.getAttribute("gratuitySituationID")%>" />
				</logic:present>
				<bean:define id="onclick">
					return confirm('<bean:message key="message.confirm.delete.gratuitySituation"/>')
				</bean:define>	
				<html:submit styleClass="inputbutton" onclick='<%= onclick.toString() %>'>
					<bean:message key="button.masterDegree.gratuity.removeExemption" />
				</html:submit>
		</td>
	</tr>
</table>
</html:form>

</logic:present>