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

<p>
	<html:link page="/qucAudit.do?method=showAuditProcesses">
		<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<bean:define id="executionSemester" name="executionCourseAudit" property="executionCourse.executionPeriod"/>
<h3><bean:write name="executionSemester" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/>
	 <bean:write name="executionSemester" property="executionYear.year"/></h3> 

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

<h3 class="mtop2 mbottom05"><bean:message key="title.inquiry.audit.processData" bundle="INQUIRIES_RESOURCES"/></h3>
<logic:present name="success">
	<span class="success0"><bean:message key="label.inquiry.audit.process.success" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>

<fr:view name="executionCourseAudit">
	<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit">
		<fr:slot name="teacherAuditor.person.name" key="label.teacher" bundle="APPLICATION_RESOURCES"/>
		<fr:slot name="studentAuditor.person.name" key="student" bundle="APPLICATION_RESOURCES"/>		
		<fr:slot name="conclusions" key="label.inquiry.audit.conclusions"/>
		<fr:slot name="measuresToTake" key="label.inquiry.audit.measuresToTake"/>
		<fr:slot name="approvedByTeacher" key="label.inquiry.audit.approvedByTeacher" layout="boolean-icon">
			<fr:property name="nullAsFalse" value="true"/>
		</fr:slot>
		<fr:slot name="approvedByStudent" key="label.inquiry.audit.approvedByStudent" layout="boolean-icon">
			<fr:property name="nullAsFalse" value="true"/>
		</fr:slot>
		<fr:slot name="executionCourseAuditFiles" key="label.archive.options.files" bundle="APPLICATION_RESOURCES" layout="list">
			<fr:property name="eachLayout" value="link"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thleft thnowrap thtop mbottom05"/>		
	</fr:layout>
</fr:view>

<bean:define id="approvedByOther" name="approvedByOther" type="java.lang.String"/>
<bean:define id="approvedBySelf" name="approvedBySelf" type="java.lang.String"/>
<logic:equal name="executionCourseAudit" property="<%= approvedBySelf %>" value="true">
	<logic:notEqual name="executionCourseAudit" property="<%= approvedByOther %>" value="true">
		<p>Nem os comentários nem os ficheiros podem ser alterados porque lacrou o processo. Se desejar alterar o conteúdo dos comentários terá que deslacrar o processo.</p>
	</logic:notEqual>
	<logic:equal name="executionCourseAudit" property="<%= approvedByOther %>" value="true">
		<p>Não é possível modificar os dados do processo visto que este se encontra lacrado por ambos os auditores.</p>
	</logic:equal>
</logic:equal>
<logic:notEqual name="executionCourseAudit" property="<%= approvedBySelf %>" value="true">
	<logic:equal name="executionCourseAudit" property="<%= approvedByOther %>" value="true">
		<p>Nem os comentários nem os ficheiros podem ser alterados porque o processo se encontra lacrado pelo outro auditor. Se desejar alterar o conteúdo dos comentários terá que pedir ao outro auditor para deslacrar.</p>
	</logic:equal>
</logic:notEqual>

<style>
	span.disabledlink {
	color: #888;
	border-bottom: 1px solid #aaa;
	cursor: pointer;
	}
</style>
<p class="mtop05">
	<logic:notEqual name="executionCourseAudit" property="<%= approvedBySelf %>" value="true">
		<html:link page="/qucAudit.do?method=sealProcess" paramId="executionCourseAuditOID" paramName="executionCourseAudit" paramProperty="externalId">
			<bean:message key="link.inquiry.seal" bundle="INQUIRIES_RESOURCES"/>
		</html:link> | 
	</logic:notEqual>	
	<logic:notEqual name="executionCourseAudit" property="<%= approvedByOther %>" value="true">
		<logic:equal name="executionCourseAudit" property="<%= approvedBySelf %>" value="true">
			<html:link page="/qucAudit.do?method=unsealProcess" paramId="executionCourseAuditOID" paramName="executionCourseAudit" paramProperty="externalId">
				<bean:message key="link.inquiry.unseal" bundle="INQUIRIES_RESOURCES"/>
			</html:link> | 
			<span class="disabledlink"><bean:message key="link.edit" bundle="APPLICATION_RESOURCES"/></span> | 
			<span class="disabledlink"><bean:message key="link.inquiry.manageFiles" bundle="INQUIRIES_RESOURCES"/></span>
		</logic:equal>	
		<logic:notEqual name="executionCourseAudit" property="<%= approvedBySelf %>" value="true">
			<html:link page="/qucAudit.do?method=prepareEditProcess" paramId="executionCourseAuditOID" paramName="executionCourseAudit" paramProperty="externalId">
				<bean:message key="link.edit" bundle="APPLICATION_RESOURCES"/>
			</html:link> | 
			<html:link page="/qucAudit.do?method=prepareManageFiles" paramId="executionCourseAuditOID" paramName="executionCourseAudit" paramProperty="externalId">
				<bean:message key="link.inquiry.manageFiles" bundle="INQUIRIES_RESOURCES"/>
			</html:link>
		</logic:notEqual>
	</logic:notEqual>
	<logic:equal name="executionCourseAudit" property="<%= approvedByOther %>" value="true">		
		<logic:equal name="executionCourseAudit" property="<%= approvedBySelf %>" value="true">
			<span class="disabledlink"><bean:message key="link.inquiry.unseal" bundle="INQUIRIES_RESOURCES"/></span> | 
		</logic:equal>
		<span class="disabledlink"><bean:message key="link.edit" bundle="APPLICATION_RESOURCES"/></span> | 
		<span class="disabledlink"><bean:message key="link.inquiry.manageFiles" bundle="INQUIRIES_RESOURCES"/></span>
	</logic:equal>
</p>