<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<ul>
<%--
	<li>
		<html:link page="<%= "/chooseDegreeAndYearContext.do?method=prepare&amp;"
 							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.examsManagement"/>
		</html:link>
	</li>
--%>
	<li>
		<html:link page="<%= "/writtenEvaluations/writtenEvaluationCalendar.faces?"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.writtenEvaluation.map"/>
		</html:link>
	</li>
	<%--
	<li>
		<html:link page="<%= "/roomExamSearch.do?method=prepare&amp;nextPage=viewExams&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchExamsByRoom"/>
		</html:link>
	</li>
	--%>	<li>		<html:link page="<%= "/writtenEvaluations/writtenEvaluationsByRoom.faces?"  							+ SessionConstants.EXECUTION_PERIOD_OID  							+ "="  							+ pageContext.findAttribute("executionPeriodOID") %>">			<bean:message key="link.writtenEvaluation.by.room"/>		</html:link>	</li>
	<%--	
	<li>
		<html:link page="<%= "/ExamSearchByDegreeAndYear.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchExamsByDegree"/>
		</html:link>
	</li>
	--%>
	<li>		<html:link page="<%= "/searchWrittenEvaluationsByDegreeAndYear.do?method=prepare&amp;"  							+ SessionConstants.EXECUTION_PERIOD_OID  							+ "="  							+ pageContext.findAttribute("executionPeriodOID") %>">			<bean:message key="link.exams.searchWrittenEvaluationsByDegreeAndYear"/>		</html:link>	</li>
	<%--	<li>
		<html:link page="<%= "/ExamSearchByDate.do?method=prepare&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchExamsByDate"/>
		</html:link>
	</li>
	--%>
	<li>		<html:link page="<%= "/searchWrittenEvaluationsByDate.do?method=prepare&amp;"  							+ SessionConstants.EXECUTION_PERIOD_OID  							+ "="  							+ pageContext.findAttribute("executionPeriodOID") %>">			<bean:message key="title.written.evaluations.search.by.date"/>		</html:link>	</li>
	<li>
		<html:link page="<%= "/roomSearch.do?method=prepare&amp;nextPage=viewEmptyRooms&amp;"
  							+ SessionConstants.EXECUTION_PERIOD_OID
  							+ "="
  							+ pageContext.findAttribute("executionPeriodOID") %>">
			<bean:message key="link.exams.searchAvailableRooms"/>
		</html:link>
	</li></ul>
