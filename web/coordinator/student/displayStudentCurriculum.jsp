<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Secretaria de Pós-Graduação" />
  <tiles:put name="serviceName" value="Secretaria de Pós-Graduação" />
  <tiles:put name="navLocal" value="/coordinator/coordinatorMainMenu.jsp" />
  <tiles:put name="navGeral" value="/coordinator/commonNavGeralCoordinator.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/coordinator/student/displayStudentCurriculum_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>