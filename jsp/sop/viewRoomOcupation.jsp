<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopRoom.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalSalasSop.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/sop/viewRoomOcupation_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>