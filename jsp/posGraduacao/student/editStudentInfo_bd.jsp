<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<bean:define id="personalInfo" name="infoPerson" scope="request" />
<br />
<logic:present name="personalInfo">
<html:form action="/editStudentInfo.do?method=change" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" name="personalInfo" property="idInternal" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" name="infoStudent" property="number"  />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
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
      	<td class="greytxt"><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="75"/></td>
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
        <html:select bundle="HTMLALT_RESOURCES" altKey="select.sex" property="sex">
            <html:options collection="<%= SessionConstants.SEX_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>          
     </td>
    </tr>   
    <!-- Numero do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentNumber" /></td>
	   <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentNumber" property="identificationDocumentNumber"/> </td>
	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentNumber" property="identificationDocumentNumber" />
    </tr>
    <!-- Tipo do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentType" /></td>
     <td>
     	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
         <html:select bundle="HTMLALT_RESOURCES" altKey="select.identificationDocumentType" property="identificationDocumentType">
         	<html:option key="dropDown.Default" value=""/>
            <html:options collection="values" property="value" labelProperty="label"/>
         </html:select>
       <!--  <bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/>-->
         <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentType" property="identificationDocumentType" />          
     </td>
    </tr>
    <!-- Local de Emissao do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentIssuePlace" property="identificationDocumentIssuePlace" size="75"/></td>
    </tr>
    <!-- Data de Emissao do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentIssueDate" /></td>
      <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateYear" property="idIssueDateYear">
            <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateMonth" property="idIssueDateMonth">
            <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateDay" property="idIssueDateDay">
            <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
      </td>          
    </tr>
<!-- Data de Validade do Documento de Identificacao -->
    <tr>
     <td><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
     <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateYear" property="idExpirationDateYear">
            <html:options collection="<%= SessionConstants.EXPIRATION_YEARS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateMonth" property="idExpirationDateMonth">
            <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>
         <html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateDay" property="idExpirationDateDay">
            <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
         </html:select>
      </td>          
    </tr>
     <!-- Numero de Contribuinte -->
    <tr>
     <td><bean:message key="label.person.contributorNumber" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber"/></td>
    </tr>
    <!-- Profissao -->
    <tr>
     <td><bean:message key="label.person.occupation" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.occupation" property="occupation" size="75"/></td>
    </tr>
    <!-- Estado Civil -->
    <tr>
     <td with="15%"><bean:message key="label.person.maritalStatus" /></td>
     <td>
        <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.maritalStatus" property="maritalStatus">
    		<html:option key="dropDown.Default" value=""/>
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
	 <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.birthYear" property="birthYear">
	        <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
	     </html:select>
	     <html:select bundle="HTMLALT_RESOURCES" altKey="select.birthMonth" property="birthMonth">
	        <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
	     </html:select>
	     <html:select bundle="HTMLALT_RESOURCES" altKey="select.birthDay" property="birthDay">
	        <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
	     </html:select>
	 </td>   
	<!-- Nacionalidade -->
    <tr>
     <td><bean:message key="label.person.nationality" /></td>
     <td>
        <html:select bundle="HTMLALT_RESOURCES" altKey="select.nationality" property="nationality">
            <html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
         </html:select>          
     </td>
    </tr>       
    <!-- Freguesia de Naturalidade -->
    <tr>
     <td><bean:message key="label.person.birthPlaceParish" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceParish" property="birthPlaceParish" size="75"/></td>
    </tr>
    <!-- Concelho de Naturalidade -->
    <tr>
     <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceMunicipality" property="birthPlaceMunicipality" size="75"/></td>
    </tr>
    <!-- Distrito de Naturalidade -->
    <tr>
     <td><bean:message key="label.person.birthPlaceDistrict" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceDistrict" property="birthPlaceDistrict" size="75"/></td>
    </tr>
    </tr>              
    <!-- Nome do Pai -->
    <tr>
     <td><bean:message key="label.person.fatherName" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.fatherName" property="fatherName" size="75"/></td>
    </tr>
    <!-- Nome da Mae -->
    <tr>
     <td><bean:message key="label.person.motherName" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.motherName" property="motherName" size="75"/></td>
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
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.address" property="address" size="75"/></td>
    </tr>
    <!-- Codigo Postal -->
    <tr>
     <td><bean:message key="label.person.postCode" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.postCode" property="postCode" size="6"/></td>
    </tr>
    <!-- Localidade -->
    <tr>
     <td><bean:message key="label.person.place" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.place" property="place" size="75"/></td>
    </tr>
    
    <!-- Area do Codigo Postal -->
    <tr>
     <td><bean:message key="label.person.areaOfPostCode" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode" size="6"/></td>
    </tr>
    <!-- Freguesia de Morada -->
    <tr>
     <td><bean:message key="label.person.addressParish" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressParish" property="addressParish" size="75"/></td>
    </tr>
    <!-- Concelho de Morada -->
    <tr>
     <td><bean:message key="label.person.addressMunicipality" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressMunicipality" property="addressMunicipality" size="75"/></td>
    </tr>
    <!-- Distrito de Morada -->
    <tr>
     <td><bean:message key="label.person.addressDistrict" /></td>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrict" property="addressDistrict" size="75"/></td>
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
	      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.telephone" property="telephone"/></td>
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
    
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar Dados" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>
    