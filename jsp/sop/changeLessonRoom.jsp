<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalSop.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>    
  <tiles:put name="body" value="/sop/changeLessonRoom_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
  <tiles:put name="context" value="/commons/contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
</tiles:insert>