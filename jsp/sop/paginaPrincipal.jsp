<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col_photo.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSop.jsp" />
  <tiles:put name="photos" value='/sop/commonEntrPhotosSop.jsp' />
  <tiles:put name="body-context" value="" />  
  <tiles:put name="body" value="/sop/paginaPrincipal_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
