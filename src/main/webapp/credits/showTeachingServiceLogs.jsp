<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean"%>
<%@page import="net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog"%>
<%@page import="java.util.SortedSet"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<%--
<jsp:include page="teacherCreditsStyles.jsp"/>
 --%>

<logic:present name="annualTeachingCreditsBean">
	<% final SortedSet<TeacherServiceLog> logs = ((AnnualTeachingCreditsBean) request.getAttribute("annualTeachingCreditsBean")).getLogs(); %>
	<%
		if (logs.isEmpty()) {
	%>
			<bean:message key="label.teacher.service.logs.none" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	<%
		} else {
	%>
			<table class="tstyle2 thlight thleft mtop05 mbottom05">
				<thead>
					<tr>
						<th><bean:message key="label.teacher.service.logs.when" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.teacher.service.logs.who" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.teacher.service.logs.description" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					</tr>
				</thead>
				<tbody> 
					<%
						for (final TeacherServiceLog log : logs) {
						    final Person person = log.getUser()!=null?log.getUser().getPerson():null;
					%>
							<tr>
								<td>
									<%= log.getModificationDate().toString("yyyy-MM-dd HH:mm") %>
								</td>
								<td>
									<% if(person != null){ %>
									<%= person.getNickname() %> (<%= person.getUsername() %>)
									<%}%>
								</td>
								<td>
									<%= log.getTeacherService().getExecutionPeriod().getName() %>
								</td>
								<td>
									<pre><%= log.getDescription() %></pre>
								</td>
							</tr>
					<%
						}
					%>
				</tbody>
			</table>
	<%
		}
	%>
</logic:present>
