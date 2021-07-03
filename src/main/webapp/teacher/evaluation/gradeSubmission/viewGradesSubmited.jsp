<%@ page import="org.fenixedu.academic.domain.EnrolmentEvaluation" %>
<%@ page import="org.fenixedu.academic.domain.ExecutionCourse" %>
<%@ page import="org.fenixedu.academic.domain.MarkSheet" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %><%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

	
<h2><bean:message key="label.submited.marks"/></h2>

<fr:view name="marksSubmited"
		 schema="markSheet.teacher.gradeSubmission.view.submited.marks">
		<fr:layout name="tabular">
			<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.person.username"/>
			<fr:property name="classes" value="tstyle4"/>
		    <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
</fr:view>
<br/>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheets.for.signing"/></h2>
<p class="infoop2">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheets.for.signing.instructions"/>
</p>
<ul>
<%
	final List<EnrolmentEvaluation> marksSubmited = (List<EnrolmentEvaluation>) request.getAttribute("marksSubmited");
	if (marksSubmited != null && marksSubmited.size() > 0) {
		final Set<MarkSheet> markSheets = new HashSet<MarkSheet>();
		for (final EnrolmentEvaluation enrolmentEvaluation : marksSubmited) {
			final MarkSheet markSheet = enrolmentEvaluation.getMarkSheet();
			if (!markSheets.contains(markSheet)) {
				markSheets.add(markSheet);
%>
	<li>
		<a href="<%= request.getContextPath() + "/teacher/markSheetManagement.do?method=viewMarkSheet"
						+ "&msID=" + markSheet.getExternalId()
						+ "&executionCourseID=" + ((ExecutionCourse) request.getAttribute("executionCourse")).getExternalId()
				 		%>">
			<%= markSheet.getDegreeCurricularPlanName() %>
		</a>
	</li>
<%
			}
		}
	}
%>
</ul>
