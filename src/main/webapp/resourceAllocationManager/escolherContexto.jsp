<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column-photo" beanName="" flush="true">
  <tiles:put name="serviceName" value="SOP - Serviço de Organização Pedagógica" />
  <tiles:put name="navGeral" value="/commons/commonGeneralNavigationBar.jsp" />
  <tiles:put name="photos" value="/resourceAllocationManager/commonEntrPhotosSop.jsp" />
  <tiles:put name="body" value="/resourceAllocationManager/escolherContexto_bd.jsp" />
  <tiles:put name="body-context" value="/commons/blank.jsp" />  
  <tiles:put name="footer" value="/copyright.jsp" />
  <tiles:put name="context" value="/commons/context.jsp" />
</tiles:insert>