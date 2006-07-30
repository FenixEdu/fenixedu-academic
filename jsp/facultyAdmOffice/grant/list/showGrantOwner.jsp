<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%-- GRANT OWNER INFORMATION --%>
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
			<logic:present name="infoGrantOwner" property="personInfo.maritalStatus">
				<bean:define id="maritalStatus" name="infoGrantOwner" property="personInfo.maritalStatus"/>
				<bean:message key='<%= maritalStatus.toString() %>'/>
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
			<bean:message key="label.grant.owner.infoperson.country"/>:&nbsp;
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.infoPais">
				<bean:write name="infoGrantOwner" property="personInfo.infoPais.name"/>
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
<br/><br/>
<hr/>
<br/>
<%-- QUALIFICATIONS INFORMATION --%>

<b><bean:message key="label.grant.qualification.information"/></b>
<logic:present name="infoQualificationList">
    <logic:iterate id="infoGrantQualification" name="infoQualificationList">
    	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degree"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="degree">
				<bean:write name="infoGrantQualification" property="degree"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.title"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="title">
				<bean:write name="infoGrantQualification" property="title"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.school"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="school">
				<bean:write name="infoGrantQualification" property="school"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.mark"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="mark">
				<bean:write name="infoGrantQualification" property="mark"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.qualificationDate"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="qualificationDate">
				<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantQualification" property="qualificationDate.time"/>
				</dt:format>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.branch"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="branch">
				<bean:write name="infoGrantQualification" property="branch"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.specializationArea"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="specializationArea">
				<bean:write name="infoGrantQualification" property="specializationArea"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degreeRecognition"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="degreeRecognition">
				<bean:write name="infoGrantQualification" property="degreeRecognition"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.country"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="infoCountry">
				<bean:write name="infoGrantQualification" property="infoCountry.name"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceSchool"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="equivalenceSchool">
				<bean:write name="infoGrantQualification" property="equivalenceSchool"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceDate"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="equivalenceDate">
				<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantQualification" property="equivalenceDate.time"/>
				</dt:format>
				</logic:present>
			</td>
		</tr>
	</table>
</logic:iterate>
</logic:present>
<logic:notPresent name="infoQualificationList">
	<br/><bean:message key="message.grant.qualification.nonExistentContracts"/>&nbsp;
</logic:notPresent>
<br/><br/>
<hr/>
<br/>

<%-- CONTRACT INFORMATION --%>

<b><bean:message key="label.grant.contract.information"/></b><br/>
<logic:present name="infoListGrantContractList">
    <logic:iterate id="infoListGrantContract" name="infoListGrantContractList">
	<br/><br/>
    <%-- Contract --%>
    <logic:present name="infoListGrantContract" property="infoGrantContract">
	<table border="1">
	<tr>
		<td>
	<br/><b><bean:message key="label.grant.contract.information"/>&nbsp;<bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/></b>
	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.number"/>:&nbsp;
			</td>
			<td>
				<bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/>
			</td>
		</tr>
		<tr>
			<td align="left">
	            <bean:message key="label.grant.contract.state"/>:&nbsp;
			</td>
			<td>
				<logic:equal name="infoListGrantContract" property="infoGrantContract.contractActive" value="true">
				    <bean:message key="label.grant.contract.state.open"/>
                </logic:equal>
                <logic:equal name="infoListGrantContract" property="infoGrantContract.contractActive" value="false">
				    <bean:message key="label.grant.contract.state.close"/>
                </logic:equal>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.type"/>:&nbsp;
			<td>
				<bean:write name="infoListGrantContract" property="infoGrantContract.grantTypeInfo.name"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateAcceptTerm"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantContract" property="infoGrantContract.dateAcceptTerm">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoListGrantContract" property="infoGrantContract.dateAcceptTerm.time"/>				
					</dt:format>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.endMotive"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantContract" property="infoGrantContract.endContractMotive">
					<bean:write name="infoListGrantContract" property="infoGrantContract.endContractMotive"/>
				</logic:present>
			</td>
		</tr>	
	</table>
	<br/>

	<%-- Contract Regime --%>
	<table>
		<tr>
			<td rowspan="2" width=50>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.grant.contract.regime.list.information"/>&nbsp;
				<bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/></b><br/>
			</td>
		</tr>
	</table>
		
	<logic:iterate id="infoGrantContractRegime" name="infoListGrantContract" property="infoGrantContractRegimes">

	<table>
		<tr>
			<td rowspan="9" width=50>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.state"/>:&nbsp;
			</td>
			<td>
				<logic:equal name="infoGrantContractRegime" property="state" value="1">
				    <bean:message key="label.grant.contract.regime.state.actual"/>
                </logic:equal>
                <logic:equal name="infoGrantContractRegime" property="state" value="0">
				    <bean:message key="label.grant.contract.regime.state.past"/>
                </logic:equal>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.beginDate"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="dateBeginContract">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantContractRegime" property="dateBeginContract.time"/>				
					</dt:format>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.endDate"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="dateEndContract">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantContractRegime" property="dateEndContract.time"/>				
					</dt:format>
				</logic:present>			
 			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="infoTeacher">
				<bean:write name="infoGrantContractRegime" property="infoTeacher.infoPerson.nome"/>				
				&nbsp;(n.<bean:write name="infoGrantContractRegime" property="infoTeacher.teacherNumber"/>)				
				</logic:present>			
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateSendDispatchCC"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="dateSendDispatchCC">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantContractRegime" property="dateSendDispatchCC.time"/>				
					</dt:format>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateDispatchCC"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="dateDispatchCC">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantContractRegime" property="dateDispatchCC.time"/>				
					</dt:format>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateSendDispatchCD"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="dateSendDispatchCD">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantContractRegime" property="dateSendDispatchCD.time"/>				
					</dt:format>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateDispatchCD"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="dateDispatchCD">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoGrantContractRegime" property="dateDispatchCD.time"/>				
					</dt:format>
				</logic:present>
			</td>
		</tr>
	</table>
	<br/><br/>
	</logic:iterate>
	<br/>

	<%-- Subsidy --%>
	<table>
		<tr>
			<td rowspan="2" width=50>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.list.grant.contract.subsidies"/>&nbsp;
			   <bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/></b>
			</td>
		</tr>
	</table>
	
	<logic:iterate id="infoListGrantSubsidy" name="infoListGrantContract" property="infoListGrantSubsidys">

	<table>
		<tr>
			<td rowspan="7" width=50>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.dateBeginSubsidy"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.dateBeginSubsidy">
					<dt:format pattern="yyyy-MM-dd">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.dateBeginSubsidy.time"/>				
					</dt:format>
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.dateEndSubsidy"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.dateEndSubsidy">
					<dt:format pattern="yyyy-MM-dd">
						<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.dateEndSubsidy.time"/>				
					</dt:format>
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.state"/>:&nbsp;
			</td>
			<td>
				<logic:equal name="infoListGrantSubsidy" property="infoGrantSubsidy.state" value="1">
				    <bean:message key="label.grant.subsidy.state.actual"/>
	            </logic:equal>
	            <logic:equal name="infoListGrantSubsidy" property="infoGrantSubsidy.state" value="0">
				    <bean:message key="label.grant.subsidy.state.past"/>
	            </logic:equal>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.value"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.value">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.value"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.valueFullName"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.valueFullName">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.valueFullName"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.subsidy.totalCost"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.totalCost">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.totalCost"/>				
				</logic:present>				
			</td>
		</tr>			
	</table>
	<br/>

	<%-- Parts --%>
	<table>
		<tr>
			<td rowspan="2" width=100>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.grant.part.information"/></b>
			</td>
		</tr>
	</table>
		<logic:iterate id="infoGrantPart" name="infoListGrantSubsidy" property="infoGrantParts">		
	<table>
		<tr>
			<td rowspan="4" width=100>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.part.percentage"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantPart" property="percentage">
					<bean:write name="infoGrantPart" property="percentage"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.part.grantPaymentEntity.designation"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantPart" property="infoGrantPaymentEntity">
					<bean:write name="infoGrantPart" property="infoGrantPaymentEntity.designation"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.part.responsibleTeacher.number"/>:&nbsp;
			</td>
			<td>
				<logic:present name="infoGrantPart" property="infoResponsibleTeacher">
					<bean:write name="infoGrantPart" property="infoResponsibleTeacher.teacherNumber"/>				
				</logic:present>								
			</td>
		</tr>
	</table>	
	</logic:iterate> <%-- Grant Part --%>
	</logic:iterate> <%-- Grant Subsidy --%>
	<br/><br/><br/>
</td>
</tr>
</table>
</logic:present>

  </logic:iterate> <%-- Grant Contract --%>
</table>

</logic:present>


</logic:messagesNotPresent>