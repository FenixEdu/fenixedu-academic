<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/sop/commonNavGeralSopExecutionCourse.jsp" />
  <tiles:put name="navLocal" value="/sop/commonNavLocalExecutionCourses.jsp" />
  <tiles:put name="body-context" value="" />  
  <tiles:put name="body" value="/sop/manageExecutionCourses_bd.jsp" />
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>