<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
  <head>
    <title><bean:message key="label.person.visualizeInformation" /></title>
  </head>
  <body>
  
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

          <!-- Dados Pessoais -->
          <tr>
            <td><h2><bean:message key="label.person.title.personal.info" /><h2></td>
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
            <td><bean:write name="personalInfo" property="dataEmissaoDocumentoIdentificacao"/></td>
          </tr>
          <!-- Data de Validade do Documento de Identificacao -->
          <tr>
            <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
            <td><bean:write name="personalInfo" property="dataValidadeDocumentoIdentificacao"/></td>
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
            <td><bean:write name="personalInfo" property="nascimento"/></td>
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
        </logic:present>
    </table>
    
  </body>
</html>
