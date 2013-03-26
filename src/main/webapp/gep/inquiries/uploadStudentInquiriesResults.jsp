<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>


<ul>
	<li>
		<h4>
			<html:link action="/uploadStudentInquiriesResults.do?method=prepareCurricularCourses">
				<bean:message key="title.inquiries.studentInquiry.uploadCourseResults" bundle="INQUIRIES_RESOURCES"/>
			</html:link>
		</h4>  
	</li>
	<li>
		<h4>
			<html:link action="/uploadStudentInquiriesResults.do?method=prepareTeachers">
				<bean:message key="title.inquiries.studentInquiry.uploadTeachingResults" bundle="INQUIRIES_RESOURCES"/>
			</html:link>
		</h4>
	</li>
</ul>