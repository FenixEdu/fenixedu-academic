<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.Collection" %>
<%@ page import="Util.RoleType" %>
<%@ page import="DataBeans.InfoRole" %>
<%@ page import="ServidorAplicacao.Servico.UserView" %>

<html>
  <head>
    <title><bean:message key="label.person.title.changePersonalInfo" /></title>
  </head>
  <body>

   <table>
  	  <html:form action="/changePersonalInfoDispatchAction?method=change">
   	  <html:hidden property="page" value="1"/>
      <span class="error"><html:errors/></span>

        <tr>
          <td colspan="2"><h2><bean:message key="label.person.title.changePersonalInfo" /></h2></td>
        </tr>
	   	<% UserView userView = (UserView) session.getAttribute("UserView"); %>
        <!-- Nome -->
        <tr>
         <td><bean:message key="label.person.name" /></td>
         <td>
           <% if (userView.hasRoleType(RoleType.MASTER_DEGREE_CANDIDATE) &&
                  userView.hasRoleType(RoleType.PERSON) && 
                  (userView.getRoles().size() == 2)) {
           %>
               <html:text property="name"/>
           <% } else { %>
		   	   <html:hidden property="name"/>
               <bean:write name="personalInfo" property="nome"/>
           <% } %>               
          </td>
        </tr>
        <!-- Estado Civil -->
        <tr>
         <td><bean:message key="label.person.maritalStatus" /></td>
         <td>
            <html:select property="maritalStatus">
                <html:options collection="maritalStatusList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Username -->
        <tr>
         <td><bean:message key="label.person.username" /></td>
          <td><html:text property="username"/></td>
        </tr>
        <!-- Nome do Pai -->
        <tr>
         <td><bean:message key="label.person.fatherName" /></td>
          <td><html:text property="fatherName"/></td>
        </tr>
        <!-- Nome da Mae -->
        <tr>
         <td><bean:message key="label.person.motherName" /></td>
          <td><html:text property="motherName"/></td>
        </tr>
        
        <!-- Data de Nascimento -->
        <tr>
         <td><bean:message key="label.person.birth" /></td>
          <td><html:select property="birthYear">
                <html:options collection="years" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="birthMonth">
                <html:options collection="months" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="birthDay">
                <html:options collection="monthDays" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>

        
        <!-- Freguesia de Naturalidade -->
        <tr>
         <td><bean:message key="label.person.birthPlaceParish" /></td>
          <td><html:text property="birthPlaceParish"/></td>
        </tr>
        <!-- Concelho de Naturalidade -->
        <tr>
         <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
          <td><html:text property="birthPlaceMunicipality"/></td>
        </tr>
        <!-- Distrito de Naturalidade -->
        <tr>
         <td><bean:message key="label.person.birthPlaceDistrict" /></td>
          <td><html:text property="birthPlaceDistrict"/></td>
        </tr>
        <!-- Numero do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentNumber" /></td>
          <td><html:text property="identificationDocumentNumber"/></td>
        </tr>
        <!-- Local de Emissao do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
          <td><html:text property="identificationDocumentIssuePlace"/></td>
        </tr>
        
	    <!-- Data de Emissao do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentIssueDate" /></td>
          <td><html:select property="idIssueDateYear">
                <html:options collection="years" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idIssueDateMonth">
                <html:options collection="months" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idIssueDateDay">
                <html:options collection="monthDays" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>

	<!-- Data de Validade do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
         <td><html:select property="idExpirationDateYear">
                <html:options collection="expirationYears" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idExpirationDateMonth">
                <html:options collection="months" property="value" labelProperty="label"/>
             </html:select>
             <html:select property="idExpirationDateDay">
                <html:options collection="monthDays" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>


        <!-- Tipo do Documento de Identificacao -->
        <tr>
         <td><bean:message key="label.person.identificationDocumentType" /></td>
         <td>
            <html:select property="identificationDocumentType">
                <html:options collection="identificationDocumentTypeList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Morada -->
        <tr>
         <td><bean:message key="label.person.address" /></td>
          <td><html:text property="address"/></td>
        </tr>
        <!-- Localidade -->
        <tr>
         <td><bean:message key="label.person.place" /></td>
          <td><html:text property="place"/></td>
        </tr>
        <!-- Codigo Postal -->
        <tr>
         <td><bean:message key="label.person.postCode" /></td>
          <td><html:text property="postCode"/></td>
        </tr>
        <!-- Area do Codigo Postal -->
        <tr>
         <td><bean:message key="label.person.areaOfPostCode" /></td>
          <td><html:text property="areaOfAreaCode"/></td>
        </tr>
        <!-- Freguesia de Morada -->
        <tr>
         <td><bean:message key="label.person.addressParish" /></td>
          <td><html:text property="addressParish"/></td>
        </tr>
        <!-- Concelho de Morada -->
        <tr>
         <td><bean:message key="label.person.addressMunicipality" /></td>
          <td><html:text property="addressMunicipality"/></td>
        </tr>
        <!-- Distrito de Morada -->
        <tr>
         <td><bean:message key="label.person.addressDistrict" /></td>
          <td><html:text property="addressDistrict"/></td>
        </tr>
        <!-- telefone -->
        <tr>
         <td><bean:message key="label.person.telephone" /></td>
          <td><html:text property="telephone"/></td>
        </tr>
        <!-- telemovel -->
        <tr>
         <td><bean:message key="label.person.mobilePhone" /></td>
          <td><html:text property="mobilePhone"/></td>
        </tr>
        <!-- Email -->
        <tr>
         <td><bean:message key="label.person.email" /></td>
          <td><html:text property="email"/></td>
        </tr>
        <!-- HomePage -->
        <tr>
         <td><bean:message key="label.person.webSite" /></td>
          <td><html:text property="webSite"/></td>
        </tr>
        <!-- Numero de Contribuinte -->
        <tr>
         <td><bean:message key="label.person.contributorNumber" /></td>
          <td><html:text property="contributorNumber"/></td>
        </tr>
        <!-- Profissao -->
        <tr>
         <td><bean:message key="label.person.occupation" /></td>
          <td><html:text property="occupation"/></td>
        </tr>
        <!-- Sexo -->
        <tr>
         <td><bean:message key="label.person.sex" /></td>
         <td>
            <html:select property="sex">
                <html:options collection="sexList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
        <!-- Nacionalidade -->
        <tr>
         <td><bean:message key="label.person.nationality" /></td>
         <td>
            <html:select property="nationality">
                <html:options collection="nationalityList" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
        <td colspan="2">
           <html:submit property="Alterar">Alterar Dados</html:submit>
           <html:reset property="Reset">Dados Originais</html:reset>
        </td>
      </html:form>  
   </table>
  </body>
</html>
