<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<table>
	<tr>
		<td colspan="2"><b><bean:message key="label.grant.owner.information"/></b></td>
	</tr>
  	<tr>
		<td >
			<bean:message key="label.grant.owner.datesendcgd"/>:&nbsp;
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
			<bean:message key="label.grant.owner.cardcopynumber"/>:&nbsp;
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
				<bean:write name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
			</logic:present>
		</td>
	</tr>
<%--	
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
--%>
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
<%--	
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.sex"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.sexo">
				<bean:write name="infoGrantOwner" property="personInfo.sexo"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.estadoCivil">
				<bean:write name="infoGrantOwner" property="personInfo.estadoCivil"/>
			</logic:present>
		</td>
	</tr> 
--%>
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
<%--	
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
--%>
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

<%--	
	<tr>
		<td align="left">
			<bean:message key="label.grant.owner.infoperson.country"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.infoPais">
				<bean:write name="infoGrantOwner" property="personInfo.infoPais.name"/>
			</logic:present>
		</td>
	</tr> 
--%>
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

<%--	
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
--%>

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

<%--
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
--%>

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

<%--	
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
--%>
</table> 

<br/>

<html:form action="/editGrantOwner">
	<bean:define id="personUsername" name="infoGrantOwner" property="personInfo.username"/>
	<bean:define id="idInternal" name="infoGrantOwner" property="idInternal"/>

	<html:hidden property="method" value="prepareEditGrantOwnerForm"/>				
	<html:hidden property="personUsername" value="<%= personUsername.toString() %>"/>
	<html:hidden property="idInternal" value="<%= idInternal.toString() %>"/>		
	<html:submit styleClass="inputbutton">
		<bean:message key="button.edit"/>
	</html:submit> 
</html:form>

<br/>

<%--  CONTRATOS --%>	
<p><b><bean:message key="label.grant.contract.information"/></b></p>
	
<logic:present name="infoGrantContractList">
	<table border="0" cellspacing="1" cellpadding="1">
	<%-- Table with contract description rows --%>
	<tr>
		<td class="listClasses-header">
			<bean:message key="label.grant.contract.number"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.contract.beginDate"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.contract.endDate"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.contract.type"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.grant.contract.responsibleTeacher"/>
		</td>
		<td class="listClasses-header"></td>
	</tr>	
	<%-- Table with result of search --%>
	<logic:iterate id="infoGrantContract" name="infoGrantContractList">
		<tr>
			<td class="listClasses">
				<bean:write name="infoGrantContract" property="contractNumber"/>
			</td>
			<td class="listClasses">
				<logic:present name="infoGrantContract" property="dateBeginContract">
					<dt:format pattern="yyyy-MM-dd">
						<bean:write name="infoGrantContract" property="dateBeginContract.time"/>
					</dt:format>
				</logic:present>
				<logic:notPresent name="infoGrantContract" property="dateBeginContract.time">
					---
				</logic:notPresent>
			</td>
			<td class="listClasses">
				<logic:present name="infoGrantContract" property="dateEndContract">
					<dt:format pattern="yyyy-MM-dd">
						<bean:write name="infoGrantContract" property="dateEndContract.time"/>
					</dt:format>
				</logic:present>
				<logic:notPresent name="infoGrantContract" property="dateEndContract.time">
					---
				</logic:notPresent>
				
			</td>
			<td class="listClasses">
				<bean:write name="infoGrantContract" property="grantTypeInfo.name"/>
			</td>		
			<td class="listClasses">
				<logic:present name="infoGrantContract" property="grantResponsibleTeacherInfo">
					<bean:write name="infoGrantContract" property="grantResponsibleTeacherInfo.responsibleTeacherInfo.infoPerson.name"/>
				</logic:present>
				<logic:notPresent name="infoGrantContratc" property="grantResponsibleTeacherInfo.responsibleTeacherInfo.infoPerson.name">
					---
				</logic:notPresent>
			</td>
			<td class="listClasses">
					<bean:define id="idContract" name="infoGrantContract" property="idInternal"/>
					<html:link page='<%= "/editGrantContract.do?method=prepareEditGrantContractForm&amp;idContract=" + idContract %>' > 
						<bean:message key="label.grant.contract.edit" />
					</html:link>		
			</td>		
		</tr>
	</logic:iterate>
	</table>
</logic:present>
	
<%-- If there are no contracts --%>
<logic:notPresent name="infoGrantContractList">
	<p align="center">Não existem contractos</p>
</logic:notPresent>
	
<br/>

<html:form action="/editGrantContract">
	<bean:define id="idGrantOwner" name="infoGrantOwner" property="idInternal"/>
	<html:hidden property="method" value="prepareEditGrantContractForm"/>
	<html:hidden property="idGrantOwner" value='<%= idGrantOwner.toString() %>'/>		
	<html:submit styleClass="inputbutton">
		<bean:message key="button.createNewContract"/>
	</html:submit> 
</html:form>