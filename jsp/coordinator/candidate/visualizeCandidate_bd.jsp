<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.State" %>
<%@ page import="DataBeans.InfoCandidateSituation" %>

     <bean:define id="personalInfo" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="session" property="infoPerson"/>
     <bean:define id="masterDegreeCandidate" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" scope="session"/>

     <table>

        <logic:present name="personalInfo">
          <!-- Name -->
          <tr>
            <td><bean:message key="label.person.name" /></td>
            <td><bean:write name="personalInfo" property="nome"/></td>
          </tr>
          <!-- Candidate Number -->
          <tr>
            <td><bean:message key="label.candidate.candidateNumber" /></td>
            <td><bean:write name="masterDegreeCandidate" property="candidateNumber"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.candidate.specialization" /></td>
            <td><bean:write name="masterDegreeCandidate" property="specialization"/></td>
          </tr>

          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      

          <!-- Contactos -->
          <tr>
            <td><h2><bean:message key="label.person.title.contactInfo" /><h2></td>
          </tr>
          <!-- Telefone -->
          <tr>
            <td><bean:message key="label.person.telephone" /></td>
            <td><bean:write name="personalInfo" property="telefone"/></td>
          </tr>
          <!-- Telemovel -->
          <tr>
            <td><bean:message key="label.person.mobilePhone" /></td>
            <td><bean:write name="personalInfo" property="telemovel"/></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
            <td><bean:write name="personalInfo" property="email"/></td>
          </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><bean:write name="personalInfo" property="enderecoWeb"/></td>
          </tr>
          
          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      

   	      <!-- Informacao de Licenciatura -->
          <tr>
            <td><h2><bean:message key="label.candidate.majorDegreeInfo" /><h2></td>
          </tr>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegree" /></td>
            <td><bean:write name="masterDegreeCandidate" property="majorDegree"/></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeYear" /></td>
            <td><bean:write name="masterDegreeCandidate" property="majorDegreeYear"/></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeSchool" /></td>
            <td><bean:write name="masterDegreeCandidate" property="majorDegreeSchool"/></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="label.candidate.average" /></td>
            <td><bean:write name="masterDegreeCandidate" property="average"/> <bean:message key="label.candidate.values"/></td>
          </tr>

          <tr></tr>
          <tr></tr>      
          <tr></tr>
          <tr></tr>      

          <tr>
            <td><h2><bean:message key="label.masterDegree.administrativeOffice.situationHistory" /><h2></td>
          </tr>

         <logic:iterate id="situation" name="masterDegreeCandidate" property="situationList">
         	<% if (((InfoCandidateSituation) situation).getValidation().equals(new State(State.ACTIVE))) { %>
         		<td><center><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></center></td>
         	<% } %>
           <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
            <td><bean:write name="situation" property="situation"/></td>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
            <td><bean:write name="situation" property="date"/></td>
		   </tr>
		   <tr>
            <td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
            <td><bean:write name="situation" property="remarks"/></td>
		   </tr>
          <tr></tr>
          <tr></tr>
          <tr></tr>

         </logic:iterate>

        </logic:present>
     </table>



