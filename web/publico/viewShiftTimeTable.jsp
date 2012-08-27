<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="definition.public.mainPage" beanName="" flush="true">
 <tiles:put name="degrees" value="/publico/associatedDegrees.jsp" />
  <tiles:put name="navGeral" value="/commons/blank.jsp" />
  <tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="body" value="/publico/viewShiftTimeTable_bd.jsp" />
  <tiles:put name="footer" value="/commons/blank.jsp" />
</tiles:insert> 