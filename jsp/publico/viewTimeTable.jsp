<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/layout/publicGesDisLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Instituto Superior T&eacute;cnico" />
  <tiles:put name="executionCourseName" beanName="exeName" />
  <tiles:put name="degrees" value="/publico/associatedDegrees.jsp" />
  	<tiles:put name="body" value="/publico/viewTimeTable_bd.jsp" />
  <tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="navbar" value="/publico/gesdisNavbar.jsp"/>
  <tiles:put name="footer" value="/commons/blank.jsp" />
</tiles:insert> 