<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Tesouraria" />
  <tiles:put name="serviceName" value="Portal da Tesouraria" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="navLocal" value="" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/treasury/notAuthorizedError_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>