<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<p align="center"><strong><bean:message key="label.grant.owner.management"/></strong></p>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>


<logic:messagesNotPresent>

<table>
	<tr>
		<td colspan="2"><b><bean:message key="label.grant.owner.information"/></b></td>
	</tr>
	<tr>
		<td >
			<bean:message key="label.grant.owner.number"/>:&nbsp;
		</td>
		<td>
			<bean:write name="infoGrantOwner" property="grantOwnerNumber"/>
		</td>
	</tr>
  	<tr>
		<td >
			<bean:message key="label.grant.owner.dateSendCGD"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="dateSendCGD">
				<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantOwner" property="dateSendCGD.time"/>
				</dt:format>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.cardCopyNumber"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="cardCopyNumber">
				<bean:write name="infoGrantOwner" property="cardCopyNumber"/>
			</logic:present>
		</td>
	</tr>
</table>

<br/>

<table>
	<tr><td colspan="2" ><b><bean:message key="label.grant.owner.infoperson.idinformation"/></td></tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idNumber"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao">
				<bean:write name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idType"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao">
				<bean:define id="idType" name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
				<bean:message key='<%=idType.toString()%>'/>
			</logic:present>
		</td>
	</tr>
</table>

<br/>

<table>
	<tr>
		<td colspan="2" ><b><bean:message key="label.grant.owner.personalinformation"/></td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.name"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nome">
				<bean:write name="infoGrantOwner" property="personInfo.nome"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.birthdate"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nascimento">
				<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantOwner" property="personInfo.nascimento.time"/>
				</dt:format>
			</logic:present>
		</td>
	</tr>

	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.nationality"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nacionalidade">
				<bean:write name="infoGrantOwner" property="personInfo.nacionalidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.address"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.morada">
				<bean:write name="infoGrantOwner" property="personInfo.morada"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.area"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.localidade">
				<bean:write name="infoGrantOwner" property="personInfo.localidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.areaCode"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.codigoPostal">
				<bean:write name="infoGrantOwner" property="personInfo.codigoPostal"/>
			</logic:present>
		</td>
	</tr>

	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.phone"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.telefone">
				<bean:write name="infoGrantOwner" property="personInfo.telefone"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.cellPhone"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.telemovel">
				<bean:write name="infoGrantOwner" property="personInfo.telemovel"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.email"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.email">
				<bean:write name="infoGrantOwner" property="personInfo.email"/>
			</logic:present>
		</td>
	</tr>

	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.numContribuinte">
				<bean:write name="infoGrantOwner" property="personInfo.numContribuinte"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.profession"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.profissao">
				<bean:write name="infoGrantOwner" property="personInfo.profissao"/>
			</logic:present>
		</td>
	</tr>

</table> 

<br/>

<html:form action="/editGrantOwner">
	<%-- Editar Bolseiro --%>
	<bean:define id="idGrantOwner" name="infoGrantOwner" property="idInternal"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idGrantOwner" property="idGrantOwner" value="<%= idGrantOwner.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEditGrantOwnerForm"/>				
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.loaddb" property="loaddb" value="1"/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.edit"/>
    </html:submit>
    &nbsp;&nbsp;&nbsp;&nbsp;
  	<html:link page='<%= "/listGrantOwner.do?method=showGrantOwner&amp;grantOwnerId=" + idGrantOwner.toString() %>' > 
       	<bean:message key="link.grant.owner.show" />
	</html:link>    
</html:form>


<br/>

<%-- Gerir contractos --%>
<%-- <strong><p align='center'><bean:message key="label.grant.contract.manage"/></p></strong> --%>
<bean:message key="message.grant.contract.manage" />:&nbsp;
<bean:define id="idGrantOwner" name="infoGrantOwner" property="idInternal"/>
<html:link page='<%= "/manageGrantContract.do?method=prepareManageGrantContractForm&amp;idInternal=" +  idGrantOwner.toString() %>' > 
	<bean:message key="link.manage.grant.contract" />
</html:link>		
	
<br/><br/>

<%-- Gerir qualificacoes --%>
<%-- <strong><p align='center'><bean:message key="label.grant.qualification.manage"/></p></strong> --%>
<bean:message key="message.grant.qualification.manage" />:&nbsp;
<bean:define id="idGrantOwner" name="infoGrantOwner" property="idInternal"/>
<bean:define id="username" name="infoGrantOwner" property="personInfo.username"/>
<bean:define id="idPerson" name="infoGrantOwner" property="personInfo.idInternal"/>
<bean:define id="grantOwnerNumber" name="infoGrantOwner" property="grantOwnerNumber"/>
<html:link page='<%= "/manageGrantQualification.do?method=prepareManageGrantQualificationForm&amp;idInternal=" +  idGrantOwner.toString() + "&amp;idPerson=" + idPerson.toString() + "&amp;username=" + username.toString() + "&amp;grantOwnerNumber=" + grantOwnerNumber.toString()%>' > 
	<bean:message key="link.manage.grant.qualification" />
</html:link>		

</logic:messagesNotPresent>

