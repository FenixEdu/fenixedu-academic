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
			<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
				<thead>
					<tr>
						<th></th>
						<th><bean:message key="label.teacher.service.logs.who" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.teacher.service.logs.when" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
						<th><bean:message key="label.teacher.service.logs.description" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					</tr>
				</thead>
				<tbody> 
					<%
						for (final TeacherServiceLog log : logs) {
					%>
							<tr>
								<td>
									<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<%= log.getUser().getUserUId() %></bean:define>
									<img src="<%= request.getContextPath() + url %>" />

								</td>
								<td>
									<%= log.getUser().getPerson().getPresentationName() %>
								</td>
								<td>
									<%= log.getModificationDate().toString("yyyy-MM-dd HH:mm") %>
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
