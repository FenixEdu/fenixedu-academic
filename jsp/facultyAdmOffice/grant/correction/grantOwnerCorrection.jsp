<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>


<html:form action="/correctGrantOwner">

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
	<p align="center"><span class="error">
	<html:errors/>
	</span></p><br/>
	</logic:messagesPresent>    

	<table class="infoop">
	 <tr>
		<td rowspan=4><p class="emphasis-box">i</p></td>
		<td><bean:message key="info.grant.correct.grantowner.title"/></td>
	 </tr>
	 <tr>
		<td><bean:message key="info.grant.correct.grantowner.grantownernumber"/></td>
	 </tr>
	 <tr>
		<td><bean:message key="info.grant.correct.grantowner.grantpersondocument"/></td>
	</tr>
	 
	 
	</table><br><br>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeAssociatedPerson"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	
	<%-- Correct Grant Owner --%> 
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.number"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber" size="5"/></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td align="left" colspan="2"><b><bean:message key="label.grant.owner.correction"/></b></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.infoperson.idNumber"/>:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.documentIdNumber" property="documentIdNumber" size="15"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.infoperson.idType"/>:&nbsp;</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.documentIdType" property="documentIdType">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.correct"/>
	</html:submit></p>
	<br/>
	<logic:present name="correctionNumber1">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p><br/>
	</logic:present>
	
	
</html:form>
