<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_1col.jsp" flush="true">
  <tiles:put name="title" value="Portal de Coordenador"/>
  <tiles:put name="serviceName" value="Portal de Coordenador"/>
  <tiles:put name="navGeral" value="/coordinator/commonNavGeralCoordinator.jsp"/>
  <tiles:put name="navLocal" value=""/>  
  <tiles:put name="body-context" value=""/>
  <tiles:put name="body" value="/coordinator/chooseDegreePage_bd.jsp"/>
  <tiles:put name="footer" value="/copyright.jsp"/>
</tiles:insert>