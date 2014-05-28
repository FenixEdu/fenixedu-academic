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
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<fr:view name="competenceCoursesToAudit">
	<fr:layout name="department-curricularCourses-resume">
		<fr:property name="extraColumn" value="true"/>
		<fr:property name="classes" value="department-resume"/>
		<fr:property name="method" value="showUCResultsAndComments"/>
		<fr:property name="action" value="/viewQucResults.do"/>
		<fr:property name="contextPath" value="/departamento/departamento"/>
	</fr:layout>
</fr:view>

<ul class="legend-general" style="margin-top: 20px;"> 
	<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li>
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

<h3 class="mtop15 mbottom05"><bean:message key="edit" bundle="APPLICATION_RESOURCES"/></h3>
<fr:form action="/qucAudit.do">
	<html:hidden property="method" value="editProcess"/>
	<fr:edit id="auditProcessBean" name="auditProcessBean" action="/qucAudit.do?method=editProcess">
		<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit">			
			<fr:slot name="conclusions" key="label.inquiry.audit.conclusions" layout="longText">
				<fr:property name="columns" value="65"/>
				<fr:property name="rows" value="7"/>
			</fr:slot>
			<fr:slot name="measuresToTake" key="label.inquiry.audit.measuresToTake" layout="longText">
				<fr:property name="columns" value="65"/>
				<fr:property name="rows" value="7"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thtop mtop05"/>
			<fr:property name="columnClasses" value=",, noborder"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.save" bundle="APPLICATION_RESOURCES"/></html:submit>
	<html:submit onclick="this.form.method.value='viewProcessDetails'"><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>

