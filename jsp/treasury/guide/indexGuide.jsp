<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Tesouraria" />
  <tiles:put name="serviceName" value="Portal da Tesouraria" />
  <tiles:put name="navLocal" value="/treasury/guide/guideMenu.jsp" />
  <tiles:put name="navGeral" value="/treasury/globalNav.jsp" />
  <tiles:put name="body" value="/treasury/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/treasury/copyrightDefault.jsp" />
</tiles:insert>
