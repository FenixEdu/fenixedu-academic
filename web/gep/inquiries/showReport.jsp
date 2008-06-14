<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesEmailReminderReport" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>


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
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="sendEmails" />
		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message key="table.header.curricular.plan" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.acronym" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.number.degree.students" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.number.students.with.email" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.number.sent.emails" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.number.unanswered.inquiries" bundle="INQUIRIES_RESOURCES"/>
				</th>
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
						Double percent = new Double((report.getNumberUnansweredInquiries().intValue()/report.getTotalNumberInquiries().doubleValue())*100);

						out.print("(" + InquiriesUtil.formatAnswer(NumberUtils.formatNumber(percent, 2)) + "%)");
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
			
			<th class="listClasses-header" colspan="2">
				<bean:message key="table.header.totals" bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<% out.print(totalNumberDegreeStudents); %>
			</th>
			<th class="listClasses-header">
				<% out.print(totalNumberStudentsWithEmail); %>
			</th>
			<th class="listClasses-header">
				<% out.print(totalNumberSentEmails); %>
			</th>
			<th class="listClasses-header">
				<% 
				Double globalPercent = new Double((totalNumberUnansweredInquiries/globalTotalNumberInquiries)*100);
				
				out.print(totalNumberUnansweredInquiries + " (" + InquiriesUtil.formatAnswer(NumberUtils.formatNumber(globalPercent, 2)) + "%)");
				%>
			</th>
		</table>
		<%--/ul--%>

	</html:form>
</logic:present>
