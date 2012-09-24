<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean"%>
<%@page import="net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog"%>
<%@page import="java.util.SortedSet"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
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
						<th><bean:message key="label.teacher.service.logs.description" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					</tr>
				</thead>
				<tbody> 
					<%
						for (final TeacherServiceLog log : logs) {
						    final Person person = log.getUser().getPerson();
					%>
							<tr>
								<td>
									<%= log.getModificationDate().toString("yyyy-MM-dd HH:mm") %>
								</td>
								<td>
									<%= person.getNickname() %> (<%= person.getUsername() %>)
								</td>
								<td>
									<%= log.getDescription() %>
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
