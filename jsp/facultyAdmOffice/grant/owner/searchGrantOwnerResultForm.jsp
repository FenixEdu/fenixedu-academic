<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.apache.struts.util.RequestUtils" %>

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
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>
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
			<td class="listClasses">
				<logic:present name="infoGrantOwner" property="grantOwnerNumber">
					<bean:write name="infoGrantOwner" property="grantOwnerNumber"/>
				</logic:present>
				<logic:notPresent name="infoGrantOwner" property="grantOwnerNumber">
					---
				</logic:notPresent>
			</td>
			<td class="listClasses">
				<bean:write name="infoGrantOwner" property="personInfo.numContribuinte"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
			</td>
			<td class="listClasses">
				<%-- Person is a Grant Owner already --%>
				<logic:present name="infoGrantOwner" property="grantOwnerNumber">
					<bean:define id="idInternal" name="infoGrantOwner" property="idInternal"/>
					<html:link page='<%= "/manageGrantOwner.do?method=prepareManageGrantOwnerForm&amp;idInternal=" + idInternal %>' > 
						<bean:message key="label.grant.owner.edit" />
					</html:link>
				</logic:present>
				<%-- Person is not a Grant Owner --%>
				<logic:notPresent name="infoGrantOwner" property="grantOwnerNumber">
					<bean:define id="personUsername" name="infoGrantOwner" property="personInfo.username"/>
					<html:link page='<%= "/editGrantOwner.do?method=prepareEditGrantOwnerForm&personUsername=" + personUsername %>' >
						<bean:message key="label.grant.owner.create" />
					</html:link>
				</logic:notPresent>
			</td>
		</tr>
	</logic:iterate>
</table>

<br/>

<table class="listClasses" align="center">
	<tr>
		<td>
			Caso queira criar uma nova pessoa bolseira clique neste botão: 
			<!-- Button to create a new Person Grant Owner -->
			<html:form action="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
				<html:submit styleClass="inputbutton">
					<bean:message key="button.createPersonGrantOwner"/>
				</html:submit>		
			</html:form>	
		</td>
	</tr>
</table>