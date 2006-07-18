<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.util.SituationName" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoRole" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>
<html:form action="/editCandidate?method=change">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>  
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID" />  
   	  
   <table>
 		<bean:define id="infoCandidate" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="request"/>
   	<bean:define id="personalInfo" name="infoCandidate" property="infoPerson" />
    	<span class="error"><html:errors/></span>
<h2><bean:message key="label.person.title.changePersonalInfo" /></h2>
       	<!-- Dados Pessoais -->
<table width="100%" cellspacing="0">    
    <tr>
    	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.person.title.personal.info" /></h2></td>
    </tr>
</table>
<br />
<table width="100%"> 
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
         <td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
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
                <html:options collection="<%= SessionConstants.SEX_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        <!-- Nacionalidade -->
        <tr>
         <td width="30%"><bean:message key="label.person.nationality" /></td>
         <td>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.nationality" property="nationality">
                <html:options collection="<%= SessionConstants.NATIONALITY_LIST_KEY %>" property="value" labelProperty="label"/>
             </html:select>          
         </td>
        </tr>
        
</table>
<br />
        <!-- Morada -->
<table width="100%" cellspacing="0"> 
     <tr>
     	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.person.title.addressInfo" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
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
<br />
     <!-- Contactos -->        
<table width="100%" cellspacing="0"> 
     <tr>
     	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.person.title.contactInfo" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
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
<br />
	 <!-- Informacao de Licenciatura -->
<table width="100%" cellspacing="0"> 
     <tr>
        <td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.candidate.majorDegreeInfo" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
	   <tr>
        <td width="30%"><bean:message key="label.candidate.majorDegree" />:</td>
        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.majorDegree" property="majorDegree"/></td>
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
                <html:options collection="<%= SessionConstants.CANDIDATE_SITUATION_LIST %>" property="value" labelProperty="label"/>
             </html:select>          
		 </td>
        </tr>
   </table>
   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Alterar" property="Alterar">Alterar Dados</html:submit>
   
   <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.Reset" property="Reset">Dados Originais</html:reset>
   
   <% InfoMasterDegreeCandidate infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) request.getAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE); 
      if (!infoMasterDegreeCandidate.getInfoCandidateSituation().getSituation().equals(SituationName.PRE_CANDIDATO_STRING)) {	%>

		<bean:define id="link">/editCandidate.do?method=changePassword&candidateID=
			<bean:write name="infoCandidate" property="idInternal"/>
		</bean:define>

        <html:link page='<%= pageContext.findAttribute("link").toString() %>' target="_blank">
       		<bean:message key="link.masterDegree.administrativeOffice.changePassword" />
       	</html:link>
   <% } %>
   	<br>
	</html:form>