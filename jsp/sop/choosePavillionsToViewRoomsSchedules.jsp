<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - SOP" />
  <tiles:put name="serviceName" value="Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopSchedule.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalBeforeSop_bd.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/sop/choosePavillionsToViewRoomsSchedules_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
  <tiles:put name="context" value="/commons/context.jsp" />
</tiles:insert>