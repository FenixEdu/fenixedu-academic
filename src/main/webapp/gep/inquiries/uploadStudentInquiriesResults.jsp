<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>


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