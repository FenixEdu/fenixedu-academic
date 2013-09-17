<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<jsp:include page="qucChooseSemesterAndHeaderMenu.jsp"/>

<h3><bean:message key="link.quc.curricularUnits" bundle="INQUIRIES_RESOURCES"/> (<bean:write name="executionSemester" property="executionYear.year"/> - 
	<bean:write name="executionSemester" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/>)</h3>

<p><bean:message key="message.department.curricularUnits" bundle="INQUIRIES_RESOURCES"/></p>

<logic:notEmpty name="competenceCoursesByScientificArea">
	<logic:iterate id="entrySet" name="competenceCoursesByScientificArea">
		<p class="mtop15"><strong><bean:write name="entrySet" property="key.name"/></strong></p>
		<fr:view name="entrySet" property="value">
			<fr:layout name="department-curricularCourses-resume">
				<fr:property name="extraColumn" value="true"/>
				<fr:property name="classes" value="department-resume"/>
				<fr:property name="method" value="showUCResultsAndComments"/>
				<fr:property name="action" value="/viewQucResults.do"/>
				<fr:property name="contextPath" value="/departamento/departamento"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
	
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
</logic:notEmpty>