<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="Portal Gestor de Projectos"/>
  <tiles:put name="serviceName" value="Portal Gestor de Projectos"/>
  <tiles:put name="navLocal" value="/projectsManagement/navBar.jsp"/>
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp"/>
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/projectsManagement/firstPage.jsp"/>
  <tiles:put name="footer" value="/copyright.jsp"/>
</tiles:insert>
