<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<p class="invisible"><strong>&raquo; Gest&atilde;o de Exames</strong></p>
<ul>
	<li>
		<html:link page="<%= "/chooseExamsContextDA.do?method=prepare&amp;nextPage=createExam&amp;inputPage=chooseExamsContext&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.create"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.map"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/chooseDayAndShiftForm.do?method=prepare&amp;nextPage=viewEmptyRooms&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchRoomsWithNoExams"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/consultRoomsForExams.do?method=prepareSearch&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.consultRoomOccupation"/>
		</html:link>
	</li>
</ul>
<p class="invisible"><strong>&raquo; Listagens de Exames</strong></p>
<ul>
	<li>
		<html:link page="<%= "/chooseDayAndShiftForm.do?method=prepare&amp;nextPage=viewExams&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.listByDayAndShift"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/chooseExamsContextDA.do?method=prepare&amp;nextPage=listByDegreeAndAcademicYear&amp;inputPage=chooseExamsContext&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.listByDegreeAndAcademicYear"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/viewAllExamsByDegreeAndCurricularYear.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.listAllByDegreeAndAcademicYear"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/consultAllRoomsForExams.do?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.viewAllRoomOccupation"/>
		</html:link>
	</li>
</ul>