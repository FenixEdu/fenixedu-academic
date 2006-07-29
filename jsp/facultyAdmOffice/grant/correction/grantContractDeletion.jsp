<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:form action="/correctGrantContract">

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
	<p align="center"><span class="error">
	<html:errors/>
	</span></p><br/>
	</logic:messagesPresent>    

	<table class="infoop">
	 <tr>
		<td rowspan=4><p class="emphasis-box">i</p></td>
	    <td><bean:message key="info.grant.correct.grantcontract.delete.title"/></td>
	 </tr>
	 <tr>
	  	<td><bean:message key="info.grant.correct.grantcontract.delete.numbolseiro"/>(<bean:message key="label.grant.owner.number"/>)</td>
	 </tr>
	 <tr>
	  	<td><bean:message key="info.grant.correct.grantcontract.delete.numcontrato"/>(<bean:message key="label.grant.contract.number"/> de contrato)</td>
	 </tr>
	</table><br/><br/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action" value="deleteContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<%-- Delete Grant Owner --%> 
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber" size="5"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.contract.number"/> de contrato:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantContractNumber" property="grantContractNumber" size="5"/></td>
		</tr>
	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.correct"/>
	</html:submit></p>
	
	<br/>
	<logic:present name="correctionNumber2">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p><br/>
	</logic:present>

	
</html:form>
