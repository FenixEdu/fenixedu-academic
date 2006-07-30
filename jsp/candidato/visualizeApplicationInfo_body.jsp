<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>

<h2><bean:message key="label.candidate.applicationInfoSituation" /></h2>
    <table>
    <bean:define id="applicationInfo" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE %>" />
        <logic:present name="applicationInfo">
          <!-- Nome -->
          <tr>
            <td><bean:message key="label.candidate.name" />:</td>
            <td><bean:write name="applicationInfo" property="infoPerson.nome"/></td>
          </tr>
          <!-- Numero de Candidatura -->
          <tr>
            <td><bean:message key="label.candidate.candidateNumber" />:</td>
            <td><bean:write name="applicationInfo" property="candidateNumber"/></td>
          </tr>
          <!-- Ano de Candidatura -->
          <tr>
            <td><bean:message key="label.candidate.applicationYear" />:</td>
            <td><bean:write name="applicationInfo" property="infoExecutionDegree.infoExecutionYear.year"/></td>
          </tr>
          <!-- Curso Pretendido -->
          <tr>
            <td><bean:message key="label.candidate.degreeName" />:</td>
            <td><bean:write name="applicationInfo" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - <bean:write name="applicationInfo" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/></td>
          </tr>
          <!-- Especializacao -->
          <tr>
            <td><bean:message key="label.candidate.specialization" />:</td>
            <td><bean:message name="applicationInfo" property="specialization.name" bundle="ENUMERATION_RESOURCES"/></td>
          </tr>
          <!-- Specialization Area -->
          <tr>
            <td><bean:message key="label.candidate.specializationArea" />:</td>
            <td><bean:write name="applicationInfo" property="specializationArea"/></td>
          </tr>
     </table>
     <br />
     <table>
          <!-- Situacao da Candidatura -->
          <logic:present name="applicationInfo" property="infoCandidateSituation">
	    	  <tr>
	            <td><h2><span class="bluetxt"><bean:message key="label.candidate.applicationSituation" /></span></h2></td>
	          </tr>
	          <!-- Situacao -->
	          <tr>
	            <td><bean:message key="label.candidate.infoCandidateSituation" />:</td>
	            <td><bean:write name="applicationInfo" property="infoCandidateSituation.situation"/></td>
	          </tr>
	          <!-- Data da Situacao -->
	          <tr>
	            <td><bean:message key="label.candidate.infoCandidateSituationDate" />:</td>
	            <logic:present name="applicationInfo" property="infoCandidateSituation.date" >
		            <bean:define id="date" name="applicationInfo" property="infoCandidateSituation.date" />
					<td><%= Data.format2DayMonthYear((Date) date) %></td>   
				</logic:present>
	          </tr>
	          <!-- Observacoes -->
	          <tr>
	            <td><bean:message key="label.candidate.infoCandidateSituationRemarks" />:</td>
	            <td><bean:write name="applicationInfo" property="infoCandidateSituation.remarks"/></td>
	          </tr>
	      </logic:present>
	      <logic:notPresent name="applicationInfo" property="infoCandidateSituation">
	    	  <tr>
	            <td colspan="2"><h2><bean:message key="label.candidate.applicationSituation" /></h2></td>
	          </tr>
	          <!-- Situacao -->
	          <tr>
				<bean:define id="supportMail" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/>?subject=Situacao Indefinida (pos-graduacao)</bean:define>
	            <td colspan="2"><span class="error"><!-- Error messages go here -->Não tem situação activa. Contacte-nos clicando <a href="<%= supportMail %>">aqui</a></span></td>
	          </tr>
	
	      </logic:notPresent>
   	  </table>
   	  <br />
   	      <!-- Informacao de Licenciatura -->
      <table>
          <tr>
            <td><h2><span class="bluetxt"><bean:message key="label.candidate.majorDegreeInfo" /></span></h2></td>
          </tr>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegree" />:</td>
            <td><bean:write name="applicationInfo" property="majorDegree"/></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeYear" />:</td>
            <td><bean:write name="applicationInfo" property="majorDegreeYear"/></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeSchool" />:</td>
            <td><bean:write name="applicationInfo" property="majorDegreeSchool"/></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="label.candidate.average" />:</td>
            <td><bean:write name="applicationInfo" property="average"/> <bean:message key="label.candidate.values"/></td>
          </tr>
        </logic:present>
    </table>