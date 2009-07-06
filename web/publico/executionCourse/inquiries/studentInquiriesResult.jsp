<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@page import="net.sourceforge.fenixedu.domain.inquiries.teacher.InquiryResponsePeriodType"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present role="PERSON">
		<br/>

		<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
		<%
			final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
			final ExecutionYear executionYear = executionSemester.getExecutionYear();
			final int year = executionYear.getBeginDateYearMonthDay().getYear();
			// TODO: Refactor this... instead check if it is before now and has no response period defined.
			if (year < 2007 || (year == 2007 && executionSemester.getSemester().intValue() == 1)) {
		%>
			    <bean:message key="message.teachingInquiries.noResults.msg2" bundle="INQUIRIES_RESOURCES"/>
		<%
			} else {			    
				final InquiryResponsePeriod inquiryResponsePeriod = executionSemester.getInquiryResponsePeriod(InquiryResponsePeriodType.TEACHING);
				if (inquiryResponsePeriod == null) {
		%>
				<bean:message key="message.teachingInquiries.noResults.msg4b" bundle="INQUIRIES_RESOURCES"/>
					
				<logic:present role="STUDENT">
					<bean:message key="message.teachingInquiries.noResults.msg4b.participate" bundle="INQUIRIES_RESOURCES"/>
					<html:link href="<%= request.getContextPath() + "/student/studentInquiry.do?method=showCoursesToAnswer"%>">
						<bean:message key="message.teachingInquiries.noResults.msg4b.link" bundle="INQUIRIES_RESOURCES"/>	
					</html:link>!
				</logic:present>					
					
		<%
				} else if (!inquiryResponsePeriod.getBegin().plusDays(12).isBeforeNow()) {
		%>
				    <bean:message key="message.teachingInquiries.noResults.msg3" bundle="INQUIRIES_RESOURCES"/>
		<%
				} else {
		%>
					<logic:empty name="studentInquiriesCourseResults">
		<%
						if (executionCourse.getAvailableForInquiries().booleanValue()) {
		%>
							<bean:message key="message.teachingInquiries.noResults.msg1" bundle="INQUIRIES_RESOURCES"/>
		<%
						} else {
		%>
							<bean:message key="message.teachingInquiries.noResults.msg0" bundle="INQUIRIES_RESOURCES"/>
		<%
						}
		%>
		<% %>
					</logic:empty>
					<logic:notEmpty name="studentInquiriesCourseResults">
						<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean" >
							<p class="mtop2">
								<bean:message key="link.teachingInquiries.cuResults" bundle="INQUIRIES_RESOURCES"/> - 
								<html:link page="<%= "/executionCourse.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getIdInternal() %>" target="_blank">
									<bean:write name="courseResult" property="studentInquiriesCourseResult.executionCourse.nome" /> - 				
									<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.degreeCurricularPlan.name" />
								</html:link>
							</p>

							<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
								<ul>
									<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
										<li>
											<html:link page="<%= "/executionCourse.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getIdInternal() %>" target="_blank">
												<bean:write name="teachingResult" property="professorship.person.name" />
												&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
											</html:link>
										</li>			
									</logic:iterate>
								</ul>
							</logic:notEmpty>
						</logic:iterate>
					</logic:notEmpty>
		<%
				}
			}
		%>
</logic:present>

<logic:notPresent role="PERSON">
	<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
	<br/>
	<br/>
	<bean:message key="message.inquiries.information.not.public" bundle="INQUIRIES_RESOURCES"/>
	<%
		String port = request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort();

		String value = request.getScheme() + "://" + request.getServerName()
			+ port + request.getContextPath() + executionCourse.getSite().getReversePath() + "/resultados-quc";
		session.setAttribute("ORIGINAL_REQUEST", Boolean.TRUE);
		session.setAttribute("ORIGINAL_URI", value);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("service", value);
		session.setAttribute("ORIGINAL_PARAMETER_MAP", map);
		session.setAttribute("ORIGINAL_ATTRIBUTE_MAP", map);

	boolean isCasEnabled = PropertiesManager.getBooleanProperty("cas.enabled");
		if (isCasEnabled) {
			String casValue = request.getScheme() + "://" + request.getServerName()
				+ port + request.getContextPath() + "/loginCAS.do";
			String urlSuffix = "?service=" + casValue;
		    String loginPage = PropertiesManager.getProperty("cas.loginUrl") + urlSuffix;
	%>
			<html:link href="<%= loginPage %>">Login</html:link>
	<%
		} else {
			String urlSuffix = "?service=" + value;
			String loginPage = PropertiesManager.getProperty("login.page") + urlSuffix;
	%>
			<html:link href="<%= loginPage %>">Login</html:link>
	<%
		}
	%>
	
</logic:notPresent>