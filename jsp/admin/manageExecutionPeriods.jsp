<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Administração" />
  <tiles:put name="serviceName" value="Portal do Administrador" />
  <tiles:put name="navLocal" value="/admin/commons/commonNavLocalAdmin.jsp" />
  <tiles:put name="navGeral" value="/admin/commons/commonNavGeralAdmin.jsp" />
  <tiles:put name="body" value="/admin/manageExecutionPeriods_bd.jsp" />
  <tiles:put name="footer" value="/admin/commons/commonFooterAdmin.jsp" />
</tiles:insert>