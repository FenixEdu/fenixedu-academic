<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_posGrad.jsp" flush="true">
  <tiles:put name="title" value="Secretaria de Pós-Graduação" />
  <tiles:put name="serviceName" value="Secretaria de Pós-Graduação" />
  <tiles:put name="navLocal" value="/masterDegreeAdministrativeOffice/pgMainMenu.jsp" />
  <tiles:put name="navGeral" value="/masterDegreeAdministrativeOffice/commonNavGeralPosGraduacao.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/masterDegreeAdministrativeOffice/marksManagement/marksConfirmationMenu_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>