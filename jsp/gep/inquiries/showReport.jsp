<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesEmailReminderReport" %>


<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" type="text/css" />

<p class="center">
	<bean:message key="title.inquiries.GEP" bundle="INQUIRIES_RESOURCES"/>
</p>
<h2 class="center caps">
	<bean:message key="title.inquiries.course.evaluation" bundle="INQUIRIES_RESOURCES"/>
</h2>
<h3 class="center caps">
	<bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/>
</h3>

<logic:present name='<%= InquiriesUtil.EMAIL_REMINDER_REPORTS_LIST %>'>
	<html:form action="/sendEmailReminder">
		<html:hidden property="method" value="sendEmails" />
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="table.header.curricular.plan" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.acronym" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.number.degree.students" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.number.students.with.email" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.number.sent.emails" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.number.unanswered.inquiries" bundle="INQUIRIES_RESOURCES"/>
				</td>
			</tr>
			<%
			int totalNumberDegreeStudents = 0;
			int totalNumberSentEmails = 0;
			int totalNumberStudentsWithEmail = 0;
			int totalNumberUnansweredInquiries = 0;
			double globalTotalNumberInquiries = 0.0;
			%>
			<logic:iterate id="report" name='<%= InquiriesUtil.EMAIL_REMINDER_REPORTS_LIST %>' type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesEmailReminderReport">
				<tr>
					<td class="listClasses">
						<bean:write name="report" property="executionDegree.infoDegreeCurricularPlan.infoDegree.nome" />
					</td>
					<td class="listClasses">
						<bean:write name="report" property="executionDegree.infoDegreeCurricularPlan.infoDegree.sigla" />
					</td>
					<td class="listClasses">
						<bean:write name="report" property="numberDegreeStudents"/>
					</td>
					<td class="listClasses">
						<bean:write name="report" property="numberStudentsWithEmail"/>
					</td>
					<td class="listClasses">
						<bean:write name="report" property="numberSentEmails"/>
					</td>
					<td class="listClasses">
						<bean:write name="report" property="numberUnansweredInquiries"/>
						<%
						out.print("(" + ((report.getNumberUnansweredInquiries().intValue()/report.getTotalNumberInquiries().doubleValue())*100) + "%)");
						%>
					</td>
				</tr>
				<%
				totalNumberDegreeStudents += report.getNumberDegreeStudents().intValue();
				totalNumberSentEmails += report.getNumberSentEmails().intValue();
				totalNumberStudentsWithEmail += report.getNumberStudentsWithEmail().intValue();
				totalNumberUnansweredInquiries += report.getNumberUnansweredInquiries().intValue();
				globalTotalNumberInquiries += report.getTotalNumberInquiries().doubleValue();
				%>
			</logic:iterate>
			
			<td class="listClasses-header" colspan="2">
				<bean:message key="table.header.totals" bundle="INQUIRIES_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<% out.print(totalNumberDegreeStudents); %>
			</td>
			<td class="listClasses-header">
				<% out.print(totalNumberStudentsWithEmail); %>
			</td>
			<td class="listClasses-header">
				<% out.print(totalNumberSentEmails); %>
			</td>
			<td class="listClasses-header">
				<% out.print(totalNumberUnansweredInquiries + " (" + ((totalNumberUnansweredInquiries/globalTotalNumberInquiries)*100) + "%)"); %>
			</td>
		</table>
		<%--/ul--%>

	</html:form>
</logic:present>
