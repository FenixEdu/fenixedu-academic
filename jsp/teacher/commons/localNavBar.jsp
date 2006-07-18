<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="executionCourseIdInternal" name="<%= SessionConstants.INFO_SITE %>" property="infoExecutionCourse.idInternal" />
<ul>
  <li><html:link page="/viewSite.do">Administrar página da disciplina</html:link></li>
  <li><html:link page='<%= "/executionCourseShiftsPercentageManager.do?method=show&amp;executionCourseInternalCode=" + executionCourseIdInternal.toString() %>'>Administração de créditos</html:link></li>
</ul>
