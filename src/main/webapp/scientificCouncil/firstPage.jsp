<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column.teacher" beanName="" flush="true">
  <tiles:put name="title" value="private.scientificcouncil" />
  <tiles:put name="serviceName" value="Portal do Conselho Científico" />
  <tiles:put name="navLocal" value="/scientificCouncil/navigation.jsp" />
  <tiles:put name="navGeral" value="/scientificCouncil/commonNavGeral.jsp" />
  <tiles:put name="body" value="/scientificCouncil/firstPage_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
