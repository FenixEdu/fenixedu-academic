<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="candidate.titleChangeApplicationInfo" /></title>
  </head>
  <body>

   <table>
  	  <html:form action="/changeApplicationInfoForm">
  	  <html:hidden property="candidateNumber"/>
  	  <html:hidden property="applicationYear"/>
  	  <html:hidden property="degreeName"/>
  	  <html:hidden property="degreeCode"/>
  	  <html:hidden property="specialization"/>

      <span class="error"><html:errors/></span>

        <tr>
          <td colspan="2"><h2><bean:message key="candidate.titleChangeApplicationInfo" /></h2></td>
        </tr>
        <!-- Numero de Candidato -->
        <tr>
         <td><bean:message key="candidate.candidateNumber" /></td>
          <td><bean:write name="candidateInformation" property="candidateNumber"/></td>
        </tr>
        <!-- Ano de Candidatura -->
        <tr>
         <td><bean:message key="candidate.applicationYear" /></td>
          <td><bean:write name="candidateInformation" property="applicationYear"/></td>
        </tr>
        <!-- Nome do Curso -->
        <tr>
         <td><bean:message key="candidate.degreeName" /></td>
          <td><bean:write name="candidateInformation" property="infoDegree.nome"/></td>
        </tr>
        <!-- Sigla do Curso -->
        <tr>
         <td><bean:message key="candidate.degreeCode" /></td>
          <td><bean:write name="candidateInformation" property="infoDegree.sigla"/></td>
        </tr>
        <!-- Especializacao -->
        <tr>
         <td><bean:message key="candidate.specialization" /></td>
          <td><bean:write name="candidateInformation" property="specialization"/></td>
        </tr>
        
        <!-- Situacao da Candidatura -->
    	  <tr>
            <td><h2><bean:message key="candidate.applicationInfoSituation" /></h2></td>
          </tr>

          <!-- Situacao -->
          <tr>
            <td><bean:message key="candidate.infoCandidateSituation" /></td>
            <td><bean:write name="candidateInformation" property="infoCandidateSituation.situation"/></td>
          </tr>

          <!-- Data da Situacao -->
          <tr>
            <td><bean:message key="candidate.infoCandidateSituationDate" /></td>
            <td><bean:write name="candidateInformation" property="infoCandidateSituation.date"/></td>
          </tr>

          <!-- Observacoes -->
          <tr>
            <td><bean:message key="candidate.infoCandidateSituationRemarks" /></td>
            <td><bean:write name="candidateInformation" property="infoCandidateSituation.remarks"/></td>
          </tr>

        
        
        <!-- Nome -->
        <tr>
         <td><bean:message key="candidate.name" /></td>
          <td><html:text property="name"/></td>
        </tr>
        <!-- Estado Civil -->
        <tr>
         <td><bean:message key="candidate.maritalStatus" /></td>
         <td>
            <html:select property="maritalStatus">
                <html:options collection="maritalStatusList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Username -->
        <tr>
         <td><bean:message key="candidate.username" /></td>
            <td><bean:write name="candidateInformation" property="username"/></td>
        </tr>
        <!-- Password -->
        <tr>
         <td><bean:message key="candidate.password" /></td>
          <td><html:text property="password"/></td>
        </tr>
        <!-- Licenciatura -->
        <tr>
         <td><bean:message key="candidate.majorDegree" /></td>
          <td><html:text property="majorDegree"/></td>
        </tr>
        <!-- Ano de Licenciatura -->
        <tr>
         <td><bean:message key="candidate.majorDegreeYear" /></td>
          <td><html:text property="majorDegreeYear"/></td>
        </tr>
        <!-- Escola de Licenciatura -->
        <tr>
         <td><bean:message key="candidate.majorDegreeSchool" /></td>
          <td><html:text property="majorDegreeSchool"/></td>
        </tr>
        <!-- Nome do Pai -->
        <tr>
         <td><bean:message key="candidate.fatherName" /></td>
          <td><html:text property="fatherName"/></td>
        </tr>
        <!-- Nome da Mae -->
        <tr>
         <td><bean:message key="candidate.motherName" /></td>
          <td><html:text property="motherName"/></td>
        </tr>
        
        <!-- Data de Nascimento -->
        <tr>
         <td><bean:message key="candidate.birth" /></td>

          <td><bean:message key="candidate.year" />   
             <html:select property="birthYear">
                <html:options collection="years" property="value" labelProperty="label"/>
             </html:select>
          </td>          
          <td><bean:message key="candidate.month" />   
             <html:select property="birthMonth">
                <html:options collection="months" property="value" labelProperty="label"/>
             </html:select>
          </td>          
          <td><bean:message key="candidate.day" />   
             <html:select property="birthDay">
                <html:options collection="monthDays" property="value" labelProperty="label"/>
             </html:select>
          </td>          

        </tr>

        
        <!-- Freguesia de Naturalidade -->
        <tr>
         <td><bean:message key="candidate.birthPlaceParish" /></td>
          <td><html:text property="birthPlaceParish"/></td>
        </tr>
        <!-- Concelho de Naturalidade -->
        <tr>
         <td><bean:message key="candidate.birthPlaceMunicipality" /></td>
          <td><html:text property="birthPlaceMunicipality"/></td>
        </tr>
        <!-- Distrito de Naturalidade -->
        <tr>
         <td><bean:message key="candidate.birthPlaceDistrict" /></td>
          <td><html:text property="birthPlaceDistrict"/></td>
        </tr>
        <!-- Numero do Documento de Identificacao -->
        <tr>
         <td><bean:message key="candidate.identificationDocumentNumber" /></td>
          <td><html:text property="identificationDocumentNumber"/></td>
        </tr>
        <!-- Local de Emissao do Documento de Identificacao -->
        <tr>
         <td><bean:message key="candidate.identificationDocumentIssuePlace" /></td>
          <td><html:text property="identificationDocumentIssuePlace"/></td>
        </tr>
        
	<!-- Data de Emissao do Documento de Identificacao -->
        <tr>
         <td><bean:message key="candidate.identificationDocumentIssueDate" /></td>
          <td><bean:message key="candidate.year" />   
             <html:select property="idIssueDateYear">
                <html:options collection="years" property="value" labelProperty="label"/>
             </html:select>
          </td>          
          <td><bean:message key="candidate.month" />   
             <html:select property="idIssueDateMonth">
                <html:options collection="months" property="value" labelProperty="label"/>
             </html:select>
          </td>          
          <td><bean:message key="candidate.day" />   
             <html:select property="idIssueDateDay">
                <html:options collection="monthDays" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>


        <!-- Tipo do Documento de Identificacao -->
        <tr>
         <td><bean:message key="candidate.identificationDocumentType" /></td>
         <td>
            <html:select property="identificationDocumentType">
                <html:options collection="identificationDocumentTypeList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Morada -->
        <tr>
         <td><bean:message key="candidate.address" /></td>
          <td><html:text property="address"/></td>
        </tr>
        <!-- Localidade -->
        <tr>
         <td><bean:message key="candidate.place" /></td>
          <td><html:text property="place"/></td>
        </tr>
        <!-- Codigo Postal -->
        <tr>
         <td><bean:message key="candidate.postCode" /></td>
          <td><html:text property="postCode"/></td>
        </tr>
        <!-- Freguesia de Morada -->
        <tr>
         <td><bean:message key="candidate.addressParish" /></td>
          <td><html:text property="addressParish"/></td>
        </tr>
        <!-- Concelho de Morada -->
        <tr>
         <td><bean:message key="candidate.addressMunicipality" /></td>
          <td><html:text property="addressMunicipality"/></td>
        </tr>
        <!-- Distrito de Morada -->
        <tr>
         <td><bean:message key="candidate.addressDistrict" /></td>
          <td><html:text property="addressDistrict"/></td>
        </tr>
        <!-- telefone -->
        <tr>
         <td><bean:message key="candidate.telephone" /></td>
          <td><html:text property="telephone"/></td>
        </tr>
        <!-- telemovel -->
        <tr>
         <td><bean:message key="candidate.mobilePhone" /></td>
          <td><html:text property="mobilePhone"/></td>
        </tr>
        <!-- Email -->
        <tr>
         <td><bean:message key="candidate.email" /></td>
          <td><html:text property="email"/></td>
        </tr>
        <!-- HomePage -->
        <tr>
         <td><bean:message key="candidate.webSite" /></td>
          <td><html:text property="webSite"/></td>
        </tr>
        <!-- Numero de Contribuinte -->
        <tr>
         <td><bean:message key="candidate.contributorNumber" /></td>
          <td><html:text property="contributorNumber"/></td>
        </tr>
        <!-- Profissao -->
        <tr>
         <td><bean:message key="candidate.occupation" /></td>
          <td><html:text property="occupation"/></td>
        </tr>
        <!-- Sexo -->
        <tr>
         <td><bean:message key="candidate.sex" /></td>
         <td>
            <html:select property="sex">
                <html:options collection="sexList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
        <!-- Pais de Origem -->
        <tr>
         <td><bean:message key="candidate.country" /></td>
         <td>
            <html:select property="country">
                <html:options collection="countryList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
        <!-- Nacionalidade -->
        <tr>
         <td><bean:message key="candidate.nationality" /></td>
         <td>
            <html:select property="nationality">
                <html:options collection="nationalityList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Media -->
        <tr>
         <td><bean:message key="candidate.average" /></td>
          <td><html:text property="average"/></td>
        </tr>
        
        <td colspan="2">
           <html:submit property="Alterar">Alterar</html:submit>
        </td>
        <td colspan="2">
           <html:reset property="Reset">Dados Originais</html:reset>
        </td>
      </html:form>  
   </table>
  </body>
</html>
