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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.inquiries.deleteResults" bundle="INQUIRIES_RESOURCES"/></h2>

<span class="error"><html:errors bundle="INQUIRIES_RESOURCES" /></span>
<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
    <p><span class="error"><bean:write name="message" /></span></p>
</html:messages>
<logic:present name="deleteSuccessful">
	<p><span class="success0"><bean:message key="message.inquiry.delete.sucess" bundle="INQUIRIES_RESOURCES"/></span></p>
</logic:present>
<logic:present name="nothingDeleted">
	<p><span class="error"><bean:message key="message.inquiry.delete.dataNotFound" bundle="INQUIRIES_RESOURCES"/></span></p>
</logic:present>

<h3><bean:message key="label.executionCourse" bundle="APPLICATION_RESOURCES"/></h3>
<fr:edit id="deleteExecutionCourseResults" name="deleteExecutionCourseResults" action="/deleteInquiryResults.do?method=deleteExecutionCourseResults" >
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteExecutionCourseResultsBean" bundle="INQUIRIES_RESOURCES">
		<fr:slot name="executionCourseOID" required="true"/>
		<fr:slot name="executionDegreeOID"/>
		<fr:slot name="inquiryQuestionOID"/>
	</fr:schema>
	
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="invalid" path="/deleteInquiryResults.do?method=prepare"/>
	<fr:destination name="cancel" path="/deleteInquiryResults.do?method=prepare"/>
</fr:edit>

<br/>
<h3><bean:message key="label.teacher" bundle="APPLICATION_RESOURCES"/></h3>
<fr:edit id="deleteProfessorshipResults" name="deleteProfessorshipResults" action="/deleteInquiryResults.do?method=deleteTeacherResults" >
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteProfessorshipResultsBean" bundle="INQUIRIES_RESOURCES">
		<fr:slot name="professorshipOID" required="true"/>
		<fr:slot name="shiftType"/>
		<fr:slot name="inquiryQuestionOID"/>
	</fr:schema>
	
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="invalid" path="/deleteInquiryResults.do?method=prepare"/>
	<fr:destination name="cancel" path="/deleteInquiryResults.do?method=prepare"/>
</fr:edit>

<br/>
<h3><bean:message key="title.inquiries.deleteResults.allTeachers" bundle="INQUIRIES_RESOURCES"/></h3>
<fr:edit id="deleteAllProfessorshipResults" name="deleteProfessorshipResults" action="/deleteInquiryResults.do?method=deleteAllTeachersResults" >
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DeleteProfessorshipResultsBean" bundle="INQUIRIES_RESOURCES">
		<fr:slot name="executionCourseOID" required="true"/>
	</fr:schema>
	
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="invalid" path="/deleteInquiryResults.do?method=prepare"/>
	<fr:destination name="cancel" path="/deleteInquiryResults.do?method=prepare"/>
</fr:edit>