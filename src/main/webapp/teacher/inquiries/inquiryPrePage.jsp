<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<html:xhtml />

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present name="executionSemester" property="teachingInquiryResponsePeriod">
	<logic:notEmpty name="executionSemester" property="teachingInquiryResponsePeriod.introduction">	
		<div style="border: 1px solid #ddd; padding: 5px 15px; background: #fafafa;">
			<bean:write name="executionSemester" property="teachingInquiryResponsePeriod.introduction" filter="false"/>
		</div>
	</logic:notEmpty>
</logic:present>

<c:if test="${professorship.teachingInquiriesToAnswer}">
	<h4 class="mtop2">
		<html:link page="/teachingInquiry.do?method=showInquiries1stPage" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
			<bean:message key="link.teachingInquiries.fillInquiry" bundle="INQUIRIES_RESOURCES"/>
		</html:link> <c:if test="${not empty professorship.teachingInquiry}"><span class="success0 smalltxt"><bean:message key="label.filled" bundle="INQUIRIES_RESOURCES"/></span></c:if>
	</h4>
</c:if>

<logic:present name="studentInquiriesCourseResults">
	<p class="separator2 mtop2"><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></p>
	<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiriesCourseResultBean" >
		<p class="mtop2">
			<bean:message key="link.teachingInquiries.cuResults" bundle="INQUIRIES_RESOURCES"/> - 
			<html:link page="<%= "/teachingInquiry.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getExternalId() %>" target="_blank">
				<bean:write name="courseResult" property="studentInquiriesCourseResult.executionCourse.nome" /> - 				
				<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.degreeCurricularPlan.name" />
			</html:link>
		</p>

		<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
			<ul>
				<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult">
					<li>
						<html:link page="<%= "/teachingInquiry.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getExternalId() %>" target="_blank">
							<bean:write name="teachingResult" property="professorship.person.name" />
							&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
						</html:link>
					</li>			
				</logic:iterate>
			</ul>
		</logic:notEmpty>
	</logic:iterate>

<%-- 
--%>
	<p class="separator2 mtop25"><bean:message key="title.inquiries.teachingReports" bundle="INQUIRIES_RESOURCES"/></p>
	<logic:iterate id="professorship" name="executionCourse" property="professorships" type="net.sourceforge.fenixedu.domain.Professorship">
		<% if (professorship.getPerson() == AccessControl.getPerson()) { %>
			<logic:notEmpty name="professorship" property="teachingInquiry">
				<bean:define id="teachingInquiryID" name="professorship" property="teachingInquiry.externalId" />
				<html:link href="<%= request.getContextPath() + "/coordinator/viewInquiriesResults.do?method=showFilledTeachingInquiry&filledTeachingInquiryId=" + teachingInquiryID %>" target="_blank">
					<bean:write name="professorship" property="teacher.person.name"/> 
				</html:link>
			</logic:notEmpty>
		<% } %>
	</logic:iterate>

</logic:present>
