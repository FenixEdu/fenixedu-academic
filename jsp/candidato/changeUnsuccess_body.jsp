<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
  <body>
    <div align="center">
      <font color="#023264" size="-1">
        <h2>          A sua situação encontra-se resolvida, e como tal não pode alterar a sua candidatura !         </h2>
      </font>
      <hr>
    </div>  
   
   <table>
    <logic:present name="situation">
        <!-- Situacao da Candidatura -->
        <tr>
	        <td><h2><bean:message key="candidate.applicationInfoSituation" /></h2></td>
        </tr>
        
        <!-- Situacao -->
        <tr>
            <td><bean:message key="candidate.infoCandidateSituation" /></td>
            <td><bean:write name="situation" property="situation"/></td>
        </tr>
        
        <!-- Data da Situacao -->
        <tr>
            <td><bean:message key="candidate.infoCandidateSituationDate" /></td>
            <td><bean:write name="situation" property="date"/></td>
        </tr>
        
        <!-- Observacoes -->
        <tr>
            <td><bean:message key="candidate.infoCandidateSituationRemarks" /></td>
            <td><bean:write name="situation" property="remarks"/></td>
        </tr>
    </logic:present>
   </table>
  </body>
  
</html>

