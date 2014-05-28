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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />


<h2><bean:message key="title.inquiry.quc.regent" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/> (<bean:write name="executionSemester" property="semester"/>º Semestre <bean:write name="executionSemester" property="executionYear.year"/>)</h3>

<p><bean:message key="message.teacher.resume.inquiry" bundle="INQUIRIES_RESOURCES"/></p>

<logic:present name="updated">
	<bean:define id="completionState" name="completionState" type="java.lang.String"/>
	<p>
		<span class="success0">
			<bean:message key="message.inquiry.report.updated" bundle="INQUIRIES_RESOURCES"/>
			<bean:message key="message.inquiry.report.filledState" bundle="INQUIRIES_RESOURCES" arg0="<%= completionState %>"/>
			<logic:present name="teacherCompletionState">
				<bean:message key="message.inquiry.report.shouldFillIn" bundle="INQUIRIES_RESOURCES"/> 
				<html:link page="/teachingInquiry.do?method=showInquiriesPrePage" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
					<bean:message key="label.inquiry.teaching" bundle="INQUIRIES_RESOURCES"/>
				</html:link> (<bean:write name="teacherCompletionState"/>).
			</logic:present>
		</span>
	</p>
</logic:present>

<logic:notPresent name="readMode">
	<html:link action="/regentInquiry.do?method=showRegentInquiry" paramName="professorship" paramProperty="externalId" paramId="professorshipOID">
		<b>
			<logic:equal name="isComplete" value="false">
				<bean:message key="link.inquiry.fillIn" bundle="INQUIRIES_RESOURCES"/>
			</logic:equal>
			<logic:equal name="isComplete" value="true">
				<bean:message key="link.inquiry.edit" bundle="INQUIRIES_RESOURCES"/>
			</logic:equal> 
			<bean:message key="label.inquiry.regent" bundle="INQUIRIES_RESOURCES"/>
		</b>
	</html:link>
	<logic:present name="completionState">
		(<bean:write name="completionState"/>)
		<logic:present name="teacherCompletionState">
			. <bean:message key="message.inquiry.report.shouldFillIn" bundle="INQUIRIES_RESOURCES"/> 
			<html:link page="/teachingInquiry.do?method=showInquiriesPrePage" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="label.inquiry.teaching" bundle="INQUIRIES_RESOURCES"/>
			</html:link> (<bean:write name="teacherCompletionState"/>).
		</logic:present>
	</logic:present>
</logic:notPresent>
<logic:present name="readMode">
	<html:link action="/viewQUCInquiryAnswers.do?method=showRegentInquiry" paramName="professorship" paramProperty="externalId" paramId="professorshipOID"
		module="/publico" target="_blank">
		<b><bean:message key="link.inquiry.view" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.regent" bundle="INQUIRIES_RESOURCES"/></b>
	</html:link>
</logic:present>

<logic:notEmpty name="coursesResultResume">
	<p class="mtop15"><strong><bean:message key="title.inquiry.resume.course" bundle="INQUIRIES_RESOURCES"/></strong></p>
	<fr:view name="coursesResultResume">
		<fr:layout name="course-degrees-inquiry-resume">
			<fr:property name="classes" value="teacher-resume"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="regentTeachersResumeList">
	<p class="mtop15">
		<strong><bean:message key="title.inquiry.resume.regent" bundle="INQUIRIES_RESOURCES"/></strong>
	</p>	
	<fr:view name="regentTeachersResumeList">
		<fr:layout name="regent-teachers-inquiry-resume">
			<fr:property name="extraColumn" value="true"/>
			<fr:property name="classes" value="responsable-resume"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="teachersWithNoResults">
	<p class="mtop2">
		<strong><bean:message key="title.inquiry.teachersWithNoResults.regent" bundle="INQUIRIES_RESOURCES"/></strong>
	</p>	
		
	<div style="margin-bottom: 30px;">
		<logic:iterate id="professorship" name="teachersWithNoResults">
			<p>
				<fr:view name="professorship" property="person.name"/>
				(<html:link action="/viewQUCInquiryAnswers.do?method=showTeacherInquiry" paramName="professorship" paramProperty="externalId" paramId="professorshipOID"
					module="/publico" target="_blank">
					<bean:message key="link.inquiry.report" bundle="INQUIRIES_RESOURCES"/>
				</html:link>)
			</p>
		</logic:iterate>
	</div>
</logic:notEmpty>

<ul class="legend-general" style="margin-top: 20px;"> 
	<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-1">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.excelent" bundle="INQUIRIES_RESOURCES"/></li>
	<logic:notPresent name="first-sem-2010">
		<li><span class="legend-bar-7">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.veryGood" bundle="INQUIRIES_RESOURCES"/></li>	
	</logic:notPresent>
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><bean:message key="label.inquiry.mandatoryCommentsNumber" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>

<ul class="legend-general" style="margin-top: 0px;"> 
	<li><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>