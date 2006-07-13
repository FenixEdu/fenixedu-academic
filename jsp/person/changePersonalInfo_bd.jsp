<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<html>
  <head>
    <title><bean:message key="label.person.title.changePersonalInfo" /></title>
  </head>
  <body>
<span class="error"><html:errors/></span>
  <bean:define id="personalInfo" name="<%= SessionConstants.PERSONAL_INFO_KEY %>" scope="session"/>
  <html:form action="/changePersonalInfoDispatchAction?method=change">

<h2><bean:message key="label.person.title.changePersonalInfo" /></h2>
        <table width="100%" cellspacing="0">
      		<tr>

<%--
            <% IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW); 
               if ((userView.getCandidateView() != null) && (userView.getRoles().size() == 2) &&
                   (userView.hasRoleType(RoleType.MASTER_DEGREE_CANDIDATE)) && 
                   (userView.getCandidateView().changeablePersonalInfo())) { %>
--%>
<%--
        	    <html:hidden property="page" value="2"/>
                <!-- Estado Civil -->
                <tr>
                 <td with="15%"><bean:message key="label.person.maritalStatus" /></td>
                 <td>
                    <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
                	<html:select property="maritalStatus">
                		<html:option key="dropDown.Default" value=""/>
                    	<html:options collection="values" property="value" labelProperty="label"/>
                 	</html:select>            
                 </td>
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
                        <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                     </html:select>
                     <html:select property="birthMonth">
                        <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                     </html:select>
                     <html:select property="birthDay">
                        <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
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
                  <td><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
				  <!-- <html:text property="identificationDocumentNumber"/> -->
				  <html:hidden property="identificationDocumentNumber" />
				  
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
                <!-- Tipo do Documento de Identificacao -->
                <tr>
                 <td><bean:message key="label.person.identificationDocumentType" /></td>
                 <td>
                    <!-- <html:select property="identificationDocumentType">
                        <html:options collection="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY %>" property="value" labelProperty="label"/>
                     </html:select> -->
                     <bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
                     <bean:message key='<%=idType.toString()%>'/>
                     <html:hidden property="identificationDocumentType" />          
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
                        <html:options collection="<%= SessionConstants.SEX_LIST_KEY %>" property="value" labelProperty="label"/>
                     </html:select>          
                 </td>
                </tr>   
               <!-- Nacionalidade -->
                <tr>
                 <td><bean:message key="label.person.nationality" /></td>
                 <td>
                    <html:select property="nationality">
                        <html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
                     </html:select>          
                 </td>
                </tr>
         	<% }else { %>
--%>
    	   	    <html:hidden property="page" value="1"/>

<%--                	
				<html:hidden property="name" />
				<html:hidden property="username" />
				<html:hidden property="sex" />
              	<html:hidden property="identificationDocumentType" />
              	<html:hidden property="identificationDocumentNumber" />
              	<html:hidden property="identificationDocumentIssuePlace" />
              	<html:hidden property="maritalStatus" />
              	<html:hidden property="nationality" />
              	<html:hidden property="fatherName" />
              	<html:hidden property="motherName" />
              	<html:hidden property="birthPlaceParish" />
              	<html:hidden property="birthPlaceMunicipality" />
              	<html:hidden property="birthPlaceDistrict" />
              	<html:hidden property="address" />
              	<html:hidden property="place" />
              	<html:hidden property="postCode" />
              	<html:hidden property="addressParish" />
              	<html:hidden property="addressMunicipality" />
              	<html:hidden property="addressDistrict" />
              	<html:hidden property="telephone" />
              	<html:hidden property="contributorNumber" />
              	<html:hidden property="occupation" />
              	<html:hidden property="birthDay" />
              	<html:hidden property="birthMonth" />
              	<html:hidden property="birthYear" />
              	<html:hidden property="idIssueDateDay" />
              	<html:hidden property="idIssueDateMonth" />
              	<html:hidden property="idIssueDateYear" />
              	<html:hidden property="idExpirationDateDay" />
              	<html:hidden property="idExpirationDateMonth" />
              	<html:hidden property="idExpirationDateYear" />
              	<html:hidden property="areaOfAreaCode" />--%>
				<tr>
					<td class="infoop" ><span class="emphasis-box">info</span>
         			<td class="infoop">
         				<bean:message key="message.person.changeContacts.info" /> 
         			</td>
         		</tr>
         	</table>
<%--         	<% } %>
--%>
	<br />
	<table>
          <!-- Telemovel -->
          <tr>
            <td width="15%"><bean:message key="label.person.mobilePhone" /></td>
            <td><html:text property="mobilePhone"/></td>
          </tr>
          <!-- Work Phome -->
          <tr>
            <td width="15%"><bean:message key="label.person.workPhone" /></td>
            <td><html:text property="workPhone" maxlength="20"/>&nbsp;
			<bean:message key="label.person.publicData" /></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
	        <td><html:text property="email"/>
	        	&nbsp;<bean:message key="label.person.availableEmail" />
	        	<html:checkbox property="availableEmail" value="true"/></td>
	      </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><html:text property="webSite"/>
            	&nbsp;<bean:message key="label.person.availableWebSite" />
	        	<html:checkbox property="availableWebSite" value="true"/></td>
          </tr>
          <!-- Photo -->

          <tr valign="top">
          	<td><bean:message key="label.person.photo" /></td>
            <td>
 				<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
 				&nbsp;<bean:message key="label.person.availablePhoto" />
	        	<html:checkbox property="availablePhoto" value="true"/></td>
          </tr>

   	</table>
<br /><br />
<html:submit property="Alterar" styleClass="inputbutton">Alterar</html:submit>
<html:reset property="Reset" styleClass="inputbutton">Repôr</html:reset>
      </html:form>  
  </body>
</html>
