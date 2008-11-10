<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present name="executionSemester" property="teachingInquiryResponsePeriod">
	<logic:notEmpty name="executionSemester" property="teachingInquiryResponsePeriod.introduction">	
		<div>
			<bean:write name="executionSemester" property="teachingInquiryResponsePeriod.introduction" filter="false"/>
		</div>
	</logic:notEmpty>
</logic:present>

<h4 class="mtop2">
	<html:link page="/teachingInquiry.do?method=showInquiries1stPage" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		<bean:message key="link.teachingInquiries.fillInquiry" bundle="INQUIRIES_RESOURCES"/>
	</html:link>
</h4>

<logic:present name="studentInquiriesCourseResults">
	<p class="separator2 mtop2"><bean:message key="label.teachingInquiries.studentInquiriesResults" bundle="INQUIRIES_RESOURCES"/></p>
	<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean" >
		<p class="mtop2">
			<html:link page="<%= "/teachingInquiry.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getIdInternal() %>" target="_blank">
				<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.presentationName" />
			</html:link>
		</p>

		<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
			<ul>
				<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
					<li>
						<html:link page="<%= "/teachingInquiry.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getIdInternal() %>" target="_blank">
							<bean:write name="teachingResult" property="professorship.teacher.person.name" />
							&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
						</html:link>
					</li>			
				</logic:iterate>
			</ul>
		</logic:notEmpty>
	</logic:iterate>
</logic:present>


