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

<c:forEach items="${degreesWithoutTeachingInquiry}" var="degree">

<bean:define id="executionDegreeID" name="degree" property="idInternal" />
<html:link page="<%= "/teachingInquiry.do?method=showInquiries1stPage&executionDegreeID=" + executionDegreeID %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<c:out value="${degree.presentationName}" />
</html:link>
<br/>

</c:forEach>