<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<bean:message key="message.teachingInquiries.notOpen" bundle="INQUIRIES_RESOURCES"/>

<c:if test="${not empty executionCoursesToAnswer}">
	<p style="margin-top: 1.75em;"><strong><bean:message key="label.teachingInquiries.coursesToAnswer" bundle="INQUIRIES_RESOURCES"/>:</strong></p>
	
	<c:forEach items="${executionCoursesToAnswer}" var="executionCourse">
		<p style="padding-left: 2em; margin: 0.75em 0;">
			<html:link page="/teachingInquiry.do?method=showInquiriesPrePage&contentContextPath_PATH=/docencia/docencia" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:write name="executionCourse" property="nome"/> -
				<bean:write name="executionCourse" property="executionPeriod.semester" />
				<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
				<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />				
			</html:link>
		</p>
	</c:forEach>
</c:if>