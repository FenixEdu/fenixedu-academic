<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/publicGesDisLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".Instituto Superior T&eacute;cnico" />
  <tiles:put name="serviceName" value="Instituto Superior T&eacute;cnico" />
 <tiles:put name="degrees" value="/publico/associatedDegrees.jsp" />
  <tiles:put name="navGeral" value="/commons/blank.jsp" />
  <tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="body" value="/publico/viewShiftTimeTable_bd.jsp" />
  <tiles:put name="footer" value="/commons/blank.jsp" />
</tiles:insert> 