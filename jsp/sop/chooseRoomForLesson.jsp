<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - SOP" />
  <tiles:put name="serviceName" value="SOP - Servico de Organizacao Pedagogica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopSchedule.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalSchedulesSop.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/sop/chooseRoomForLesson_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
  <tiles:put name="context" value="/commons/contextLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
</tiles:insert>