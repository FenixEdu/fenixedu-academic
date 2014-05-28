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

<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<bean:define id="executionDegree" name="executionDegree" type="net.sourceforge.fenixedu.domain.ExecutionDegree"/>
<h3><bean:write name="executionDegree" property="degree.sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p><bean:message key="message.coordinator.resume.inquiry.begin" bundle="INQUIRIES_RESOURCES"/></p>

<logic:equal name="isResponsible" value="true">	

	<bean:define id="coordinator" name="coordinator" type="net.sourceforge.fenixedu.domain.Coordinator"/>
	<logic:equal name="readMode" value="false">
		<logic:equal name="coursesToAudit" value="true">
			<p><bean:message key="message.coordinator.resume.inquiry.audit" bundle="INQUIRIES_RESOURCES"/></p>
		</logic:equal>
		<logic:equal name="coursesToAudit" value="false">
			<p><bean:message key="message.coordinator.resume.inquiry.notAudit" bundle="INQUIRIES_RESOURCES"/></p>
		</logic:equal>
		<p><bean:message key="message.coordinator.resume.inquiry.end" bundle="INQUIRIES_RESOURCES"/></p>
	
		<html:link action="<%= "/viewInquiriesResults.do?method=showCoordinatorInquiry&executionDegreeOID=" + executionDegree.getExternalId() 
				 + "&degreeCurricularPlanID=" + executionDegree.getDegreeCurricularPlan().getExternalId()
				 + "&coordinatorOID=" + coordinator.getExternalId()	%>"
				 paramName="executionPeriod" paramProperty="externalId" paramId="executionPeriodOID">
				<b><bean:message key="link.inquiry.fillIn" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.coordinator" bundle="INQUIRIES_RESOURCES"/></b>
		</html:link>
		<logic:present name="completionState">
			(<bean:write name="completionState"/>)
		</logic:present>
	</logic:equal>
	<logic:equal name="readMode" value="true">	
		<html:link action="<%= "/viewQUCInquiryAnswers.do?method=showCoordinatorInquiry&executionDegreeOID=" + executionDegree.getExternalId() 
				+ "&coordinatorOID=" + coordinator.getExternalId() %>"
				 paramName="executionPeriod" paramProperty="externalId" paramId="executionPeriodOID" module="/publico" target="_blank">
			<b><bean:message key="link.inquiry.view" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.coordinator" bundle="INQUIRIES_RESOURCES"/></b>
		</html:link>
	</logic:equal>
</logic:equal>

<%--
<logic:notEqual name="isResponsible" value="true">	
	<bean:define id="coordinator" name="coordinator" type="net.sourceforge.fenixedu.domain.Coordinator"/>
	<logic:equal name="readMode" value="true">	
		<html:link action="<%= "/viewQUCInquiryAnswers.do?method=showCoordinatorInquiry&executionDegreeOID=" + executionDegree.getExternalId() 
				+ "&coordinatorOID=" + coordinator.getExternalId() %>"
				 paramName="executionPeriod" paramProperty="externalId" paramId="executionPeriodOID" module="/publico" target="_blank">
			<b><bean:message key="link.inquiry.view" bundle="INQUIRIES_RESOURCES"/> <bean:message key="label.inquiry.coordinator" bundle="INQUIRIES_RESOURCES"/></b>
		</html:link>
	</logic:equal>
</logic:notEqual>
 --%>
 
<logic:iterate id="entrySet" name="coursesResultResumeMap">
	<p class="mtop15"><strong><bean:write name="entrySet" property="key"/>º Ano</strong></p>
	<fr:view name="entrySet" property="value">
		<fr:layout name="coordinator-inquiry-resume">
			<fr:property name="classes" value="coordinator-resume"/>
			<fr:property name="action" value="/viewInquiriesResults.do"/>
			<fr:property name="method" value="showUCResultsAndComments"/>
			<fr:property name="contextPath" value="/coordenador/coordenador"/>
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