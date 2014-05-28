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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.ListCandidatesDispatchAction" %>
<%@ page import="net.sourceforge.fenixedu.domain.ApplicationDocumentType"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>

<bean:define id="personalInfo" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE %>" scope="request" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE %>" scope="request"/>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:define id="link">/editCandidate.do?method=prepareEdit&candidateID=</bean:define>

<h2><bean:message key="label.candidate"/></h2>


<table class="tstyle2">
	<logic:present name="personalInfo">
  	<!-- Nome -->
   	<tr>
   		<td width="30%"><bean:message key="label.person.name" /></td>
      	<td class="greytxt"><bean:write name="personalInfo" property="nome"/></td>
   	</tr>
    <!-- Username -->
    <tr>
    	<td width="30%"><bean:message key="label.person.username" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="username"/></td>
    </tr>
	<!-- Application Number -->
    <tr>
    	<td width="30%"><bean:message key="label.candidate.candidateNumber" />:</td>
        <td class="greytxt"><bean:write name="masterDegreeCandidate" property="candidateNumber"/></td>
    </tr>
    <!-- Specialization Area -->
    <tr>
        <td width="30%"><bean:message key="label.candidate.specializationArea" /></td>
        <td class="greytxt"><bean:write name="masterDegreeCandidate" property="specializationArea"/></td>
    </tr>
</table>

     
          
 	<!-- Dados Pessoais -->

<h3 class="separator2 mtop15"><bean:message key="label.person.title.personal.info" /></h3>


<table class="tstyle2">  
    <!-- Sexo -->
    <tr>
    	<td width="30%"><bean:message key="label.person.sex" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="sexo"/></td>
    </tr>
 	<!-- Numero do Documento de Identificacao -->
    <tr>
    	<td width="30%"><bean:message key="label.person.identificationDocumentNumber" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="numeroDocumentoIdentificacao"/></td>
   	</tr>
    <!-- Tipo do Documento de Identificacao -->
    <tr>
    	<td width="30%"><bean:message key="label.person.identificationDocumentType" /></td>
        <td class="greytxt">
        	<bean:define id="idType" name="personalInfo" property="tipoDocumentoIdentificacao"/>
        	<bean:message key='<%=idType.toString()%>'/>
        </td>
    </tr>
    <!-- Local de Emissao do Documento de Identificacao -->
    <tr>
     	<td width="30%"><bean:message key="label.person.identificationDocumentIssuePlace" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="localEmissaoDocumentoIdentificacao"/></td>
  	</tr>
    <!-- Data de Emissao do Documento de Identificacao -->
    <tr>
    	<td width="30%"><bean:message key="label.person.identificationDocumentIssueDate" /></td>
            <logic:present name="personalInfo" property="dataEmissaoDocumentoIdentificacao" >
	            <bean:define id="date" name="personalInfo" property="dataEmissaoDocumentoIdentificacao" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) 
%></td>   
			</logic:present>
   	</tr>
    <!-- Data de Validade do Documento de Identificacao -->
    <tr>
    	<td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
        	<logic:present name="personalInfo" property="dataValidadeDocumentoIdentificacao" >
	    		<bean:define id="date" name="personalInfo" property="dataValidadeDocumentoIdentificacao" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) 
%></td> 
			</logic:present>
   	</tr>
    <!-- Numero de Contribuinte -->
    <tr>
    	<td width="30%"><bean:message key="label.person.contributorNumber" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="numContribuinte"/></td>
    </tr>
    <!-- Estado Civil -->
    <tr>
    	<td width="30%"><bean:message key="label.person.maritalStatus" /></td>
        <bean:define id="maritalStatus" name="personalInfo" property="maritalStatus"/>
		<td class="greytxt"><bean:message key='<%= maritalStatus.toString() %>'/></td>
    </tr>
    <!-- Data de Nascimento -->
    <tr>
    	<td width="30%"><bean:message key="label.person.birth" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) 
%></td> 
			</logic:present>
     </tr>
     <!-- Nacionalidade -->
    
    
     <tr>
     	<td width="30%"><bean:message key="label.person.country" /></td>
     	 <logic:present name="personalInfo"  property="infoPais">
	     	<td class="greytxt"><bean:write name="personalInfo" property="infoPais.nationality"/></td>
         </logic:present>
     	 <logic:notPresent name="personalInfo"  property="infoPais">
	     	<td class="greytxt"></td>
         </logic:notPresent>

     </tr>        
     <!-- Freguesia de Naturalidade -->
     <tr>
     	<td width="30%"><bean:message key="label.person.birthPlaceParish" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="freguesiaNaturalidade"/></td>
   	 </tr>
     <!-- Concelho de Naturalidade -->
     <tr>
     	<td width="30%"><bean:message key="label.person.birthPlaceMunicipality" /></td>
      	<td class="greytxt"><bean:write name="personalInfo" property="concelhoNaturalidade"/></td>
     </tr>
     <!-- Distrito de Naturalidade -->
     <tr>
     	<td width="30%"><bean:message key="label.person.birthPlaceDistrict" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="distritoNaturalidade"/></td>
   	 </tr>
     <!-- Nome do Pai -->
     <tr>
     	<td width="30%"><bean:message key="label.person.fatherName" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="nomePai"/></td>
   	 </tr>
     <!-- Nome da Mae -->
     <tr>
     	<td width="30%"><bean:message key="label.person.motherName" /></td>
     	<td class="greytxt"><bean:write name="personalInfo" property="nomeMae"/></td>
   	 </tr>
     <!-- Profissao -->
     <tr>
     	<td width="30%"><bean:message key="label.person.occupation" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="profissao"/></td>
     </tr>
</table>



<!-- Dados de Residencia -->

<h3 class="separator2 mtop15"><bean:message key="label.person.title.addressInfo" /></h3>

<table class="tstyle2">
     <!-- Morada -->
     <tr>
     	<td width="30%"><bean:message key="label.person.address" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="morada"/></td>
     </tr>
     <!-- Codigo Postal -->
     <tr>
     	<td width="30%"><bean:message key="label.person.postCode" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="codigoPostal"/></td>
     </tr>
     <!-- Area do Codigo Postal -->
     <tr>
     	<td width="30%"><bean:message key="label.person.areaOfPostCode" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="localidadeCodigoPostal"/></td>
   	 </tr>
     <!-- Localidade de Residencia -->
     <tr>
     	<td width="30%"><bean:message key="label.person.place" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="localidade"/></td>
     </tr>
     <!-- Freguesia de Residencia -->
     <tr>
     	<td width="30%"><bean:message key="label.person.addressParish" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="freguesiaMorada"/></td>
     </tr>
     <!-- Concelho de Residencia -->
     <tr>
     	<td width="30%"><bean:message key="label.person.addressMunicipality" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="concelhoMorada"/></td>
     </tr>
     <!-- Distrito de Residencia -->
     <tr>
     	<td width="30%"><bean:message key="label.person.addressDistrict" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="distritoMorada"/></td>
     </tr>
</table>
<br />
     <!-- Contactos -->
     
<h3 class="separator2 mtop15"><bean:message key="label.person.title.contactInfo" /></h3>


<table class="tstyle2">
     <!-- Telefone -->
     <tr>
     	<td width="30%"><bean:message key="label.person.telephone" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="telefone"/></td>
     </tr>
     <!-- Telemovel -->
     <tr>
     	<td width="30%"><bean:message key="label.person.mobilePhone" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="telemovel"/></td>
     </tr>
     <!-- E-Mail -->
     <tr>
     	<td width="30%"><bean:message key="label.person.email" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="email"/></td>
   	 </tr>
     <!-- WebPage -->
     <tr>
     	<td width="30%"><bean:message key="label.person.webSite" /></td>
        <td class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></td>
     </tr>
</table>

	 <!-- Informacao de Licenciatura -->

<h3 class="separator2 mtop15"><bean:message key="label.candidate.majorDegreeInfo" /></h3>

<table class="tstyle2">
     <!-- Licenciatura -->
     <tr>
     	<td width="30%"><bean:message key="label.candidate.majorDegree" />:</td>
        <td class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegree"/></td>
     </tr>
     <!-- Ano de Licenciatura -->
     <tr>
     	<td width="30%"><bean:message key="label.candidate.majorDegreeYear" />:</td>
        <td class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegreeYear"/></td>
     </tr>
     <!-- Escola de Licenciatura -->
     <tr>
     	<td width="30%"><bean:message key="label.candidate.majorDegreeSchool" />:</td>
        <td class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegreeSchool"/></td>
     </tr>
     <!-- Media -->
     <tr>
     	<td width="30%"><bean:message key="label.candidate.average" />:</td>
        <td class="greytxt"><bean:write name="masterDegreeCandidate" property="average"/> <bean:message key="label.candidate.values"/></td>
     </tr>
</table>


<h3 class="separator2 mtop15"><bean:message key="label.masterDegree.administrativeOffice.situationHistory" /></h3>


     	<logic:iterate id="situation" name="masterDegreeCandidate" property="situationList">
         	<% if (((InfoCandidateSituation) situation).getValidation().equals(new State(State.ACTIVE))) { 
%>
        <p class="mbottom05"><strong><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></strong></p>
         	<% } 
%>
<table class="tstyle2">
     <tr>
     	<td width="30%"><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
        <td class="greytxt"><bean:write name="situation" property="situation"/></td>
     </tr>
	 <tr>
       	<td width="30%"><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
        	<bean:define id="date" name="situation" property="date" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) 
%></td>             
	 </tr>
	 <tr>
     	<td width="30%"><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
        <td class="greytxt"><bean:write name="situation" property="remarks"/></td>
	 </tr>
	 </logic:iterate>
</table>




<h3 class="separator2 mtop15"><bean:message key="label.masterDegree.studyPlan.studyPlanForCandidate"/></h3>


<table class="tstyle2">
		<tr>
			<td style="vertical-align: top;">
			<bean:message key="label.masterDegree.studyPlan.givenCredits"/>
			</td>
			<td><bean:write name="masterDegreeCandidate" property="givenCredits"/></td>
         </tr>
		<tr>
			<td style="vertical-align: top;">
				<bean:message key="label.masterDegree.studyPlan.givenCreditsRemarks"/>
			</td>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.givenCreditsRemarks" disabled="true" name="masterDegreeCandidate" property="givenCreditsRemarks"/>
			</td>
		</tr>
</table>


			<logic:present name="studyPlan">
            <p class="mbottom05"><b><bean:message key="label.masterDegree.studyPlan.listOfCurricularCourses"/>:</b></p>
			<table class="tstyle2 mtop05">
				<tr>
					<td width="70%"><bean:message key="label.masterDegree.studyPlan.curricularCourseName"/></td>
					<td width="30%"><bean:message key="label.masterDegree.studyPlan.curricularCourseCredits"/></td>
				</tr>
			<logic:iterate id="StudyPlanItem" name="studyPlan">
				<bean:define id="infoCourse" name="StudyPlanItem" property="infoCurricularCourse"/>
				<tr>
					<td><bean:write name="infoCourse" property="name"/></td>
					<td><bean:write name="infoCourse" property="credits"/></td>
				</tr>
			</logic:iterate>
            </table>
	</logic:present>

			<logic:notPresent name="studyPlan">
				<bean:message key="label.masterDegree.studyPlan.noStudyPlanForCandidate"/>
			</logic:notPresent>



<h3 class="separator2 mtop15"><bean:message key="label.masterDegree.studyPlan.isClassAssistantTitle"/></h3>


<table class="tstyle2">
    <tr>
        <td>
            <bean:message key="label.masterDegree.studyPlan.isClassAssistant"/>
        </td>
        <td>
            <html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.courseAssistant" disabled="true" name="masterDegreeCandidate" property="courseAssistant"/>
        </td>
    </tr>
    <tr>
        <td>
            <bean:message key="label.masterDegree.studyPlan.executionCoursesToAssist"/>
        </td>
        <td>
            <html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.coursesToAssist" disabled="true" name="masterDegreeCandidate" property="coursesToAssist" cols="40" rows="2"/>
        </td>
    </tr>
</table>



<h3 class="separator2 mtop15"><bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorTitle"/></h3>


<logic:notEmpty name="masterDegreeCandidate" property="infoGuider">

<table class="tstyle2">
    <tr>
        <td>
            <bean:define id="infoTeacher" name="masterDegreeCandidate" property="infoGuider"/>
            <bean:define id="infoPersonTeacher" name="infoTeacher" property="infoPerson"/>
            <bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorName"/><bean:write name="infoPersonTeacher" property="nome"/>
        </td>
    </tr>
    <tr>
        <td>
            <bean:message key="label.masterDegree.studyPlan.advisorOrCoordinatorRole"/>
        </td>
    </tr>
    <tr>
        <td>
            <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.hasGuider" disabled="true" name="masterDegreeCandidate" property="hasGuider" value="true"/><bean:message key="label.masterDegree.studyPlan.advisor"/>
        </td>
    </tr>
    <tr>
        <td>
            <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.hasGuider" disabled="true" name="masterDegreeCandidate" property="hasGuider" value="false"/><bean:message key="label.masterDegree.studyPlan.coordinator"/>
        </td>
    </tr>
</table>

</logic:notEmpty>

<logic:empty name="masterDegreeCandidate" property="infoGuider">
	<p>
	    <em><bean:message key="label.masterDegree.studyPlan.NoAdvisorOrCoordinatorTitle"/></em>
    </p>
</logic:empty>
<bean:define id="candidateID" name="masterDegreeCandidate" property="externalId" />
	


<h3 class="separator2 mtop15"><bean:message key="label.masterDegree.applicationDocuments"/></h3>


<table width="100%">
				<tr>
					<td>
						<html:link action='<%= "/visualizeCandidates.do?method=showApplicationDocuments&documentType=CURRICULUM_VITAE&candidateID=" + candidateID %>'><bean:message key="label.masterDegree.showCurriculumVitae"/></html:link>
					</td>
				</tr>
				<tr>
					<td>
						<html:link action='<%= "/visualizeCandidates.do?method=showApplicationDocuments&documentType=HABILITATION_CERTIFICATE&candidateID=" + candidateID %>' ><bean:message key="label.masterDegree.showHabilitationLetter"/></html:link>
					</td>
				</tr>
				<tr>
					<td>
						<html:link action='<%= "/visualizeCandidates.do?method=showApplicationDocuments&documentType=SECOND_HABILITATION_CERTIFICATE&candidateID=" + candidateID %>' ><bean:message key="label.masterDegree.showSecondHabilitationLetter"/></html:link>
					</td>
				</tr>
				<tr>
					<td>
						<html:link action='<%= "/visualizeCandidates.do?method=showApplicationDocuments&documentType=INTEREST_LETTER&candidateID=" + candidateID %>' ><bean:message key="label.masterDegree.showManifestationLetter"/></html:link>
					</td>
				</tr>
</table>
    
	</logic:present>

	<bean:define id="candidateLink">
		<bean:write name="link"/><bean:write name="candidateID"/>
	</bean:define>
	
	<ul class="mtop2">
		<li>
		    <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
		    	<bean:message key="link.masterDegree.administrativeOffice.editCandidate" />
		    </html:link>    
	    </li>
    </ul>

 