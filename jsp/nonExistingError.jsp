<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/errorLayout.jsp" flush="true">
  <tiles:put name="title" value=".IST" />
  <tiles:put name="serviceName" value=".IST - Página de Erro" />
  <tiles:put name="body" value="/nonExistingError_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>