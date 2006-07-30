<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="noGratuityValues">
	<logic:equal name="noGratuityValues" value="true">
		<br /><span class="error"><!-- Error messages go here --><bean:message key="error.impossible.noGratuityValues" /></span>
	</logic:equal>
</logic:present>

<logic:present name="studentCurricularPlan">

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></h2>

<bean:define id="studentCurricularPlanID" name="studentCurricularPlan" property="idInternal" />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="student" /></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.number" />&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.infoPerson.nome" /><br/>
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

<html:form action="/manageExemptionGratuityLA" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanID" property="studentCurricularPlanID" value="<%=pageContext.findAttribute("studentCurricularPlanID").toString()%>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"  value="<%=pageContext.findAttribute("executionYear").toString()%>" />
<logic:present name="gratuitySituationID">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuitySituationID" property="gratuitySituationID" value="<%= ""+request.getAttribute("gratuitySituationID")%>" />
</logic:present>
<logic:present name="gratuityValuesID">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuityValuesID" property="gratuityValuesID" value="<%= ""+request.getAttribute("gratuityValuesID")%>" />
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
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.valueExemptionGratuity" property="valueExemptionGratuity" value="<%= percentage.toString() %>"/>
						</td>
					</tr>
				</logic:iterate>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>
						<bean:message key="label.masterDegree.gratuity.anotherOne" />&nbsp;
					</td>
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.valueExemptionGratuity" property="valueExemptionGratuity" value="-1"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.otherValueExemptionGratuity" property="otherValueExemptionGratuity" size="4"/>%
					</td>
				</tr>	
				<tr><td>&nbsp;</td></tr>
				<tr><td><b><bean:message key="label.or" /></b></td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td colspan="2">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.adHocValueExemptionGratuity" property="adHocValueExemptionGratuity" size="4"/>&euro;
					</td>
				</tr>											
			</table>
		</td>
		<td>
			<!-- JUSTIFICATION OF EXEMPTION -->			
			<table border="0" width='100%'>
				<logic:iterate id="exemptionGratuity" name="exemptionGratuityList">
					<logic:notEqual name="exemptionGratuity" property="name" value="OTHER">
					<tr>						
						<td>	
							<bean:message name="exemptionGratuity" property="name" bundle="ENUMERATION_RESOURCES" />&nbsp;
						</td>
						<td>								
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.justificationExemptionGratuity" property="justificationExemptionGratuity" idName="exemptionGratuity" value="name"/>
						</td>
					</tr>
					</logic:notEqual>
				</logic:iterate>	
				<tr>
					<td>
						<bean:message key="OTHER" bundle="ENUMERATION_RESOURCES" />&nbsp;
					</td>
					<td>																							
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.justificationExemptionGratuity" property="justificationExemptionGratuity" value="OTHER"/>
					</td>
				</tr>	
				<tr>
					<td colspan="2">																							
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.otherJustificationExemptionGratuity" property="otherJustificationExemptionGratuity" size="40"/>
					</td>
				</tr>					
			</table>
		</td>
	</tr>			
	<tr><td  colspan="2" ><br /><br /><br /></td></tr>
	<tr>
		<td valign='middle' align="right">	
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
				<bean:message key="button.masterDegree.gratuity.give" />
			</html:submit>				
			
		</td>
		<td valign='middle' align="left">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton">
				<bean:message key="button.masterDegree.gratuity.remove" />
			</html:submit>				
		</td>
	</tr>
</table>
</html:form>

</logic:present>