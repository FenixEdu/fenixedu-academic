<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="link.grant.contract.change.number.correction"/></h2>

<html:form action="/correctGrantContract">

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
		<span class="error"><!-- Error messages go here -->
			<html:errors/>
		</span>
	</logic:messagesPresent>    


<div class="infoop2">
	<p><strong><bean:message key="info.grant.correct.grantcontract.title"/></strong></p>
	<p><bean:message key="info.grant.correct.grantcontract.grantownernumber"/>(<bean:message key="label.grant.owner.number"/>)</p>
	<p><bean:message key="info.grant.correct.grantcontract.contractnumber"/>(<bean:message key="label.grant.contract.number.correction"/>)</p>
	<p><bean:message key="info.grant.correct.grantcontract.newcontractnumber"/>(<bean:message key="label.grant.new.contract.number.correction"/>)</p>
</div>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeNumberContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action" value="changeNumberContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	
	<%-- Change number of Grant Contract --%> 
	<table class="tstyle5 thlight thright">
		<tr>
			<td><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber" size="5"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.grant.contract.number.correction"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantContractNumber" property="grantContractNumber" size="5"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.grant.new.contract.number.correction"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.newGrantContractNumber" property="newGrantContractNumber" size="5"/></td>
		</tr>
	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.correct"/>
	</html:submit></p>
	
	<br/>
	<logic:present name="correctionNumber3">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p><br/>
	</logic:present>
	
</html:form>
