<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="org.fenixedu.bennu.core.util.CoreConfiguration"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<style> 
ul {
margin: 0 20px;
padding: 0;
}
ul li {
margin: 0;
padding: 0;
}
</style>

<h2><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present role="role(PERSON)">
	<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>

	<table class="tstyle2 thwhite thleft tdleft thlight">
		<tr>
			<th><bean:write name="executionCourse" property="nome"/></th>
			<td>
				<ul>
					<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">		
						<bean:define id="degreeCurricularPlanOID" name="curricularCourse" property="degreeCurricularPlan.externalId"/>
						<li>
							<html:link page="<%= "/executionCourse.do?method=dispatchToInquiriesResultPage&executionCourseID=" + executionCourse.getExternalId() + 
								"&degreeCurricularPlanOID=" + degreeCurricularPlanOID %>" target="_blank">
								Resultados da UC (<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>)
							</html:link>
						</li>			
					</logic:iterate>
				</ul>
			</td>
		</tr>
	</table>

	<table class="tstyle2 thwhite thleft tdleft thlight">
		<logic:iterate id="teacherInquiry" name="professorships">
			<bean:define id="professorshipOID" name="teacherInquiry" property="key.externalId"/>
			<tr>
				<th>
					<bean:write name="teacherInquiry" property="key.person.name"/>										
				</th>
				<td>
					<ul>			
						<logic:iterate id="teacherInquiryByShift" name="teacherInquiry" property="value">
							<li>
								<html:link page="/executionCourse.do?method=dispatchToTeacherInquiriesResultPage&professorshipOID=${professorshipOID}&shiftType=${teacherInquiryByShift}&executionCourseID=${executionCourse.externalId}" target="_blank">
									<bean:message name="teacherInquiryByShift" property="name" bundle="ENUMERATION_RESOURCES"/>
								</html:link>
							</li>
						</logic:iterate>
					</ul>
				</td>
			</tr>
		</logic:iterate>
	</table>
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