<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Secretaria de Graduação"/>
  <tiles:put name="serviceName" value="Secretaria de Graduação"/>
  <tiles:put name="navLocal" value="/degreeAdministrativeOffice/pgMainMenu.jsp"/>
  <tiles:put name="navGeral" value="/degreeAdministrativeOffice/commonNavGeral.jsp"/>
  <tiles:put name="body-context" value=""/>
  <tiles:put name="body" value="/degreeAdministrativeOffice/welcomeScreen.jsp"/>
  <tiles:put name="footer" value="/degreeAdministrativeOffice/copyrightDefault.jsp"/>
</tiles:insert>
