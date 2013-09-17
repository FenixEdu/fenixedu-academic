<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<bean:define id="executionCourseExternalId" name="<%= PresentationConstants.INFO_SITE %>" property="infoExecutionCourse.externalId" />
<ul>
  <li><html:link page='<%= "/executionCourseShiftsPercentageManager.do?method=show&amp;executionCourseInternalCode=" + executionCourseExternalId.toString() %>'>Administração de créditos</html:link></li>
</ul>
