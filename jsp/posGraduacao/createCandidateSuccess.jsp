<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
  <head>
    <title><bean:message key="candidate.titleVisualizeApplicationInfo" /></title>
  </head>
  <body>
    <div align="center">
     <font color="#023264" size="-1">
        <h2>          Candidato Criado !         </h2>
      </font>
    </div>
    <table>
      <logic:present name="newCandidate">
          <!-- Name -->
          <tr>
            <td><bean:message key="label.name" /></td>
            <td><bean:write name="newCandidate" property="name"/></td>
          </tr>

          <!-- Candidate Number -->
          <tr>
            <td><bean:message key="label.candidate.number" /></td>
            <td><bean:write name="newCandidate" property="candidateNumber"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.specialization" /></td>
            <td><bean:write name="newCandidate" property="specialization"/></td>
          </tr>

          <!-- Degree  -->
          <tr>
            <td><bean:message key="label.degree" /></td>
            <td><bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> - 
                <bean:write name="newCandidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
            </td>
          </tr>

          <!-- Identification Document Number -->
          <tr>
            <td><bean:message key="label.identificationDocumentNumber" /></td>
            <td><bean:write name="newCandidate" property="identificationDocumentNumber"/></td>
          </tr>

          <!-- Specialization -->
          <tr>
            <td><bean:message key="label.identificationDocumentType" /></td>
            <td><bean:write name="newCandidate" property="infoIdentificationDocumentType"/></td>
          </tr>

      </logic:present>
    </table>
  </body>
</html>

