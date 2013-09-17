<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="bundle" value="TITLES_RESOURCES"/>
  <tiles:put name="title" value="private.resourcemanagement.schedules" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />  
  <tiles:put name="navLocal" value="/resourceAllocationManager/commonNavLocalExecutionCourses.jsp" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp" />  
  <tiles:put name="body" value="/resourceAllocationManager/manageExecutionCourse_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
  <tiles:put name="context" value="/commons/contextExecutionCourse.jsp" />
</tiles:insert>