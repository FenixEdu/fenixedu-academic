<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Conselho Directivo" />
  <tiles:put name="serviceName" value="Conselho Directivo" />
  <tiles:put name="navLocal" value="/directiveCouncil/pgMainMenu.jsp" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/directiveCouncil/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
