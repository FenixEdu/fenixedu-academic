<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLEncoder"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present role="PERSON">
	<br/>
	
	<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
	
	<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degreeCurricularPlanOID" name="curricularCourse" property="degreeCurricularPlan.externalId"/>
		Resultados da UC no curso: <bean:write name="curricularCourse" property="degreeCurricularPlan.degree.nome"/> - 	
		<html:link page="<%= "/executionCourse.do?method=dispatchToInquiriesResultPage&executionCourseOID=" + executionCourse.getExternalId() + 
			"&degreeCurricularPlanOID=" + degreeCurricularPlanOID %>" target="_blank">
			ver
		</html:link>
		<br/>					
	</logic:iterate>
	
	<table>
		<logic:iterate id="teacherInquiry" name="professorships">
			<bean:define id="professorshipOID" name="teacherInquiry" property="key.externalId"/>
			<tr>
				<td class="aleft">
					<bean:write name="teacherInquiry" property="key.person.name"/>										
				</td>
				<td>
					<table class="tstyle2 thwhite thleft tdleft">					
						<logic:iterate id="teacherInquiryByShift" name="teacherInquiry" property="value">
							<tr>
								<td>
									<html:link page="<%= "/executionCourse.do?method=dispatchToTeacherInquiriesResultPage&professorshipOID=" + professorshipOID + 
											"&shiftType=" + teacherInquiryByShift %>" target="_blank">
										<bean:message name="teacherInquiryByShift" property="name" bundle="ENUMERATION_RESOURCES"/>
									</html:link>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
		</logic:iterate>
	</table>
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