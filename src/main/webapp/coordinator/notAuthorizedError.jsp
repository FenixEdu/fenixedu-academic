<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="Coordenador11" />
  <tiles:put name="serviceName" value="Portal do Coordenador" />
  <tiles:put name="navGeral" value="/coordinator/commonNavGeralCoordinator.jsp" />
  <tiles:put name="navLocal" value="/commons/blank.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/coordinator/notAuthorizedError_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>