<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
  <head>
    <title><bean:message key="candidate.titleVisualizeApplicationInfo" /></title>
  </head>
  <body>
    <table>
    
        <logic:present name="applicationInfo">
          <!-- Nome -->
          <tr>
            <td><bean:message key="candidate.name" /></td>
            <td><bean:write name="applicationInfo" property="name"/></td>
          </tr>
          <!-- Username -->
          <tr>
            <td><bean:message key="candidate.username" /></td>
            <td><bean:write name="applicationInfo" property="username"/></td>
          </tr>

          <!-- Situacao da Candidatura -->
    	  <tr>
            <td><h2><bean:message key="candidate.applicationInfoSituation" /></h2></td>
          </tr>

          <!-- Situacao -->
          <tr>
            <td><bean:message key="candidate.infoCandidateSituation" /></td>
            <td><bean:write name="applicationInfo" property="infoCandidateSituation.situation"/></td>
          </tr>

          <!-- Data da Situacao -->
          <tr>
            <td><bean:message key="candidate.infoCandidateSituationDate" /></td>
            <td><bean:write name="applicationInfo" property="infoCandidateSituation.date"/></td>
          </tr>

          <!-- Observacoes -->
          <tr>
            <td><bean:message key="candidate.infoCandidateSituationRemarks" /></td>
            <td><bean:write name="applicationInfo" property="infoCandidateSituation.remarks"/></td>
          </tr>



          <!-- Informacao de Candidatura -->
    	  <tr>
            <td><h2><bean:message key="candidate.applicationInfo" /></h2></td>
          </tr>
          <!-- Numero de Candidatura -->
          <tr>
            <td><bean:message key="candidate.candidateNumber" /></td>
            <td><bean:write name="applicationInfo" property="candidateNumber"/></td>
          </tr>
          <!-- Ano de Candidatura -->
          <tr>
            <td><bean:message key="candidate.applicationYear" /></td>
            <td><bean:write name="applicationInfo" property="applicationYear"/></td>
          </tr>
          <!-- Curso Pretendido -->
          <tr>
            <td><bean:message key="candidate.degreeName" /></td>
            <td><bean:write name="applicationInfo" property="degreeName"/> - <bean:write name="applicationInfo" property="degreeCode"/></td>
          </tr>
          <!-- Especializacao -->
          <tr>
            <td><bean:message key="candidate.specialization" /></td>
            <td><bean:write name="applicationInfo" property="specialization"/></td>
          </tr>



	 <!-- Informacao de Licenciatura -->
          <tr>
            <td><h2><bean:message key="candidate.majorDegreeInfo" /><h2></td>
          </tr>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="candidate.majorDegree" /></td>
            <td><bean:write name="applicationInfo" property="majorDegree"/></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="candidate.majorDegreeYear" /></td>
            <td><bean:write name="applicationInfo" property="majorDegreeYear"/></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="candidate.majorDegreeSchool" /></td>
            <td><bean:write name="applicationInfo" property="majorDegreeSchool"/></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="candidate.average" /></td>
            <td><bean:write name="applicationInfo" property="average"/> <bean:message key="candidate.values"/></td>
          </tr>
          
          
          
          <!-- Dados Pessoais -->
          <tr>
            <td><h2><bean:message key="candidate.personalInfo" /><h2></td>
          </tr>
          <!-- Sexo -->
          <tr>
            <td><bean:message key="candidate.sex" /></td>
            <td><bean:write name="applicationInfo" property="sex"/></td>
          </tr>
 	      <!-- Numero do Documento de Identificacao -->
          <tr>
            <td><bean:message key="candidate.identificationDocumentNumber" /></td>
            <td><bean:write name="applicationInfo" property="identificationDocumentNumber"/></td>
          </tr>
          <!-- Tipo do Documento de Identificacao -->
          <tr>
            <td><bean:message key="candidate.identificationDocumentType" /></td>
            <td><bean:write name="applicationInfo" property="identificationDocumentType"/></td>
          </tr>
          <!-- Local de Emissao do Documento de Identificacao -->
          <tr>
            <td><bean:message key="candidate.identificationDocumentIssuePlace" /></td>
            <td><bean:write name="applicationInfo" property="identificationDocumentIssuePlace"/></td>
          </tr>
          <!-- Data de Emissao do Documento de Identificacao -->
          <tr>
            <td><bean:message key="candidate.identificationDocumentIssueDate" /></td>
            <td><bean:write name="applicationInfo" property="identificationDocumentIssueDate"/></td>
          </tr>
          <!-- Numero de Contribuinte -->
          <tr>
            <td><bean:message key="candidate.contributorNumber" /></td>
            <td><bean:write name="applicationInfo" property="contributorNumber"/></td>
          </tr>
          <!-- Estado Civil -->
          <tr>
            <td><bean:message key="candidate.maritalStatus" /></td>
            <td><bean:write name="applicationInfo" property="maritalStatus"/></td>
          </tr>
          <!-- Data de Nascimento -->
          <tr>
            <td><bean:message key="candidate.birth" /></td>
            <td><bean:write name="applicationInfo" property="birth"/></td>
          </tr>
          <!-- Nacionalidade -->
          <tr>
            <td><bean:message key="candidate.nationality" /></td>
            <td><bean:write name="applicationInfo" property="nationality"/></td>
          </tr>
          <!-- Naturalidade -->
          <tr>
            <td><bean:message key="candidate.country" /></td>
            <td><bean:write name="applicationInfo" property="country"/></td>
          </tr>
          
          <!-- Freguesia de Naturalidade -->
          <tr>
            <td><bean:message key="candidate.birthPlaceParish" /></td>
            <td><bean:write name="applicationInfo" property="birthPlaceParish"/></td>
          </tr>
          <!-- Concelho de Naturalidade -->
          <tr>
            <td><bean:message key="candidate.birthPlaceMunicipality" /></td>
            <td><bean:write name="applicationInfo" property="birthPlaceMunicipality"/></td>
          </tr>
          <!-- Distrito de Naturalidade -->
          <tr>
            <td><bean:message key="candidate.birthPlaceDistrict" /></td>
            <td><bean:write name="applicationInfo" property="birthPlaceDistrict"/></td>
          </tr>
          <!-- Nome do Pai -->
          <tr>
            <td><bean:message key="candidate.fatherName" /></td>
            <td><bean:write name="applicationInfo" property="fatherName"/></td>
          </tr>
          <!-- Nome da Mae -->
          <tr>
            <td><bean:message key="candidate.motherName" /></td>
            <td><bean:write name="applicationInfo" property="motherName"/></td>
          </tr>
          <!-- Profissao -->
          <tr>
            <td><bean:message key="candidate.occupation" /></td>
            <td><bean:write name="applicationInfo" property="occupation"/></td>
          </tr>
          
          
          

          <!-- Dados de Residencia -->
          <tr>
            <td><h2><bean:message key="candidate.addressInfo" /><h2></td>
          </tr>
          <!-- Morada -->
          <tr>
            <td><bean:message key="candidate.address" /></td>
            <td><bean:write name="applicationInfo" property="address"/></td>
          </tr>
          <!-- Codigo Postal -->
          <tr>
            <td><bean:message key="candidate.postCode" /></td>
            <td><bean:write name="applicationInfo" property="postCode"/></td>
          </tr>
          <!-- Localidade de Residencia -->
          <tr>
            <td><bean:message key="candidate.place" /></td>
            <td><bean:write name="applicationInfo" property="place"/></td>
          </tr>
          <!-- Freguesia de Residencia -->
          <tr>
            <td><bean:message key="candidate.addressParish" /></td>
            <td><bean:write name="applicationInfo" property="addressParish"/></td>
          </tr>
          <!-- Concelho de Residencia -->
          <tr>
            <td><bean:message key="candidate.addressMunicipality" /></td>
            <td><bean:write name="applicationInfo" property="addressMunicipality"/></td>
          </tr>
          <!-- Distrito de Residencia -->
          <tr>
            <td><bean:message key="candidate.addressDistrict" /></td>
            <td><bean:write name="applicationInfo" property="addressDistrict"/></td>
          </tr>


          <!-- Contactos -->
          <tr>
            <td><h2><bean:message key="candidate.contactInfo" /><h2></td>
          </tr>
          <!-- Telefone -->
          <tr>
            <td><bean:message key="candidate.telephone" /></td>
            <td><bean:write name="applicationInfo" property="telephone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td><bean:message key="candidate.mobilePhone" /></td>
            <td><bean:write name="applicationInfo" property="mobilePhone"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="candidate.email" /></td>
            <td><bean:write name="applicationInfo" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="candidate.webSite" /></td>
            <td><bean:write name="applicationInfo" property="webSite"/></td>
          </tr>
        </logic:present>
    </table>
    
  </body>
</html>
