<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="&Aacute;rea de Pessoa" />
  <tiles:put name="serviceName" value="&Aacute;rea de Pessoa" />
  <tiles:put name="navGeral" value="/person/commonNavGeralPerson.jsp" />
  <tiles:put name="navLocal" value="/commons/blank.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp"/>  
  <tiles:put name="body" value="/person/notAuthorizedError_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>