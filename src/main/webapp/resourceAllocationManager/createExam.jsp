<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/resourceAllocationManager/commonNavGeralSopExam.jsp" />
  <tiles:put name="navLocal" value="/resourceAllocationManager/commonNavLocalExamsSop.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/resourceAllocationManager/createExam_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
<%--
  <tiles:put name="context" value="/commons/contextExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
--%>
  <tiles:put name="context" value="/commons/contextCurricularYearsExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />
</tiles:insert>