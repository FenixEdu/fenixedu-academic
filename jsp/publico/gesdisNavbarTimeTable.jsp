<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:link page="/viewSite.do">
	<li><bean:message key="link.home"/></li>
</html:link>
<logic:present name="<%=SessionConstants.INFO_SITE%>" property="alternativeSite" >
	<html:link page="/accessAlternativeSite.do">
		<li><bean:message key="link.alternative"/></li>
	</html:link>
</logic:present>
<html:link  page="/accessAnnouncements.do">
	<li><bean:message key="link.announcements"/></li>
</html:link>
<html:link page="/accessObjectives.do?method=acessObjectives">
	<li><bean:message key="link.objectives"/></li>
</html:link>
<html:link page="/accessProgram.do?method=acessProgram">
	<li><bean:message key="link.program"/></li>
</html:link>
<html:link page="/accessBibliographicReferences.do?method=viewBibliographicReference">
	<li><bean:message key="link.bibliography"/></li>
</html:link>
	<html:link page="/accessTeachers.do">
	<li><bean:message key="link.teachers"/></li>
</html:link>
<html:link page="/viewExecutionCourseShifts.do">
		<li><bean:message key="link.executionCourse.shifts"/></li>
</html:link>