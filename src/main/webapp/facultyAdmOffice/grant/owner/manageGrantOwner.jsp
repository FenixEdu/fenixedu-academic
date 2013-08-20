<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.management"/></h2>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>


<logic:messagesNotPresent>

<table class="tstyle1 mbottom05">
	<tr>
		<td align="center" colspan="2"><b><bean:message key="label.grant.owner.information"/></b></td>
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

<table class="tstyle1 mbottom05">
	<tr><td align="center" colspan="2" ><b><bean:message key="label.grant.owner.infoperson.idinformation"/></b></td></tr>
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
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idLocation"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.localEmissaoDocumentoIdentificacao">
				<bean:write name="infoGrantOwner" property="personInfo.localEmissaoDocumentoIdentificacao"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idDate"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.dataEmissaoDocumentoIdentificacao">
				<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantOwner" property="personInfo.dataEmissaoDocumentoIdentificacao.time"/>
				</dt:format>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.idValidDate"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.dataValidadeDocumentoIdentificacao">
				<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantOwner" property="personInfo.dataValidadeDocumentoIdentificacao.time"/>
				</dt:format>
			</logic:present>
		</td>
	</tr>
</table>

<br/>

<table class="tstyle1 mbottom05">
	<tr>
		<td align="center" colspan="2" ><b><bean:message key="label.grant.owner.personalinformation"/></b></td>
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
			<bean:message key="label.grant.owner.infoperson.sex"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.sexo">
				<bean:define id="gender" name="infoGrantOwner" property="personInfo.sexo"/>
				<bean:message key="<%= "label." + gender %>"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.maritalStatus">
				<bean:define id="maritalStatus" name="infoGrantOwner" property="personInfo.maritalStatus"/>
				<bean:message key="<%= "label." + maritalStatus %>"/>
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
			<bean:message key="label.grant.owner.infoperson.fatherName"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nomePai">
				<bean:write name="infoGrantOwner" property="personInfo.nomePai"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.motherName"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nomeMae">
				<bean:write name="infoGrantOwner" property="personInfo.nomeMae"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtBirth"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.distritoNaturalidade">
				<bean:write name="infoGrantOwner" property="personInfo.distritoNaturalidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.freguesiaNaturalidade">
				<bean:write name="infoGrantOwner" property="personInfo.freguesiaNaturalidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.concelhoNaturalidade">
				<bean:write name="infoGrantOwner" property="personInfo.concelhoNaturalidade"/>
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
			<bean:message key="label.grant.owner.infoperson.areaOfAreaCode"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.localidadeCodigoPostal">
				<bean:write name="infoGrantOwner" property="personInfo.localidadeCodigoPostal"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressParish"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.freguesiaMorada">
				<bean:write name="infoGrantOwner" property="personInfo.freguesiaMorada"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.concelhoMorada">
				<bean:write name="infoGrantOwner" property="personInfo.concelhoMorada"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.addressDistrict"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.distritoMorada">
				<bean:write name="infoGrantOwner" property="personInfo.distritoMorada"/>
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
			<bean:message key="label.grant.owner.infoperson.workphone"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.workPhone">
				<bean:write name="infoGrantOwner" property="personInfo.workPhone"/>
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
			<bean:message key="label.grant.owner.infoperson.homepage"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.enderecoWeb">
				<bean:write name="infoGrantOwner" property="personInfo.enderecoWeb"/>
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
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.codigoFiscal">
				<bean:write name="infoGrantOwner" property="personInfo.codigoFiscal"/>
			</logic:present>
		</td>
	</tr>
</table> 

<br/>


<bean:define id="idGrantOwner" name="infoGrantOwner" property="externalId"/>
<bean:define id="personExternalId" name="infoGrantOwner" property="personInfo.person.externalId"/>

	<%-- Editar Bolseiro 
	
	<html:link page='<%= "/findPerson.do?method=viewPerson&amp;personID=" +  personExternalId.toString() %>' > 
		<bean:message key="button.edit" />
	</html:link>--%>
<br/>
<%-- 
<html:form action="/editGrantOwner">

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
--%>

	<html:link page='<%= "/listGrantOwner.do?method=showGrantOwner&amp;grantOwnerId=" + idGrantOwner.toString() %>' > 
       	<bean:message key="link.grant.owner.show" />
	</html:link>    
<br/>

<%-- Gerir contractos --%>
<%-- <strong><p align='center'><bean:message key="label.grant.contract.manage"/></p></strong> --%>
<bean:message key="message.grant.contract.manage" />:&nbsp;
<html:link page='<%= "/manageGrantContract.do?method=prepareManageGrantContractForm&amp;externalId=" +  idGrantOwner.toString() %>' > 
	<bean:message key="link.manage.grant.contract" />
</html:link>		
	
<br/><br/>

<%-- Gerir qualificacoes --%>
<%-- <strong><p align='center'><bean:message key="label.grant.qualification.manage"/></p></strong> --%>
<bean:message key="message.grant.qualification.manage" />:&nbsp;
<bean:define id="username" name="infoGrantOwner" property="personInfo.username"/>
<bean:define id="idPerson" name="infoGrantOwner" property="personInfo.externalId"/>
<bean:define id="grantOwnerNumber" name="infoGrantOwner" property="grantOwnerNumber"/>
<html:link page='<%= "/manageGrantQualification.do?method=prepareManageGrantQualificationForm&amp;externalId=" +  idGrantOwner.toString() + "&amp;idPerson=" + idPerson.toString() + "&amp;username=" + username.toString() + "&amp;grantOwnerNumber=" + grantOwnerNumber.toString()%>' > 
	<bean:message key="link.manage.grant.qualification" />
</html:link>		

</logic:messagesNotPresent>

