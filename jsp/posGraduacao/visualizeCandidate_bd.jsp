<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>

<bean:define id="personalInfo" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="request" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="request"/>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
<bean:define id="link">/editCandidate.do?method=prepareEdit&candidateID=</bean:define>
<br />
<table>
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
<br />
     
          
 	<!-- Dados Pessoais -->
<table width="100%" cellspacing="0">    
    <tr>
    	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.person.title.personal.info" /></h2></td>
    </tr>
</table>
<br />
<table width="100%">  
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
        <td class="greytxt"><bean:write name="personalInfo" property="tipoDocumentoIdentificacao"/></td>
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
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>   
			</logic:present>
   	</tr>
    <!-- Data de Validade do Documento de Identificacao -->
    <tr>
    	<td width="30%"><bean:message key="label.person.identificationDocumentExpirationDate" /></td>
        	<logic:present name="personalInfo" property="dataValidadeDocumentoIdentificacao" >
	    		<bean:define id="date" name="personalInfo" property="dataValidadeDocumentoIdentificacao" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td> 
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
        <td class="greytxt"><bean:write name="personalInfo" property="estadoCivil"/></td>
    </tr>
    <!-- Data de Nascimento -->
    <tr>
    	<td width="30%"><bean:message key="label.person.birth" /></td>
            <logic:present name="personalInfo" property="nascimento" >
	            <bean:define id="date" name="personalInfo" property="nascimento" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td> 
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
<br />	
     <!-- Dados de Residencia -->
<table width="100%" cellspacing="0"> 
     <tr>
     	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.person.title.addressInfo" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
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
<table width="100%" cellspacing="0"> 
     <tr>
     	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.person.title.contactInfo" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
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
<br />
	 <!-- Informacao de Licenciatura -->
<table width="100%" cellspacing="0"> 
     <tr>
        <td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.candidate.majorDegreeInfo" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
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
<br />
<table width="100%" cellspacing="0"> 
     <tr>
     	<td class="infoop" colspan="2"><h2 class="inline"><bean:message key="label.masterDegree.administrativeOffice.situationHistory" /></h2></td>
     </tr>
</table>
<br />
<table width="100%">
     	<logic:iterate id="situation" name="masterDegreeCandidate" property="situationList">
         	<% if (((InfoCandidateSituation) situation).getValidation().equals(new State(State.ACTIVE))) { %>
        <td width="30%"><h3 class="inline"><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></h3></td>
         	<% } %>
     <tr>
     	<td width="30%"><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
        <td class="greytxt"><bean:write name="situation" property="situation"/></td>
     </tr>
	 <tr>
       	<td width="30%"><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
        	<bean:define id="date" name="situation" property="date" />
		<td class="greytxt"><%= Data.format2DayMonthYear((Date) date) %></td>             
	 </tr>
	 <tr>
     	<td width="30%"><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
        <td class="greytxt"><bean:write name="situation" property="remarks"/></td>
	 </tr>
	 </logic:iterate>
	</logic:present>
</table>

	<bean:define id="candidateLink">
		<bean:write name="link"/><bean:write name="masterDegreeCandidate" property="idInternal"/>
	</bean:define>

    <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>    	<bean:message key="link.masterDegree.administrativeOffice.editCandidate" />
    </html:link>