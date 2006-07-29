<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
  <bean:define id="personalInfo" name="<%= SessionConstants.PERSONAL_INFO_KEY %>" scope="session"/>
  <html:form action="/changePersonalInfoDispatchAction?method=change">

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<h2><bean:message key="label.person.title.changePersonalInfo" /></h2>
        <table width="100%" cellspacing="0">

<%--
            <% IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW); 
               if ((userView.getCandidateView() != null) && (userView.getRoles().size() == 2) &&
                   (userView.hasRoleType(RoleType.MASTER_DEGREE_CANDIDATE)) && 
                   (userView.getCandidateView().changeablePersonalInfo())) { %>
--%>
<%--
        	    
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
                <!-- Nome do Pai -->
                <tr>
                 <td><bean:message key="label.person.fatherName" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.fatherName" property="fatherName"/></td>
                </tr>
                <!-- Nome da Mae -->
                <tr>
                 <td><bean:message key="label.person.motherName" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.motherName" property="motherName"/></td>
                </tr>
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
                </tr>
                <!-- Freguesia de Naturalidade -->
                <tr>
                 <td><bean:message key="label.person.birthPlaceParish" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceParish" property="birthPlaceParish"/></td>
                </tr>
                <!-- Concelho de Naturalidade -->
                <tr>
                 <td><bean:message key="label.person.birthPlaceMunicipality" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceMunicipality" property="birthPlaceMunicipality"/></td>
                </tr>
                <!-- Distrito de Naturalidade -->
                <tr>
                 <td><bean:message key="label.person.birthPlaceDistrict" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceDistrict" property="birthPlaceDistrict"/></td>
                </tr>
                <!-- Numero do Documento de Identificacao -->
                <tr>
                 <td><bean:message key="label.person.identificationDocumentNumber" /></td>
                  <td><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
				  <!-- <html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentNumber" property="identificationDocumentNumber"/> -->
				  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentNumber" property="identificationDocumentNumber" />
				  
                </tr>
                <!-- Local de Emissao do Documento de Identificacao -->
                <tr>
                 <td><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentIssuePlace" property="identificationDocumentIssuePlace"/></td>
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
                <!-- Tipo do Documento de Identificacao -->
                <tr>
                 <td><bean:message key="label.person.identificationDocumentType" /></td>
                 <td>
                    <!-- <html:select bundle="HTMLALT_RESOURCES" altKey="select.identificationDocumentType" property="identificationDocumentType">
                        <html:options collection="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY %>" property="value" labelProperty="label"/>
                     </html:select> -->
                     <bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
                     <bean:message key='<%=idType.toString()%>'/>
                     <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentType" property="identificationDocumentType" />          
                 </td>
                </tr>
                <!-- Morada -->
                <tr>
                 <td><bean:message key="label.person.address" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.address" property="address"/></td>
                </tr>
                <!-- Localidade -->
                <tr>
                 <td><bean:message key="label.person.place" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.place" property="place"/></td>
                </tr>
                <!-- Codigo Postal -->
                <tr>
                 <td><bean:message key="label.person.postCode" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.postCode" property="postCode"/></td>
                </tr>
                <!-- Area do Codigo Postal -->
                <tr>
                 <td><bean:message key="label.person.areaOfPostCode" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode"/></td>
                </tr>
                <!-- Freguesia de Morada -->
                <tr>
                 <td><bean:message key="label.person.addressParish" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressParish" property="addressParish"/></td>
                </tr>
                <!-- Concelho de Morada -->
                <tr>
                 <td><bean:message key="label.person.addressMunicipality" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressMunicipality" property="addressMunicipality"/></td>
                </tr>
                <!-- Distrito de Morada -->
                <tr>
                 <td><bean:message key="label.person.addressDistrict" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrict" property="addressDistrict"/></td>
                </tr>
                <!-- telefone -->
                <tr>
                 <td><bean:message key="label.person.telephone" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.telephone" property="telephone"/></td>
                </tr>
                <!-- Numero de Contribuinte -->
                <tr>
                 <td><bean:message key="label.person.contributorNumber" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber"/></td>
                </tr>
                <!-- Profissao -->
                <tr>
                 <td><bean:message key="label.person.occupation" /></td>
                  <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.occupation" property="occupation"/></td>
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
               <!-- Nacionalidade -->
                <tr>
                 <td><bean:message key="label.person.nationality" /></td>
                 <td>
                    <html:select bundle="HTMLALT_RESOURCES" altKey="select.nationality" property="nationality">
                        <html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
                     </html:select>          
                 </td>
                </tr>
         	<% }else { %>
--%>

<%--                	
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" property="name" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.username" property="username" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sex" property="sex" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentType" property="identificationDocumentType" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentNumber" property="identificationDocumentNumber" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.identificationDocumentIssuePlace" property="identificationDocumentIssuePlace" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.maritalStatus" property="maritalStatus" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.nationality" property="nationality" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.fatherName" property="fatherName" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.motherName" property="motherName" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthPlaceParish" property="birthPlaceParish" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthPlaceMunicipality" property="birthPlaceMunicipality" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthPlaceDistrict" property="birthPlaceDistrict" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.address" property="address" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.place" property="place" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.postCode" property="postCode" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addressParish" property="addressParish" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addressMunicipality" property="addressMunicipality" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addressDistrict" property="addressDistrict" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.telephone" property="telephone" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contributorNumber" property="contributorNumber" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.occupation" property="occupation" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthDay" property="birthDay" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthMonth" property="birthMonth" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.birthYear" property="birthYear" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idIssueDateDay" property="idIssueDateDay" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idIssueDateMonth" property="idIssueDateMonth" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idIssueDateYear" property="idIssueDateYear" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idExpirationDateDay" property="idExpirationDateDay" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idExpirationDateMonth" property="idExpirationDateMonth" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idExpirationDateYear" property="idExpirationDateYear" />
              	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.areaOfAreaCode" property="areaOfAreaCode" />--%>
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
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mobilePhone" property="mobilePhone"/></td>
          </tr>
          <!-- Work Phome -->
          <tr>
            <td width="15%"><bean:message key="label.person.workPhone" /></td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.workPhone" property="workPhone" maxlength="20"/>&nbsp;
			<bean:message key="label.person.publicData" /></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
	        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email"/>
	        	&nbsp;<bean:message key="label.person.availableEmail" />
	        	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availableEmail" property="availableEmail" value="true"/></td>
	      </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.webSite" property="webSite"/>
            	&nbsp;<bean:message key="label.person.availableWebSite" />
	        	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availableWebSite" property="availableWebSite" value="true"/></td>
          </tr>
          <!-- Photo -->

          <tr valign="top">
          	<td><bean:message key="label.person.photo" /></td>
            <td>
 				<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
 				&nbsp;<bean:message key="label.person.availablePhoto" />
	        	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availablePhoto" property="availablePhoto" value="true"/></td>
          </tr>

   	</table>
<br /><br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Alterar" property="Alterar" styleClass="inputbutton">Alterar</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.Reset" property="Reset" styleClass="inputbutton">Rep�r</html:reset>
      </html:form>  
  </body>
