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