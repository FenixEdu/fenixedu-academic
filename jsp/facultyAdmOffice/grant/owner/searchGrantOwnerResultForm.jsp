<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%-- Present number of hits of search --%>
<bean:size id="resultSize" name="infoGrantOwnerList"/>
<p><bean:message key="label.grant.owner.searchresult" arg0="<%= resultSize.toString() %>"/></p>

<table border="0" cellspacing="1" cellpadding="1">
	<%-- Table description rows --%>
	<tr>
		<td class="listClasses-header" colspan="3">
			<bean:message key="label.grant.owner.personalinformation"/>
		</td>
		<td class="listClasses-header" colspan="2">
			<bean:message key="label.grant.owner.documentidentification"/>
		</td>		
		<td class="listClasses-header">
			<%-- blank --%>
		</td>				
	</tr>
	<tr>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.infoperson.name"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.idInternal"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>
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
	<%-- Table with result of search --%>
	<logic:iterate id="infoGrantOwner" name="infoGrantOwnerList">
		<tr>
			<td class="listClasses">
				<bean:write name="infoGrantOwner" property="personInfo.nome"/>
			</td>
			<td class="listClasses">&nbsp;
				<logic:present name="infoGrantOwner" property="grantOwnerNumber">
					<bean:write name="infoGrantOwner" property="grantOwnerNumber"/>
				</logic:present>
			</td>
			<td class="listClasses">&nbsp;
				<bean:write name="infoGrantOwner" property="personInfo.numContribuinte"/>
			</td>
			<td class="listClasses">&nbsp;
				<bean:write name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao"/>
			</td>
			<td class="listClasses">&nbsp;
				<bean:write name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
			</td>
			<td class="listClasses">&nbsp;
				<%-- Person is a Grant Owner already --%>
				<logic:present name="infoGrantOwner" property="grantOwnerNumber">
					<html:link page="/manageGrantOwner.do?method=prepareManageGrantOwnerForm"
						       paramId="idInternal"
						       paramName="infoGrantOwner"
						       paramProperty="idInternal"> 
						<bean:message key="link.manage.grant.owner" />
					</html:link>
				</logic:present>
				<%-- Person is not a Grant Owner --%>
				<logic:notPresent name="infoGrantOwner" property="grantOwnerNumber">
					<html:link page='<%= "/editGrantOwner.do?method=prepareEditGrantOwnerForm&amp;loaddb=" + 1 %>'
							   paramId="personId"
							   paramName="infoGrantOwner"
							   paramProperty="personInfo.idInternal">
						<bean:message key="label.grant.owner.create" />
					</html:link>
				</logic:notPresent>
			</td>
		</tr>
	</logic:iterate>
</table>

<br/><br/>

<bean:message key="message.grant.owner.creation"/>:&nbsp;
<html:link page="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
	<bean:message key="label.grant.owner.create"/>
</html:link>
