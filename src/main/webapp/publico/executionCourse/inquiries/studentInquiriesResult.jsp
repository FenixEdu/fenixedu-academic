<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="pt.ist.bennu.core.util.CoreConfiguration"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@page import="net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present role="role(PERSON)">
		<br/>

		<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
	
		<logic:notEmpty name="studentInquiriesCourseResults">
			<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiriesCourseResultBean" >
				<p class="mtop2"><b>
					<bean:message key="link.teachingInquiries.cuResults" bundle="INQUIRIES_RESOURCES"/></b> - 
					<html:link page="<%= "/executionCourse.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getExternalId() %>" target="_blank">
						<bean:write name="courseResult" property="studentInquiriesCourseResult.executionCourse.nome" /> - 				
						<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.degreeCurricularPlan.name" />
					</html:link>
				</p>

				<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
					<ul>
						<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult">
							<li>
								<html:link page="<%= "/executionCourse.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getExternalId() %>" target="_blank">
									<bean:write name="teachingResult" property="professorship.person.name" />
									&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
								</html:link>
							</li>			
						</logic:iterate>
					</ul>
				</logic:notEmpty>
			</logic:iterate>
		</logic:notEmpty>

</logic:present>

<logic:notPresent role="role(PERSON)">
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

		boolean isCasEnabled = CoreConfiguration.casConfig().isCasEnabled();
		if (isCasEnabled) {
		    String casValue = request.getScheme() + "://" + request.getServerName() + port + request.getContextPath() + "/loginCAS.do";
			String loginPage = CoreConfiguration.casConfig().getCasLoginUrl(casValue);
	%>
			<html:link href="<%= loginPage %>">Login</html:link>
	<%
		} else {
			String urlSuffix = "?service=" + value;
			String loginPage = FenixConfigurationManager.getConfiguration().getLoginPage() + urlSuffix;
	%>
			<html:link href="<%= loginPage %>">Login</html:link>
	<%
		}
	%>
	
</logic:notPresent>