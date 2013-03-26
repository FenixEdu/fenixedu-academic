<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="link.grant.owner.correction"/></h2>


<html:form action="/correctGrantOwner">

	<%-- Presenting Errors --%>
	<logic:messagesPresent>
		<span class="error"><!-- Error messages go here -->
			<html:errors/>
		</span>
	</logic:messagesPresent>    

	<div class="infoop2">
		<p><bean:message key="info.grant.correct.grantowner.title"/></p>
		<p><bean:message key="info.grant.correct.grantowner.grantownernumber"/></p>
		<p><bean:message key="info.grant.correct.grantowner.grantpersondocument"/></p>
	</div>


	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeAssociatedPerson"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	
	<%-- Correct Grant Owner --%> 
	<table class="tstyle5">
		<tr>
			<td><bean:message key="label.grant.owner.number"/>:</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.grantOwnerNumber" property="grantOwnerNumber" size="5"/></td>
		</tr>
	</table>


	<p><b><bean:message key="label.grant.owner.correction"/></b></p>

	<table class="tstyle5">
		<tr>
			<td><bean:message key="label.grant.owner.infoperson.idNumber"/>:</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.documentIdNumber" property="documentIdNumber" size="15"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.grant.owner.infoperson.idType"/>:</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.documentIdType" property="documentIdType">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.correct"/>
		</html:submit>
	</p>


	<logic:present name="correctionNumber1">
	    <p><strong><bean:message key="message.grant.correction.successfull"/></strong></p>
	</logic:present>
	
	
</html:form>
