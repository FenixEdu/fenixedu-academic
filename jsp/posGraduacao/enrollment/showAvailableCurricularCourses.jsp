<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_posGrad.jsp" flush="true">
  <tiles:put name="title" value=".IST - Secretaria de Pós-Graduação"/>
  <tiles:put name="serviceName" value="Secretaria de Pós-Graduação"/>
  <tiles:put name="navLocal" value="/posGraduacao/student/studentMenu.jsp"/>
  <tiles:put name="navGeral" value="/posGraduacao/commonNavGeralPosGraduacao.jsp"/>
  <tiles:put name="body-context" value="/degreeAdministrativeOffice/enrolment/enrolmentContextWithoutRules.jsp"/>    
  <tiles:put name="body" value="/degreeAdministrativeOffice/enrolment/showAvailableCurricularCoursesWithoutRules.jsp"/>
  <tiles:put name="footer" value="/posGraduacao/copyrightDefault.jsp"/>
</tiles:insert>
