<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/teacherLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Portal Docente" />
  <tiles:put name="institutionName" value="Instituto Superior T&eacute;cnico" />  
  <tiles:put name="navGeral" value="/teacher/commonNavGeralTeacher.jsp" />
  <tiles:put name="executionCourseName" value="/teacher/executionCourseName.jsp" />
  <tiles:put name="body" value="/teacher/exercicesFirstPage_bd.jsp" />
  <tiles:put name="navLocal" value="/teacher/testsNavbar.jsp" type="page"/>
  <tiles:put name="footer" value="/teacher/commons/commonFooterTests.jsp" />
</tiles:insert>