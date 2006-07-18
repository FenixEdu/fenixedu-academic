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
	    <td><bean:message key="info.grant.correct.grantcontract.move.title"/></td>
	 </tr>
	 <tr>
	 	<td><bean:message key="info.grant.correct.grantcontract.move.grantowner"/>(<bean:message key="label.grant.owner.number"/>)</td>
	 </tr>
	 <tr>
	 	<td><bean:message key="info.grant.correct.grantcontract.move.grantcontract"/>(<bean:message key="label.grant.contract.number.correction"/>)</td>
	 </tr>
	 <tr>
	 	<td><bean:message key="info.grant.correct.grantcontract.move.newgrantowner"/>(<bean:message key="label.new.grant.owner"/>)</td>
	 </tr>
	 
	</table><br><br>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="moveContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action" value="moveContract"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	
	<%-- Change number of Grant Contract --%> 
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber" size="5"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.contract.number.correction"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantContractNumber" property="grantContractNumber" size="5"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.new.grant.owner"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.newGrantOwnerNumber" property="newGrantOwnerNumber" size="5"/></td>
		</tr>
	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.correct"/>
	</html:submit></p>

	<br/>	
    <logic:present name="correctionNumber4">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p><br/>
	</logic:present>

</html:form>
