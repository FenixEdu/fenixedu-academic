<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="SOP" />
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="navLocal" value="/resourceAllocationManager/commonNavLocalSalasSop.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/resourceAllocationManager/viewRoomOcupation_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>