<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Coordenador" />
  <tiles:put name="serviceName" value="Portal do Coordenador" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="navLocal" value="" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/coordinator/error_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>