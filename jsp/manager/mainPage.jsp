<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - &Aacute;rea de Ger&ecircncia" />
  <tiles:put name="serviceName" value="&Aacute;rea de Ger&ecircncia" />
  <tiles:put name="navLocal" value="/manager/mainMenu.jsp" />
  <tiles:put name="navGeral" value="/manager/commonNavGeralManager.jsp" />
  <tiles:put name="body" value="/manager/welcomScreen.jsp" />
  <tiles:put name="footer" value="/manager/commonFooter.jsp" />
</tiles:insert>