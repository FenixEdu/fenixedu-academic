<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Operador" />
  <tiles:put name="serviceName" value="Operador" />
  <tiles:put name="navLocal" value="/operator/operatorMainMenu.jsp" />
  <tiles:put name="navGeral" value="/operator/commonNavGeralOperator.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/operator/password/choosePersonPage_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

