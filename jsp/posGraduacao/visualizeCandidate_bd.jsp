<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.State" %>
<%@ page import="Util.Data" %>
<%@ page import="java.util.Date" %>
<%@ page import="DataBeans.InfoCandidateSituation" %>
<bean:define id="personalInfo" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="session" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="session"/>
<br />
<table>
        <logic:present name="personalInfo">
          <!-- Nome -->
          <tr>
            <td><bean:message key="label.person.name" /></td>
            <td><bean:write name="personalInfo" property="nome"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td><bean:message key="label.person.username" /></td>
            <td><bean:write name="personalInfo" property="username"/></td>
          </tr>
		  <!-- Application Number -->
          <tr>
            <td><bean:message key="label.candidate.candidateNumber" />:</td>
            <td><bean:write name="masterDegreeCandidate" property="candidateNumber"/></td>
          </tr>
          <!-- Dados Pessoais -->
          <tr>
            <td class="infoop"><b><bean:message key="label.person.title.personal.info" /></b></td>
          </tr>  
          <!-- Sexo -->
          <tr>
            <td><bean:message key="label.person.sex" /></td>
            <td><bean:write name="personalInfo" property="sexo"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentNumber" /></td>
            <td><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentType" /></td>
            <td><bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
            <td><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentIssueDate" /></td>
            <logic:present name="personalInfo" property="dataEmissaoDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataEmissaoDocumentoIdentificacao" />
				<td><%= Data.format2DayMonthYear((Date) date) %></td>   
			</logic:present>
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
            <logic:present name="personalInfo" property="dataValidadeDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataValidadeDocumentoIdentificacao" />
				<td><%= Data.format2DayMonthYear((Date) date) %></td> 
			</logic:present>
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td><bean:message key="label.person.contributorNumber" /></td>
            <td><bean:write name="personalInfo" property="numContribuinte"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td><bean:message key="label.person.maritalStatus" /></td>
            <td><bean:write name="personalInfo" property="estadoCivil"/></td>
          </tr>
          <!-- Data de Nascimento -->
          <tr>
            <td><bean:message key="label.person.birth" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
				<td><%= Data.format2DayMonthYear((Date) date) %></td> 
			</logic:present>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td><bean:message key="label.person.country" /></td>
            <td><bean:write name="personalInfo" property="infoPais.nationality"/></td>
          </tr>
          
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td><bean:message key="label.person.birthPlaceParish" /></td>
            <td><bean:write name="personalInfo" property="freguesiaNaturalidade"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
            <td><bean:write name="personalInfo" property="concelhoNaturalidade"/></td>
          </tr>
          <!-- Distrito de Naturalidade -->
          <tr>
            <td><bean:message key="label.person.birthPlaceDistrict" /></td>
            <td><bean:write name="personalInfo" property="distritoNaturalidade"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td><bean:message key="label.person.fatherName" /></td>
            <td><bean:write name="personalInfo" property="nomePai"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td><bean:message key="label.person.motherName" /></td>
            <td><bean:write name="personalInfo" property="nomeMae"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td><bean:message key="label.person.occupation" /></td>
            <td><bean:write name="personalInfo" property="profissao"/></td>
          </tr>
          
          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      
     
          <!-- Dados de Residencia -->
          <tr>
            <td><h2><bean:message key="label.person.title.addressInfo" /><h2></td>
          </tr>
          <!-- Morada -->
          <tr>
            <td><bean:message key="label.person.address" /></td>
            <td><bean:write name="personalInfo" property="morada"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td><bean:message key="label.person.postCode" /></td>
            <td><bean:write name="personalInfo" property="codigoPostal"/></td>
          </tr>
          <!-- Area do Codigo Postal -->
          <tr>
            <td><bean:message key="label.person.areaOfPostCode" /></td>
            <td><bean:write name="personalInfo" property="localidadeCodigoPostal"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td><bean:message key="label.person.place" /></td>
            <td><bean:write name="personalInfo" property="localidade"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td><bean:message key="label.person.addressParish" /></td>
            <td><bean:write name="personalInfo" property="freguesiaMorada"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td><bean:message key="label.person.addressMunicipality" /></td>
            <td><bean:write name="personalInfo" property="concelhoMorada"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td><bean:message key="label.person.addressDistrict" /></td>
            <td><bean:write name="personalInfo" property="distritoMorada"/></td>
          </tr>

          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      

          <!-- Contactos -->
          <tr>
            <td><h2><bean:message key="label.person.title.contactInfo" /><h2></td>
          </tr>
          <!-- Telefone -->
          <tr>
            <td><bean:message key="label.person.telephone" /></td>
            <td><bean:write name="personalInfo" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td><bean:message key="label.person.mobilePhone" /></td>
            <td><bean:write name="personalInfo" property="telemovel"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
            <td><bean:write name="personalInfo" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><bean:write name="personalInfo" property="enderecoWeb"/></td>
          </tr>
          
          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      

   	      <!-- Informacao de Licenciatura -->
          <tr>
            <td><h2><bean:message key="label.candidate.majorDegreeInfo" /><h2></td>
          </tr>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegree" /></td>
            <td><bean:write name="masterDegreeCandidate" property="majorDegree"/></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeYear" /></td>
            <td><bean:write name="masterDegreeCandidate" property="majorDegreeYear"/></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeSchool" /></td>
            <td><bean:write name="masterDegreeCandidate" property="majorDegreeSchool"/></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="label.candidate.average" /></td>
            <td><bean:write name="masterDegreeCandidate" property="average"/> <bean:message key="label.candidate.values"/></td>
          </tr>

          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      

          <tr>
            <td><h2><bean:message key="label.masterDegree.administrativeOffice.situationHistory" /><h2></td>
          </tr>

         <logic:iterate id="situation" name="masterDegreeCandidate" property="situationList">
         	<% if (((InfoCandidateSituation) situation).getValidation().equals(new State(State.ACTIVE))) { %>
         		<td><center><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></center></td>
         	<% } %>
           <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
            <td><bean:write name="situation" property="situation"/></td>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
            <bean:define id="date" name="situation" property="date" />
			<td><%= Data.format2DayMonthYear((Date) date) %></td>             
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
            <td><bean:write name="situation" property="remarks"/></td>
		   </tr>
          <tr></tr>
          <tr></tr>
          <tr></tr>

         </logic:iterate>

        </logic:present>
     </table>

    <html:link page="/editCandidate.do?method=prepareEdit">
    	<bean:message key="link.masterDegree.administrativeOffice.editCandidate" />
    </html:link>
