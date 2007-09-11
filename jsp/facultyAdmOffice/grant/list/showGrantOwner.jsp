<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.visualize"/></h2>

<%-- Presenting errors --%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%-- GRANT OWNER INFORMATION --%>
<h3 class="mtop15 separator2 mbottom05"><bean:message key="label.grant.owner.information"/></h3>
<table class="tstyle2 mtop05">
	<tr>
		<td>
			<bean:message key="label.grant.owner.number"/>:
		</td>
		<td>
			<bean:write name="infoGrantOwner" property="grantOwnerNumber"/>
		</td>
	</tr>
  	<tr>
		<td>
			<bean:message key="label.grant.owner.dateSendCGD"/>:
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
			<bean:message key="label.grant.owner.cardCopyNumber"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="cardCopyNumber">
				<bean:write name="infoGrantOwner" property="cardCopyNumber"/>
			</logic:present>
		</td>
	</tr>
</table>


<h3 class="mtop15 separator2 mbottom05"><bean:message key="label.grant.owner.infoperson.idinformation"/></h3>
<table class="tstyle2 mtop05">
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idNumber"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao">
				<bean:write name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idType"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao">
				<bean:define id="idType" name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
				<bean:message key='<%=idType.toString()%>'/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idLocation"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.localEmissaoDocumentoIdentificacao">
				<bean:write name="infoGrantOwner" property="personInfo.localEmissaoDocumentoIdentificacao"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.idDate"/>:
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
		<td>
			<bean:message key="label.grant.owner.infoperson.idValidDate"/>:
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

<h3 class="mtop15 separator2 mbottom05"><bean:message key="label.grant.owner.personalinformation"/></h3>
<table class="tstyle2 mtop05">
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.name"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nome">
				<bean:write name="infoGrantOwner" property="personInfo.nome"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.sex"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.sexo">
				<bean:write name="infoGrantOwner" property="personInfo.sexo"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.maritalStatus"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.maritalStatus">
				<bean:define id="maritalStatus" name="infoGrantOwner" property="personInfo.maritalStatus"/>
				<bean:message key='<%= maritalStatus.toString() %>'/>
			</logic:present>
		</td>
	</tr> 
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.birthdate"/>:
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
		<td>
			<bean:message key="label.grant.owner.infoperson.fatherName"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nomePai">
				<bean:write name="infoGrantOwner" property="personInfo.nomePai"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.motherName"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nomeMae">
				<bean:write name="infoGrantOwner" property="personInfo.nomeMae"/>
			</logic:present>
		</td>
	</tr>	
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.districtBirth"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.distritoNaturalidade">
				<bean:write name="infoGrantOwner" property="personInfo.distritoNaturalidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.parishOfBirth"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.freguesiaNaturalidade">
				<bean:write name="infoGrantOwner" property="personInfo.freguesiaNaturalidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.districtSubBirth"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.concelhoNaturalidade">
				<bean:write name="infoGrantOwner" property="personInfo.concelhoNaturalidade"/>
			</logic:present>
		</td>			
	</tr> 
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.nationality"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.nacionalidade">
				<bean:write name="infoGrantOwner" property="personInfo.nacionalidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.country"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.infoPais">
				<bean:write name="infoGrantOwner" property="personInfo.infoPais.name"/>
			</logic:present>
		</td>
	</tr> 
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.address"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.morada">
				<bean:write name="infoGrantOwner" property="personInfo.morada"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.area"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.localidade">
				<bean:write name="infoGrantOwner" property="personInfo.localidade"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.areaCode"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.codigoPostal">
				<bean:write name="infoGrantOwner" property="personInfo.codigoPostal"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.areaOfAreaCode"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.localidadeCodigoPostal">
				<bean:write name="infoGrantOwner" property="personInfo.localidadeCodigoPostal"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.addressParish"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.freguesiaMorada">
				<bean:write name="infoGrantOwner" property="personInfo.freguesiaMorada"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.addressDistrictSub"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.concelhoMorada">
				<bean:write name="infoGrantOwner" property="personInfo.concelhoMorada"/>
			</logic:present>
		</td>
	</tr> 
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.addressDistrict"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.distritoMorada">
				<bean:write name="infoGrantOwner" property="personInfo.distritoMorada"/>
			</logic:present>
		</td>
	</tr> 
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.phone"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.telefone">
				<bean:write name="infoGrantOwner" property="personInfo.telefone"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.cellPhone"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.telemovel">
				<bean:write name="infoGrantOwner" property="personInfo.telemovel"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.email"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.email">
				<bean:write name="infoGrantOwner" property="personInfo.email"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.homepage"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.enderecoWeb">
				<bean:write name="infoGrantOwner" property="personInfo.enderecoWeb"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.numContribuinte">
				<bean:write name="infoGrantOwner" property="personInfo.numContribuinte"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.profession"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.profissao">
				<bean:write name="infoGrantOwner" property="personInfo.profissao"/>
			</logic:present>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.grant.owner.infoperson.fiscalCode"/>:
		</td>
		<td>
			<logic:present name="infoGrantOwner" property="personInfo.codigoFiscal">
				<bean:write name="infoGrantOwner" property="personInfo.codigoFiscal"/>
			</logic:present>
		</td>
	</tr>
</table> 

<%-- QUALIFICATIONS INFORMATION --%>

<h3 class="mtop15 separator2 mbottom05"><bean:message key="label.grant.qualification.information"/></h3>
<logic:present name="infoQualificationList">
    <logic:iterate id="infoGrantQualification" name="infoQualificationList">
    	<table class="tstyle2 mtop05">
		<tr>
			<td>
				<bean:message key="label.grant.qualification.degree"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="degree">
				<bean:write name="infoGrantQualification" property="degree"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.title"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="title">
				<bean:write name="infoGrantQualification" property="title"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.school"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="school">
				<bean:write name="infoGrantQualification" property="school"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.mark"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="mark">
				<bean:write name="infoGrantQualification" property="mark"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.qualificationDate"/>:
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
			<td>
				<bean:message key="label.grant.qualification.branch"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="branch">
				<bean:write name="infoGrantQualification" property="branch"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.specializationArea"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="specializationArea">
				<bean:write name="infoGrantQualification" property="specializationArea"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.degreeRecognition"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="degreeRecognition">
				<bean:write name="infoGrantQualification" property="degreeRecognition"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.country"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="infoCountry">
				<bean:write name="infoGrantQualification" property="infoCountry.name"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.equivalenceSchool"/>:
			</td>
			<td>
				<logic:present name="infoGrantQualification" property="equivalenceSchool">
				<bean:write name="infoGrantQualification" property="equivalenceSchool"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.qualification.equivalenceDate"/>:
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
	<p>
		<em><bean:message key="message.grant.qualification.nonExistentContracts"/></em>
	</p>
</logic:notPresent>




<%-- CONTRACT INFORMATION --%>

<h3 class="mtop15 separator2 mbottom05"><b><bean:message key="label.grant.contract.information"/></b></h3>
<logic:present name="infoListGrantContractList">
    <logic:iterate id="infoListGrantContract" name="infoListGrantContractList">
    
    <%-- Contract --%>
    <logic:present name="infoListGrantContract" property="infoGrantContract">

	<p class="mtop15 mbottom05">
		<span class="highlight6"><b><bean:message key="label.grant.contract.information"/> <bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/></b></span>
	</p>

	<table class="tstyle2 mtop05">
		<tr>
			<td>
				<bean:message key="label.grant.contract.number"/>:
			</td>
			<td>
				<bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/>
			</td>
		</tr>
		<tr>
			<td>
	            <bean:message key="label.grant.contract.state"/>:
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
			<td>
				<bean:message key="label.grant.contract.type"/>:
			<td>
				<bean:write name="infoListGrantContract" property="infoGrantContract.grantTypeInfo.name"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.contract.regime.dateAcceptTerm"/>:
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
			<td>
				<bean:message key="label.grant.contract.endMotive"/>:
			</td>
			<td>
				<logic:present name="infoListGrantContract" property="infoGrantContract.endContractMotive">
					<bean:write name="infoListGrantContract" property="infoGrantContract.endContractMotive"/>
				</logic:present>
			</td>
		</tr>	
	</table>


<div style="padding-left: 4em;">

	<%-- Contract Regime --%>
	<p class="mbottom05">
		<b><bean:message key="label.grant.contract.regime.list.information"/>
		<bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/></b>
	</p>

		
	<logic:iterate id="infoGrantContractRegime" name="infoListGrantContract" property="infoGrantContractRegimes">

	<table class="tstyle2 mtop05">
		<tr>
			<td>
				<bean:message key="label.grant.contract.regime.state"/>:
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
			<td>
				<bean:message key="label.grant.contract.regime.beginDate"/>:
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
			<td>
				<bean:message key="label.grant.contract.regime.endDate"/>:
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
			<td>
				<bean:message key="label.grant.contract.orientationTeacher"/>:
			</td>
			<td>
				<logic:present name="infoGrantContractRegime" property="infoTeacher">
				<bean:write name="infoGrantContractRegime" property="infoTeacher.infoPerson.nome"/>				
				(n.<bean:write name="infoGrantContractRegime" property="infoTeacher.teacherNumber"/>)				
				</logic:present>			
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.contract.regime.dateSendDispatchCC"/>:
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
			<td>
				<bean:message key="label.grant.contract.regime.dateDispatchCC"/>:
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
			<td>
				<bean:message key="label.grant.contract.regime.dateSendDispatchCD"/>:
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
			<td>
				<bean:message key="label.grant.contract.regime.dateDispatchCD"/>:
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

	</logic:iterate>


	<%-- Subsidy --%>
	<p class="mbottom05">
		<b><bean:message key="label.list.grant.contract.subsidies"/>
		<bean:write name="infoListGrantContract" property="infoGrantContract.contractNumber"/></b>
	</p>
	
	<logic:iterate id="infoListGrantSubsidy" name="infoListGrantContract" property="infoListGrantSubsidys">

	<table class="tstyle2 mtop05">
		<tr>
			<td>
				<bean:message key="label.grant.subsidy.dateBeginSubsidy"/>:
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
			<td>
				<bean:message key="label.grant.subsidy.dateEndSubsidy"/>:
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
			<td>
				<bean:message key="label.grant.subsidy.state"/>:
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
			<td>
				<bean:message key="label.grant.subsidy.value"/>:
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.value">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.value"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.subsidy.valueFullName"/>:
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.valueFullName">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.valueFullName"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.subsidy.totalCost"/>:
			</td>
			<td>
				<logic:present name="infoListGrantSubsidy" property="infoGrantSubsidy.totalCost">
					<bean:write name="infoListGrantSubsidy" property="infoGrantSubsidy.totalCost"/>				
				</logic:present>				
			</td>
		</tr>			
	</table>


	<%-- Parts --%>
	<p class="mbottom05"><b><bean:message key="label.grant.part.information"/></b></p>	
	
	<logic:iterate id="infoGrantPart" name="infoListGrantSubsidy" property="infoGrantParts">		

	<table class="tstyle2 mtop05">
		<tr>
			<td>
				<bean:message key="label.grant.part.percentage"/>:
			</td>
			<td>
				<logic:present name="infoGrantPart" property="percentage">
					<bean:write name="infoGrantPart" property="percentage"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.part.grantPaymentEntity.designation"/>:
			</td>
			<td>
				<logic:present name="infoGrantPart" property="infoGrantPaymentEntity">
					<bean:write name="infoGrantPart" property="infoGrantPaymentEntity.designation"/>				
				</logic:present>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.part.responsibleTeacher.number"/>:
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

</div>

</logic:present>

</logic:iterate> <%-- Grant Contract --%>



</logic:present>


</logic:messagesNotPresent>