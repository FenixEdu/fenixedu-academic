<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<tiles:insert page="/publicGesDisLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Instituto Superior Técnico" />
  <tiles:put name="executionCourseName" beanName="<%=SessionConstants.INFO_SITE %>" beanProperty="infoExecutionCourse.nome" />
  <tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="navbar" value="/publico/gesdisNavbar.jsp" />
  <tiles:put name="body" value="/publico/viewExecutionCourseShifts_bd.jsp" />
  <tiles:put name="footer" value="/publico/commonFooterPub.jsp" />
</tiles:insert>