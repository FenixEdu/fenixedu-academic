<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Date" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.State" %>
<%@ page import="Util.Data" %>
<%@ page import="DataBeans.InfoCandidateSituation" %>
<bean:define id="personalInfo" name="masterDegreeCandidate" scope="request" property="infoPerson"/>
<bean:define id="masterDegreeCandidate" name="masterDegreeCandidate" scope="request"/>
<jsp:include page="../context.jsp"/>
<h2><bean:message key="title.candidate.info" /></h2>
	<table>
        <logic:present name="personalInfo">
          <!-- Name -->
          <tr>
	            <td><bean:message key="label.person.name" /></td>
	            <td><span class="greytxt"><bean:write name="personalInfo" property="nome"/></span></td>
          </tr>
          <!-- Candidate Number -->
          <tr>
	            <td><bean:message key="label.candidate.candidateNumber" />:</td>
	            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="candidateNumber"/></span></td>
          </tr>
          <!-- Specialization -->
          <tr>
	            <td><bean:message key="label.candidate.specialization" />:</td>
	            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="specialization"/></span></td>
          </tr>
          <!-- Specialization Area -->
          <tr>
	            <td><bean:message key="label.candidate.specializationArea" />:</td>
	            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="specializationArea"/></span></td>
          </tr>
    </table>
    <br />
          <!-- Contactos -->
   	<table width="100%" cellspacing="0">  
         <tr>
         	<td class="infoop" width="50px"><span class="emphasis-box">1</span>
            <td class="infoop"><strong><bean:message key="label.person.title.contactInfo" /></strong></td>
          </tr>
     </table>
     <br />
     <table>
          <!-- Telefone -->
          <tr>
            <td><bean:message key="label.person.telephone" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="telefone"/></span></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td><bean:message key="label.person.mobilePhone" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="telemovel"/></span></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="email"/></span></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><span class="greytxt"><bean:write name="personalInfo" property="enderecoWeb"/></span></td>
          </tr>
	</table>
	<br />
   	      <!-- Informacao de Licenciatura -->
	<table width="100%" cellspacing="0">    
          <tr>
          	<td class="infoop" width="50px"><span class="emphasis-box">2</span>
            <td class="infoop"><strong><bean:message key="label.candidate.majorDegreeInfo" /></strong></td>
          </tr>
    </table>
    <br />
    <table>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegree" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegree"/></span></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeYear" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegreeYear"/></span></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeSchool" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="majorDegreeSchool"/></span></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="label.candidate.average" />:</td>
            <td><span class="greytxt"><bean:write name="masterDegreeCandidate" property="average"/> <bean:message key="label.candidate.values"/></span></td>
          </tr>
    </table>
    <br />
	<table width="100%" cellspacing="0">
		<tr>
			<td class="infoop" width="50px"><span class="emphasis-box">3</span>
            <td class="infoop"><strong><bean:message key="label.masterDegree.administrativeOffice.situationHistory" /></strong></td>
         </tr>
   </table>
   <br />
   <table>
         <logic:iterate id="situation" name="masterDegreeCandidate" property="situationList">
         	<% if (((InfoCandidateSituation) situation).getValidation().equals(new State(State.ACTIVE))) { %>
         		<td><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></td>
         	<% } %>
           <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situation" />:</td>
            <td><span class="greytxt"><bean:write name="situation" property="situation"/></span></td>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situationDate" />:</td>
            <logic:present name="situation" property="date" >
	            <bean:define id="date" name="situation" property="date" />
				<td><%= Data.format2DayMonthYear((Date) date) %></td>             
			</logic:present>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.remarks" />:</td>
            <td><span class="greytxt"><bean:write name="situation" property="remarks"/></span></td>
		   </tr>
         </logic:iterate>
        </logic:present>
     </table>