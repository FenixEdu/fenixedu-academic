<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.util.SituationName" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>
<html:form action="/editCandidate?method=change">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>  
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID" />  
   	  
   <table>
 		<bean:define id="infoCandidate" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE %>" scope="request"/>
   	<bean:define id="personalInfo" name="infoCandidate" property="infoPerson" />
    	<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2><bean:message key="label.person.title.changePersonalInfo" /></h2>

<!-- Dados Pessoais -->
<h3 class="separator2 mtop15"><bean:message key="label.person.title.personal.info" /></h3>

<table class="tstyle2">
        <!-- Nome -->
        <tr>
         <td width="30%"><bean:message key="label.person.name" /></td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="75" /></td>
        </tr>
        <!-- Estado Civil -->
        <tr>
         <td width="30%"><bean:message key="label.person.maritalStatus" /></td>
         <td>
            <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.MaritalStatus"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.maritalStatus" property="maritalStatus">
        		<html:option key="dropDown.Default" value=""/>
            	<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>           
         </td>
        </tr>
        <!-- Username -->
        <tr>
         <td width="30%"><bean:message key="label.person.username" /></td>
          <td><bean:write name="personalInfo" property="username"/></td>
		  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.username" property="username" />
        </tr>
        
        <!-- Nome do Pai -->
        <tr>
         <td width="30%"><bean:message key="label.person.fatherName" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.fatherName" property="fatherName" size="75"/></td>
        </tr>
        <!-- Nome da Mae -->
        <tr>
         <td width="30%"><bean:message key="label.person.motherName" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.motherName" property="motherName" size="75"/></td>
        </tr>   
        <!-- Data de Nascimento -->
        <tr>
         <td width="30%"><bean:message key="label.person.birth" /></td>
          <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.birthYear" property="birthYear">
                <html:options collection="<%= PresentationConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select bundle="HTMLALT_RESOURCES" altKey="select.birthMonth" property="birthMonth">
                <html:options collection="<%= PresentationConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select bundle="HTMLALT_RESOURCES" altKey="select.birthDay" property="birthDay">
                <html:options collection="<%= PresentationConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>
        <!-- Freguesia de Naturalidade -->
        <tr>
         <td width="30%"><bean:message key="label.person.birthPlaceParish" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceParish" property="birthPlaceParish" size="50" /></td>
        </tr>
        <!-- Concelho de Naturalidade -->
        <tr>
         <td width="30%"><bean:message key="label.person.birthPlaceMunicipality" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceMunicipality" property="birthPlaceMunicipality"/></td>
        </tr>
        <!-- Distrito de Naturalidade -->
        <tr>
         <td width="30%"><bean:message key="label.person.birthPlaceDistrict" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.birthPlaceDistrict" property="birthPlaceDistrict"/></td>
        </tr>
        <!-- Numero do Documento de Identificacao -->
        <tr>
         <td width="30%"><bean:message key="label.person.identificationDocumentNumber" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentNumber" property="identificationDocumentNumber"/></td>
        </tr>
        <!-- Local de Emissao do Documento de Identificacao -->
        <tr>
         <td width="30%"><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentIssuePlace" property="identificationDocumentIssuePlace"/></td>
        </tr>
	    <!-- Data de Emissao do Documento de Identificacao -->
        <tr>
         <td width="30%"><bean:message key="label.person.identificationDocumentIssueDate" /></td>
          <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateYear" property="idIssueDateYear">
                <html:options collection="<%= PresentationConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateMonth" property="idIssueDateMonth">
                <html:options collection="<%= PresentationConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select bundle="HTMLALT_RESOURCES" altKey="select.idIssueDateDay" property="idIssueDateDay">
                <html:options collection="<%= PresentationConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>
	    <!-- Data de Validade do Documento de Identificacao -->
        <tr>
         <td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
         <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateYear" property="idExpirationDateYear">
                <html:options collection="<%= PresentationConstants.EXPIRATION_YEARS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateMonth" property="idExpirationDateMonth">
                <html:options collection="<%= PresentationConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>
             <html:select bundle="HTMLALT_RESOURCES" altKey="select.idExpirationDateDay" property="idExpirationDateDay">
                <html:options collection="<%= PresentationConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             </html:select>
          </td>          
        </tr>
        <!-- Tipo do Documento de Identificacao -->
        <tr>
         <td width="30%"><bean:message key="label.person.identificationDocumentType" /></td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.identificationDocumentType" property="identificationDocumentType">
            	<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Numero de Contribuinte -->
        <tr>
         <td width="30%"><bean:message key="label.person.contributorNumber" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber"/></td>
        </tr>
        <!-- Profissao -->
        <tr>
         <td width="30%"><bean:message key="label.person.occupation" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.occupation" property="occupation"/></td>
        </tr>
        <!-- Sexo -->
        <tr>
         <td width="30%"><bean:message key="label.person.sex" /></td>
         <td>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.sex" property="sex">
                <html:options collection="<%= PresentationConstants.SEX_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Nacionalidade -->
        <tr>
         <td width="30%"><bean:message key="label.person.nationality" /></td>
         <td>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.nationality" property="nationality">
                <html:options collection="<%= PresentationConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
</table>


        <!-- Morada -->

<h3 class="separator2 mtop15"><bean:message key="label.person.title.addressInfo" /></h3>


<table class="tstyle2">
        <tr>
         <td width="30%"><bean:message key="label.person.address" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.address" property="address" size="75" /></td>
        </tr>
        <!-- Localidade -->
        <tr>
         <td width="30%"><bean:message key="label.person.place" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.place" property="place" size="50"/></td>
        </tr>
        <!-- Codigo Postal -->
        <tr>
         <td width="30%"><bean:message key="label.person.postCode" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.postCode" property="postCode"/></td>
        </tr>
        <!-- Area do Codigo Postal -->
        <tr>
         <td width="30%"><bean:message key="label.person.areaOfPostCode" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode"/></td>
        </tr>
        <!-- Freguesia de Morada -->
        <tr>
         <td width="30%"><bean:message key="label.person.addressParish" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressParish" property="addressParish" size="50"/></td>
        </tr>
        <!-- Concelho de Morada -->
        <tr>
         <td width="30%"><bean:message key="label.person.addressMunicipality" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressMunicipality" property="addressMunicipality"/></td>
        </tr>
        <!-- Distrito de Morada -->
        <tr>
         <td width="30%"><bean:message key="label.person.addressDistrict" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.addressDistrict" property="addressDistrict"/></td>
        </tr>
</table>


     <!-- Contactos -->        
<h3 class="separator2 mtop15"><bean:message key="label.person.title.contactInfo" /></h3>

<table class="tstyle2">
        <!-- telefone -->
        <tr>
         <td width="30%"><bean:message key="label.person.telephone" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.telephone" property="telephone"/></td>
        </tr>
        <!-- telemovel -->
        <tr>
         <td width="30%"><bean:message key="label.person.mobilePhone" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mobilePhone" property="mobilePhone"/></td>
        </tr>
        <!-- Email -->
        <tr>
         <td width="30%"><bean:message key="label.person.email" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email"/></td>
        </tr>
        <!-- HomePage -->
        <tr>
         <td width="30%"><bean:message key="label.person.webSite" /></td>
          <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.webSite" property="webSite"/></td>
        </tr>
</table>

	 <!-- Informacao de Licenciatura -->
	<h3 class="separator2 mtop15"><bean:message key="label.candidate.majorDegreeInfo" /></h3>

<table class="tstyle2">
	   <tr>
        <td width="30%"><bean:message key="label.candidate.majorDegree" />:</td>
        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegree" property="majorDegree" size="50"/></td>
	   </tr>
	   <tr>
        <td width="30%"><bean:message key="label.candidate.majorDegreeSchool" />:</td>
        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegreeSchool" property="majorDegreeSchool"/></td>
	   </tr>
	   <tr>
        <td width="30%"><bean:message key="label.candidate.average" />:</td>
        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.average" property="average"/></td>
	   </tr>
	   <tr>
        <td width="30%"><bean:message key="label.candidate.majorDegreeYear" />:</td>
        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegreeYear" property="majorDegreeYear"/></td>
	   </tr>
	   <!-- Specialization Area -->
       <tr>
         <td><bean:message key="label.candidate.specializationArea"/> </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.specializationArea" property="specializationArea"/></td>
         </td>
       </tr>
	   
		<!-- Active Situation -->
       <tr>
        <td width="30%"><bean:message key="label.masterDegree.administrativeOffice.situation" />:</td>
        <td><bean:write name="infoCandidate" property="infoCandidateSituation.situation"/></td>
	   </tr>
	   <tr>
        <td width="30%"><bean:message key="label.masterDegree.administrativeOffice.situationDate" />:</td>
        <logic:present name="infoCandidate" property="infoCandidateSituation.date" >
	        <bean:define id="date" name="infoCandidate" property="infoCandidateSituation.date" />
			<td><%= Data.format2DayMonthYear((Date) date) %></td>          
		</logic:present>
	   </tr>
	<%-- <tr>
        <td width="30%"><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
        <td><bean:write name="infoCandidate" property="infoCandidateSituation.remarks"/></td>
	   </tr> --%>
	   <tr>
         <td width="30%" style="vertical-align: top;"><bean:message key="label.masterDegree.administrativeOffice.remarks" />:</td>
          <td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.situationRemarks" property="situationRemarks" cols="30" rows="5" /></td>
        </tr>
 	   <tr>
         <td width="30%"><bean:message key="label.masterDegree.administrativeOffice.newSituation" />:</td>
		 <td>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.situation" property="situation">
                <html:options collection="<%= PresentationConstants.CANDIDATE_SITUATION_LIST %>" property="value" labelProperty="label"/>
             </html:select>          
		 </td>
        </tr>
   </table>
   
   <p class="mvert15">
	   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Alterar" property="Alterar">Alterar Dados</html:submit>
	   <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.Reset" property="Reset">Dados Originais</html:reset>
   </p>

	</html:form>