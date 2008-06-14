<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="link.grant.contract.delete.correction"/></h2>

<html:form action="/correctGrantContract">

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
		<html:errors/>
	</logic:messagesPresent>    

<div class="infoop2">
	<p><strong><bean:message key="info.grant.correct.grantcontract.delete.title"/></strong></p>
	<p><bean:message key="info.grant.correct.grantcontract.delete.numbolseiro"/>(<bean:message key="label.grant.owner.number"/>)</p>
	<p><bean:message key="info.grant.correct.grantcontract.delete.numcontrato"/>(<bean:message key="label.grant.contract.number"/> de contrato)</p>
</div>


	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action" value="deleteContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<%-- Delete Grant Owner --%> 
	<table class="tstyle5 thlight thright thmiddle">
		<tr>
			<td><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber" size="5"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.grant.contract.number"/> de contrato:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantContractNumber" property="grantContractNumber" size="5"/></td>
		</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.correct"/>
		</html:submit>
	</p>
	
	<logic:present name="correctionNumber2">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p><br/>
	</logic:present>

	
</html:form>
