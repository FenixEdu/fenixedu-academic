<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayoutStudent_2col_photo.jsp" flush="true">
  <tiles:put name="title" value=".IST - portalAdmin" />
  <tiles:put name="serviceName" value="Portal de Administração" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="photos" value="/admin/commonEntrPhotosAdmin.jsp" />
  <tiles:put name="body" value="/admin/error_bd.jsp" />
  <tiles:put name="footer" value="/admin/commonFooterAdmin.jsp" />
</tiles:insert>