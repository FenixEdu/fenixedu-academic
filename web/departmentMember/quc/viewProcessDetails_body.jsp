<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<logic:present name="success">
	<span class="success0"><bean:message key="label.inquiry.audit.process.success" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>

<bean:define id="approvedByOther" name="approvedByOther" type="java.lang.String"/>
<bean:define id="approvedBySelf" name="approvedBySelf" type="java.lang.String"/>

<fr:form action="/qucAudit.do?method=editProcess">
	<logic:notEqual name="auditProcessBean" property="<%= approvedByOther %>" value="true">
		<fr:edit id="auditProcessBean" name="auditProcessBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit">
				<fr:slot name="measuresToTake" key="label.inquiry.audit.measuresToTake" layout="longText">
					<fr:property name="columns" value="65"/>
					<fr:property name="rows" value="7"/>
				</fr:slot>
				<fr:slot name="conclusions" key="label.inquiry.audit.conclusions" layout="longText">
					<fr:property name="columns" value="65"/>
					<fr:property name="rows" value="7"/>
				</fr:slot>
				<fr:slot name="<%= approvedBySelf %>" key="<%= "label.inquiry.audit." + approvedBySelf %>"/>
				<fr:slot name="<%= approvedByOther %>" key="<%= "label.inquiry.audit." + approvedByOther %>" readOnly="true" layout="boolean-icon">
					<fr:property name="nullAsFalse" value="true"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="columnClasses" value=",, noborder"/>
			</fr:layout>
		</fr:edit>
	</logic:notEqual>		
	<logic:equal name="auditProcessBean" property="<%= approvedByOther %>" value="true">
		<fr:edit id="auditProcessBean" name="auditProcessBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit">
				<fr:slot name="measuresToTake" key="label.inquiry.audit.measuresToTake" readOnly="true"/>
				<fr:slot name="conclusions" key="label.inquiry.audit.conclusions" readOnly="true"/>
				<fr:slot name="<%= approvedBySelf %>" key="<%= "label.inquiry.audit." + approvedBySelf %>"/>
				<fr:slot name="<%= approvedByOther %>" key="<%= "label.inquiry.audit." + approvedByOther %>" layout="boolean-icon" readOnly="true">
					<fr:property name="nullAsFalse" value="true"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="columnClasses" value=",, noborder"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>

	<html:submit><bean:message key="button.submit"/></html:submit>
</fr:form>

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