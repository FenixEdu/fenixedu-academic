<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:link page="/viewSite.do">
	<li><bean:message key="link.home"/></li>
</html:link>
<html:link page="/accessAlternativeSite.do">
	<li>	<bean:message key="link.alternative"/></li>
</html:link>
<html:link  page="/accessAnnouncements.do">
	<li><bean:message key="link.announcements"/></li>
</html:link>
<html:link page="/objectivesManagerDA.do?method=acessObjectives">
	<li><bean:message key="link.objectives"/></li>
</html:link>
<html:link page="/programManagerDA.do?method=acessProgram">
	<li><bean:message key="link.program"/></li>
</html:link><html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference">
	<li><bean:message key="link.bibliography"/></li>
</html:link>
	<html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship">
	<li><bean:message key="link.teachers"/></li>
</html:link>
<html:link page="/viewTimeTable.do">
		<li><bean:message key="link.executionCourse.timeTable"/></li>
</html:link>
<html:link page="/viewExecutionCourseShifts.do">
		<li><bean:message key="link.executionCourse.shifts"/></li>
</html:link>


