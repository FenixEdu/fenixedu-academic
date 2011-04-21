<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" media="screen" type="text/css" />

<bean:define id="executionDegree" name="executionDegree" type="net.sourceforge.fenixedu.domain.ExecutionDegree"/>
<h3><bean:write name="executionDegree" property="degree.sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p><bean:message key="message.coordinator.resume.inquiry.begin" bundle="INQUIRIES_RESOURCES"/></p>
<logic:equal name="coursesToAudit" value="true">
	<p><bean:message key="message.coordinator.resume.inquiry.audit" bundle="INQUIRIES_RESOURCES"/></p>
</logic:equal>
<logic:equal name="coursesToAudit" value="false">
	<p><bean:message key="message.coordinator.resume.inquiry.notAudit" bundle="INQUIRIES_RESOURCES"/></p>
</logic:equal>
<p><bean:message key="message.coordinator.resume.inquiry.end" bundle="INQUIRIES_RESOURCES"/></p>

<html:link action="<%= "/viewInquiriesResults.do?method=showCoordinatorInquiry&executionDegreeOID=" + executionDegree.getExternalId() + "&degreeCurricularPlanID=" + executionDegree.getDegreeCurricularPlan().getIdInternal() %>"
		 paramName="executionPeriod" paramProperty="externalId" paramId="executionPeriodOID">
		<b><bean:message key="link.inquiry.fillIn" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.coordinator" bundle="INQUIRIES_RESOURCES"/></b>
</html:link>
<logic:present name="completionState">
	(<bean:write name="completionState"/>)
</logic:present>

<logic:iterate id="entrySet" name="coursesResultResumeMap">
	<p class="mtop15"><strong><bean:write name="entrySet" property="key"/>º Ano</strong></p>
	<fr:view name="entrySet" property="value">
		<fr:layout name="coordinator-inquiry-resume">
			<fr:property name="classes" value="coordinator-resume"/>
		</fr:layout>
	</fr:view>
</logic:iterate>

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