<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<span class="error"><html:errors/></span>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.defineInsuranceValue" /></h2>

<html:form action="/editInsuranceValue" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readInsuranceValue"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.insuranceValue" property="insuranceValue" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<table>
		<tr>
			<td><bean:message key="label.executionYear" />:&nbsp;</td>
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear">
				<html:options collection="executionYears" property="value" labelProperty="label" />
			</html:select></td>
		</tr>
		<tr>
			<td colspan="2">
			&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.continue"/> 
				</html:submit>
			</td>
		</tr>
	</table>
</html:form>