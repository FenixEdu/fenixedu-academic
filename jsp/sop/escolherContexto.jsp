<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col_photo.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopSchedule.jsp" />
  <tiles:put name="photos" value="/sop/commonEntrPhotosSop.jsp" />
  <tiles:put name="body" value="/sop/escolherContexto_bd.jsp" />
  <tiles:put name="body-context" value="" />  
  <tiles:put name="footer" value="/copyright.jsp" />
  <tiles:put name="context" value="/commons/context.jsp" />
</tiles:insert>