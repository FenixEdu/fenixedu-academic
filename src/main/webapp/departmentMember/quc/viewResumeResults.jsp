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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<jsp:include page="qucChooseSemesterAndHeaderMenu.jsp"/>

<h3><bean:message key="link.quc.resume" bundle="INQUIRIES_RESOURCES"/> (<bean:write name="executionSemester" property="executionYear.year"/> - 
	<bean:write name="executionSemester" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/>)</h3>

<p><bean:message key="message.department.resume" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="INQUIRIES_RESOURCES"/></p>

<logic:notEqual name="nothingToImprove" value="true">
	<logic:notEmpty name="competenceCoursesToAudit">
		<p class="mtop15"><strong><bean:message key="title.inquiry.resume.coursesToImprove" bundle="INQUIRIES_RESOURCES"/></strong></p>
		<fr:view name="competenceCoursesToAudit">
			<fr:layout name="department-curricularCourses-resume">
				<fr:property name="extraColumn" value="true"/>
				<fr:property name="classes" value="department-resume"/>
				<fr:property name="method" value="showUCResultsAndComments"/>
				<fr:property name="action" value="/viewQucResults.do"/>
				<fr:property name="contextPath" value="/departamento/departamento"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:notEmpty name="teachersResumeToImprove">
		<p class="mtop15">
			<strong><bean:message key="title.inquiry.resume.teachersToImprove" bundle="INQUIRIES_RESOURCES"/></strong>
		</p>	
		<fr:view name="teachersResumeToImprove">
			<fr:layout name="department-teachers-resume">
				<fr:property name="classes" value="department-resume"/>
			</fr:layout>
		</fr:view>
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
		<li><bean:message key="label.inquiry.questionsToImprove" bundle="INQUIRIES_RESOURCES"/></li> 
	</ul>
	
	<ul class="legend-general" style="margin-top: 0px;"> 
		<li><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</li> 
		<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
		<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
		<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
		<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
	</ul>
</logic:notEqual>