<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
  <head>
    <title><bean:message key="title.candidate.visualizeApplicationInfo" /></title>
  </head>
  <body>
    <table>
    <bean:define id="applicationInfo" name="<%= SessionConstants.APPLICATION_INFO %>" scope="request" />
        <logic:present name="applicationInfo">
        
          <!-- Nome -->
          <tr>
            <td><bean:message key="label.candidate.name" /></td>
            <td><bean:write name="applicationInfo" property="infoPerson.nome"/></td>
          </tr>

          <!-- Informacao de Candidatura -->
    	  <tr>
            <td><h2><bean:message key="label.candidate.applicationInfoSituation" /></h2></td>
          </tr>
          <!-- Numero de Candidatura -->
          <tr>
            <td><bean:message key="label.candidate.candidateNumber" /></td>
            <td><bean:write name="applicationInfo" property="candidateNumber"/></td>
          </tr>
          <!-- Ano de Candidatura -->
          <tr>
            <td><bean:message key="label.candidate.applicationYear" /></td>
            <td><bean:write name="applicationInfo" property="infoExecutionDegree.infoExecutionYear.year"/></td>
          </tr>
          <!-- Curso Pretendido -->
          <tr>
            <td><bean:message key="label.candidate.degreeName" /></td>
            <td><bean:write name="applicationInfo" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - <bean:write name="applicationInfo" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/></td>
          </tr>
          <!-- Especializacao -->
          <tr>
            <td><bean:message key="label.candidate.specialization" /></td>
            <td><bean:write name="applicationInfo" property="specialization"/></td>
          </tr>


          <!-- Situacao da Candidatura -->
    	  <tr>
            <td><h2><bean:message key="label.candidate.applicationInfoSituation" /></h2></td>
          </tr>

          <!-- Situacao -->
          <tr>
            <td><bean:message key="label.candidate.infoCandidateSituation" /></td>
            <td><bean:write name="applicationInfo" property="infoCandidateSituation.situation"/></td>
          </tr>

          <!-- Data da Situacao -->
          <tr>
            <td><bean:message key="label.candidate.infoCandidateSituationDate" /></td>
            <td><bean:write name="applicationInfo" property="infoCandidateSituation.date"/></td>
          </tr>

          <!-- Observacoes -->
          <tr>
            <td><bean:message key="label.candidate.infoCandidateSituationRemarks" /></td>
            <td><bean:write name="applicationInfo" property="infoCandidateSituation.remarks"/></td>
          </tr>


   	      <!-- Informacao de Licenciatura -->
          <tr>
            <td><h2><bean:message key="label.candidate.majorDegreeInfo" /><h2></td>
          </tr>
          <!-- Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegree" /></td>
            <td><bean:write name="applicationInfo" property="majorDegree"/></td>
          </tr>
          <!-- Ano de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeYear" /></td>
            <td><bean:write name="applicationInfo" property="majorDegreeYear"/></td>
          </tr>
          <!-- Escola de Licenciatura -->
          <tr>
            <td><bean:message key="label.candidate.majorDegreeSchool" /></td>
            <td><bean:write name="applicationInfo" property="majorDegreeSchool"/></td>
          </tr>
          <!-- Media -->
          <tr>
            <td><bean:message key="label.candidate.average" /></td>
            <td><bean:write name="applicationInfo" property="average"/> <bean:message key="label.candidate.values"/></td>
          </tr>
        </logic:present>
    </table>
    
  </body>
</html>
