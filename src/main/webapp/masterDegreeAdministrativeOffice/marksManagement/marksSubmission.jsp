<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column.posGrad" beanName="" flush="true">
  <tiles:put name="title" value="Secretaria de Pós-Graduação167" />
  <tiles:put name="serviceName" value="Secretaria de Pós-Graduação" />
  <tiles:put name="navLocal" value="/masterDegreeAdministrativeOffice/pgMainMenu.jsp" />
  <tiles:put name="navGeral" value="/masterDegreeAdministrativeOffice/commonNavGeralPosGraduacao.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/masterDegreeAdministrativeOffice/marksManagement/marksSubmission_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>