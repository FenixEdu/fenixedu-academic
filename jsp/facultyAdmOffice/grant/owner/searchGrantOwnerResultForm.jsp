<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:size id="resultSize" name="infoGrantOwnerList"/>

<p>
	<bean:message key="label.grant.owner.searchresult" arg0="<%= resultSize.toString() %>"/>
</p>
<table border="0" cellspacing="1" cellpadding="1">

	<!-- Table description rows -->
	<tr>
		<td class="listClasses-header" colspan="3">
			<bean:message key="label.grant.owner.personalinformation"/>
		</td>
		<td class="listClasses-header" colspan="2">
			<bean:message key="label.grant.owner.documentidentification"/>
		</td>		
		<td class="listClasses-header">
		</td>				
	</tr>
	<tr>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.infoperson.name"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.idInternal"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.infoperson.documentId"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.infoperson.documentIdType"/>
		</td>
		<td class="listClasses-header">
		</td>		
		
	</tr>

	<!-- Table with result of search -->
	<logic:iterate id="infoGrantOwner" name="infoGrantOwnerList">
	<tr>
		<td class="listClasses">
			<bean:write name="infoGrantOwner" property="personInfo.nome"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoGrantOwner" property="personInfo.numContribuinte"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoGrantOwner" property="grantOwnerNumber"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
		</td>
		<td class="listClasses">
			<!-- Person is a Grant Owner already -->
			<logic:present name="infoGrantOwner" property="grantOwnerNumber">
				<bean:define id="idInternal" name="infoGrantOwner" property="idInternal"/>
				<html:link page='<%= "/manageGrantOwner.do?method=prepareManageGrantOwnerForm&amp;idInternal=" + idInternal %>' >
					<bean:message key="label.grant.owner.edit" />
				</html:link>
			</logic:present>
			<!-- Person is not a Grant Owner -->
			<logic:notPresent name="infoGrantOwner" property="grantOwnerNumber">
				<bean:define id="personUsername" name="infoGrantOwner" property="personInfo.username"/>
				<html:link page='<%= "/editGrantOwner.do?method=prepareEditGrantOwnerForm&amp;personUsername=" + personUsername %>' >
					<bean:message key="label.grant.owner.create" />
				</html:link>
			</logic:notPresent>
		</td>
	</tr>
	</logic:iterate>
	<!-- End of table with the results -->
</table>
<p>

<!--	<html:link page="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
		<bean:message key="label.grant.owner.createnew" />
	</html:link>-->

	<table align="center">
	<tr>
		<td>
			<!-- Button to create a new Person Grant Owner -->
			<html:form action="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
				<html:submit styleClass="inputbutton">
					<bean:message key="button.search"/>
				</html:submit>		
			</html:form>	
		</td>
		<td>
			<!-- Back Button -->
			<html:form action="/index.do">
				<html:submit styleClass="inputbutton">
					<bean:message key="button.back"/>
				</html:submit>
			</html:form>
		</td>
	</tr>
	</table>
</p>

</p>
