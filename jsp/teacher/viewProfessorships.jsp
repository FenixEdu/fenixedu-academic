<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<tiles:insert page="/teacherLayout_2col.jsp" flush="true">
 
  <tiles:put name="institutionName" value="Instituto Superior T&eacute;cnico" />
  <tiles:put name="executionCourseName" beanName="<%=SessionConstants.INFO_SITE %>"  />
  <tiles:put name="body" value="/teacher/viewProfessorships_bd.jsp" />
  <tiles:put name="navbar" value="/teacher/professorshipnavbar.jsp" type="page"/>
</tiles:insert>