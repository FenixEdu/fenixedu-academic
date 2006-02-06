<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Tesouraria" />
  <tiles:put name="serviceName" value="Portal da Tesouraria" />
  <tiles:put name="navLocal" value="/treasury/treasuryMainMenu.jsp" />
  <tiles:put name="navGeral" value="/treasury/globalNav.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/treasury/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

