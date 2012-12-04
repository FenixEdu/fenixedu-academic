<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="bundle" value="TITLES_RESOURCES"/>
  <tiles:put name="title" value="private.operator" />
  <tiles:put name="serviceName" value="Operador" />
  <tiles:put name="navLocal" value="/manager/commons/commonNavLocalManager.jsp" />
  <tiles:put name="navGeral" value="/commons/functionalities/top-menu.jsp" />
  <tiles:put name="body-context" value="/commons/functionalities/breadCrumbs.jsp"/>  
  <tiles:put name="body" value="/manager/password/generateNewStudentsPassword_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

