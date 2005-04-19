<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<span class="error"><html:errors/></span>



<bean:define id="personalInfo" name="infoPerson" scope="request" />
<br />
<logic:present name="personalInfo">
<html:form action="/editStudentInfo.do?method=change" >
<html:hidden name="personalInfo" property="idInternal" />
<html:hidden property="page" value="2"/>
<html:hidden name="infoStudent" property="number"  />
<html:hidden property="studentNumber" />
<table width="100%" cellpadding="0" cellspacing="0">
	<!-- Dados Pessoais -->
	<tr>
	    <td class="infoop" width="25"><span class="emphasis-box">1</span></td>
	    <td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
	</tr>
</table>
<br />
<table>	
	
    <table width="100%">
          <!-- Nome -->
    <tr>
   		<td width="30%"><bean:message key="label.person.name" /></td>
      	<td class="greytxt"><html:text property="name" size="75"/></td>
   	</tr>
    <!-- Username -->
    <tr>
    	<td width="30%"><bean:message key="label.person.username" /></td>
        <td class="greytxt"><bean:write name="changePersonalInfoForm" property="username"/></td>
    </tr>   
  	<!-- Sexo -->
    <tr>
     <td><bean:message key="label.person.sex" /></td>
     <td>
        <html:select property="sex">
            <html:options collection="<%= SessionConstants.SEX_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>          
     </td>
    </tr>   
    <!-- Numero do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentNumber" /></td>
      <!-- <bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/> -->
	   <td><html:text property="identificationDocumentNumber"/> </td>
	  <html:hidden property="identificationDocumentNumber" />
    </tr>
    <!-- Tipo do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentType" /></td>
     <td>
         <html:select property="identificationDocumentType">
            <html:options collection="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>
       <!--  <bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/>-->
         <html:hidden property="identificationDocumentType" />          
     </td>
    </tr>
    <!-- Local de Emissao do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
      <td><html:text property="identificationDocumentIssuePlace" size="75"/></td>
    </tr>
    <!-- Data de Emissao do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentIssueDate" /></td>
      <td><html:select property="idIssueDateYear">
            <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select property="idIssueDateMonth">
            <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select property="idIssueDateDay">
            <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
      </td>          
    </tr>
<!-- Data de Validade do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
     <td><html:select property="idExpirationDateYear">
            <html:options collection="<%= SessionConstants.EXPIRATION_YEARS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select property="idExpirationDateMonth">
            <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select property="idExpirationDateDay">
            <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
      </td>          
    </tr>
     <!-- Numero de Contribuinte -->
    <tr>
     <td><bean:message key="label.person.contributorNumber" /></td>
      <td><html:text property="contributorNumber"/></td>
    </tr>
    <!-- Profissao -->
    <tr>
     <td><bean:message key="label.person.occupation" /></td>
      <td><html:text property="occupation" size="75"/></td>
    </tr>
    <!-- Estado Civil -->
    <tr>
     <td with="15%"><bean:message key="label.person.maritalStatus" /></td>
     <td>
        <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
		<html:select property="maritalStatus">
    		<html:option key="dropDown.Default" value="null"/>
        	<html:options collection="values" property="value" labelProperty="label"/>
		</html:select>           
     </td>
    </tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
   <!-- Filiação -->
  	<tr>
	   <td class="infoop" width="25"><span class="emphasis-box">2</span></td>
  	   <td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
  	</tr>
</table>
<br />          
<table width="100%">  
   <!-- Data de Nascimento -->
	<tr>
	 <td><bean:message key="label.person.birth" /></td>
	 <td><html:select property="birthYear">
	        <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
	     </html:select>
	     <html:select property="birthMonth">
	        <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
	     </html:select>
	     <html:select property="birthDay">
	        <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
	     </html:select>
	 </td>   
	<!-- Nacionalidade -->
    <tr>
     <td><bean:message key="label.person.nationality" /></td>
     <td>
        <html:select property="nationality">
            <html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>          
     </td>
    </tr>       
    <!-- Freguesia de Naturalidade -->
    <tr>
     <td><bean:message key="label.person.birthPlaceParish" /></td>
      <td><html:text property="birthPlaceParish" size="75"/></td>
    </tr>
    <!-- Concelho de Naturalidade -->
    <tr>
     <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
      <td><html:text property="birthPlaceMunicipality" size="75"/></td>
    </tr>
    <!-- Distrito de Naturalidade -->
    <tr>
     <td><bean:message key="label.person.birthPlaceDistrict" /></td>
      <td><html:text property="birthPlaceDistrict" size="75"/></td>
    </tr>
    </tr>              
    <!-- Nome do Pai -->
    <tr>
     <td><bean:message key="label.person.fatherName" /></td>
      <td><html:text property="fatherName" size="75"/></td>
    </tr>
    <!-- Nome da Mae -->
    <tr>
     <td><bean:message key="label.person.motherName" /></td>
      <td><html:text property="motherName" size="75"/></td>
    </tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<!-- Dados de Residencia -->
	<tr>
	    <td class="infoop" width="25"><span class="emphasis-box">3</span></td>
	    <td class="infoop"><strong><bean:message key="label.person.title.addressInfo" /></strong></td>
	</tr>
</table>
<br />
<table width="100%">                              
    <!-- Morada -->
    <tr>
     <td><bean:message key="label.person.address" /></td>
      <td><html:text property="address" size="75"/></td>
    </tr>
    <!-- Codigo Postal -->
    <tr>
     <td><bean:message key="label.person.postCode" /></td>
      <td><html:text property="postCode" size="6"/></td>
    </tr>
    <!-- Localidade -->
    <tr>
     <td><bean:message key="label.person.place" /></td>
      <td><html:text property="place" size="75"/></td>
    </tr>
    
    <!-- Area do Codigo Postal -->
    <tr>
     <td><bean:message key="label.person.areaOfPostCode" /></td>
      <td><html:text property="areaOfAreaCode" size="6"/></td>
    </tr>
    <!-- Freguesia de Morada -->
    <tr>
     <td><bean:message key="label.person.addressParish" /></td>
      <td><html:text property="addressParish" size="75"/></td>
    </tr>
    <!-- Concelho de Morada -->
    <tr>
     <td><bean:message key="label.person.addressMunicipality" /></td>
      <td><html:text property="addressMunicipality" size="75"/></td>
    </tr>
    <!-- Distrito de Morada -->
    <tr>
     <td><bean:message key="label.person.addressDistrict" /></td>
      <td><html:text property="addressDistrict" size="75"/></td>
    </tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<!-- Contactos -->
	<tr>
	    <td class="infoop" width="25"><span class="emphasis-box">4</span></td>
	    <td class="infoop"><strong><bean:message key="label.person.title.contactInfo" /></strong></td>
	</tr>
	</table>
<br />
<table width="100%">
	<!-- telefone -->
	<tr>
	     <td><bean:message key="label.person.telephone" /></td>
	      <td><html:text property="telephone"/></td>
	</tr>
	<!-- Telemovel -->
	<tr>
	    <td width="15%"><bean:message key="label.person.mobilePhone" /></td>
	    <td><bean:write name="changePersonalInfoForm" property="mobilePhone"/></td>
	</tr>
	<!-- E-Mail -->
	<tr>
	    <td><bean:message key="label.person.email" /></td>
	    <td><bean:write name="changePersonalInfoForm" property="email" /></td>
	</tr>
	<!-- WebPage -->
	<tr>
	    <td><bean:message key="label.person.webSite" /></td>
	    <td><bean:write name="changePersonalInfoForm" property="webSite" /></td>
	</tr>
 </table>
    
	<html:submit value="Alterar Dados" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>
    