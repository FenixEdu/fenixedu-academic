<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - SOP" />
  <tiles:put name="serviceName" value="Portal Docência" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="navLocal" value="" />
  <tiles:put name="body" value="/sop/error_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>