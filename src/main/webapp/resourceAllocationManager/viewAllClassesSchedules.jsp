<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%--
<tiles:insert definition="df.layout.print.alternative" beanName="" flush="true">
--%>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="private.resourcemanagement.schedules.executiondates.listingofclasses"/>
  <tiles:put name="bundle" value="TITLES_RESOURCES"/>
  
  <tiles:put name="serviceName" value="Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/resourceAllocationManager/viewAllClassesSchedules_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
  
  <tiles:put name="navLocal" value="/resourceAllocationManager/commonNavLocalSchedulesSop.jsp" />
  <tiles:put name="context" value="/commons/context.jsp" /> 
</tiles:insert>