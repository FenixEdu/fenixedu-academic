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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml />

<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<bean:define id="executionSemester" name="executionCourseSearchBean" property="executionPeriod"/>
<h3>
	<bean:message key="label.inquiry.audit.processes" bundle="INQUIRIES_RESOURCES"/>
	(<bean:write name="executionSemester" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/> 
		<bean:write name="executionSemester" property="executionYear.year"/>)
</h3>

<logic:present name="success">
	<span class="success0"><bean:message key="label.inquiry.audit.process.success" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>
<logic:notEmpty name="executionCoursesAudits">
	<fr:view name="executionCoursesAudits">
		<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit">
			<fr:slot name="executionCourse.name" key="label.executionCourse.name" bundle="APPLICATION_RESOURCES"/>
			<fr:slot name="teacherAuditor.person.name" key="teacher.docente" bundle="APPLICATION_RESOURCES"/>
			<fr:slot name="studentAuditor.person.name" key="student" bundle="APPLICATION_RESOURCES"/>
			<fr:slot name="approvedByTeacher" key="label.inquiry.audit.approvedByTeacher" layout="boolean-icon">
				<fr:property name="nullAsFalse" value="true"/>
			</fr:slot>
			<fr:slot name="approvedByStudent" key="label.inquiry.audit.approvedByStudent" layout="boolean-icon">
				<fr:property name="nullAsFalse" value="true"/>
			</fr:slot>
			<fr:slot name="availableProcess" key="label.inquiry.audit.availableProcess" layout="boolean-icon">
				<fr:property name="nullAsFalse" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mtop1"/>
			<fr:property name="columnClasses" value=",,,acenter, acenter, acenter"/>
			<fr:property name="linkFormat(view)" value="/qucAudit.do?method=viewProcessDetails&executionCourseAuditOID=${externalId}"/>
			<fr:property name="key(view)" value="link.inquiry.auditProcess" />
			<fr:property name="bundle(view)" value="INQUIRIES_RESOURCES" />
			<fr:property name="linkFormat(edit)" value="/qucAudit.do?method=prepareSelectPersons&executionCourseOID=${executionCourse.externalId}"/>
			<fr:property name="key(edit)" value="link.inquiry.editAuditors" />
			<fr:property name="bundle(edit)" value="INQUIRIES_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="executionCoursesAudits">
	<em><bean:message key="label.inquiry.audit.createdProcessesNone" bundle="INQUIRIES_RESOURCES"/></em>
</logic:empty>

<h3>
	<bean:message key="title.inquiry.audit.search.executionCourse" bundle="INQUIRIES_RESOURCES"/>
	(<bean:write name="executionSemester" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/>
		 <bean:write name="executionSemester" property="executionYear.year"/>)
</h3>

<fr:form action="/qucAudit.do?method=searchExecutionCourse">		
	<fr:edit id="executionCourseSearchBean" name="executionCourseSearchBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.ExecutionCourseQucAuditSearchBean" bundle="APPLICATION_RESOURCES">
			<fr:slot name="executionPeriod" key="label.executionPeriod" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA$ExecutionSemesterQucProvider"/>
				<fr:property name="format" value="${semester}º Semestre ${executionYear.year}" />
			</fr:slot>
			<fr:slot name="executionDegree" key="label.executionDegree" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionDegree1stAnd2ndCycleProviderForExecutionCourseSearchBean" />
				<fr:property name="format" value="${presentationName}" />
				<fr:property name="defaultText" value="label.inquiry.executionCoursesToImprove"/>
				<fr:property name="key" value="true" />
				<fr:property name="bundle" value="INQUIRIES_RESOURCES" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear error0"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit"/></html:submit>
</fr:form>

<logic:present name="executionCourses">
	<fr:view name="executionCourses">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
			<fr:slot name="departments" key="label.department" layout="flowLayout">
				<fr:property name="eachSchema" value="show.department"/>
				<fr:property name="eachLayout" value="values" />
				<fr:property name="htmlSeparator" value="," />
			</fr:slot>
			<fr:slot name="nome" key="label.executionCourse.name"/>
			<fr:slot name="degreePresentationString" key="label.degrees"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
   	    	<fr:property name="columnClasses" value=",,,,noborder"/>
			<fr:property name="linkFormat(create)" value="/qucAudit.do?method=prepareSelectPersons&executionCourseOID=${externalId}"/>
			<fr:property name="key(create)" value="label.create" />
			<fr:property name="bundle(create)" value="APPLICATION_RESOURCES" />
			<fr:property name="visibleIfNot(create)" value="hasExecutionCourseAudit" />
			<fr:property name="linkFormat(view)" value="/qucAudit.do?method=viewProcessDetails&executionCourseAuditOID=${executionCourseAudit.externalId}"/>
			<fr:property name="key(view)" value="link.inquiry.auditProcess" />
			<fr:property name="bundle(view)" value="INQUIRIES_RESOURCES" />
			<fr:property name="visibleIf(view)" value="hasExecutionCourseAudit" />
		</fr:layout>
	</fr:view>
</logic:present>
