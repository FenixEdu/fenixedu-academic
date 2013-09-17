<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="bundle" value="TITLES_RESOURCES"/>
  <tiles:put name="title" value="private.steeringcouncil" />
  <tiles:put name="serviceName" value="Conselho Directivo" />
  <tiles:put name="navLocal" value="/directiveCouncil/pgMainMenu.jsp" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/directiveCouncil/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
