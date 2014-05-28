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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<fr:form action="/markSheetManagement.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="gradeSubmissionStepOne" />
	<input type="hidden" name="executionCourseID" value="${executionCourseID}" />	
	
	<h2><bean:message key="label.submit.listMarks"/></h2>
	
	<div class="infoop2">
		<bean:message key="label.submitMarks.introduction"/>
	</div>

	<p class="breadcumbs mbottom1"><span class="actual"><bean:message key="label.markSheet.gradeSubmission.step.one"/></span> &gt; <span><bean:message key="label.markSheet.gradeSubmission.step.two"/></span></p>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<fr:edit id="submissionBean"
			 name="submissionBean"
			 schema="markSheet.teacher.gradeSubmission.step.one"
			 type="net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop1"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>

	<p>
		<span class="warning0"><bean:message key="label.submitMarks.remainder"/></span>
	</p>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	</p>
	
</fr:form>
