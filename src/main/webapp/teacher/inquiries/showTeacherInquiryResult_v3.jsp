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
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/jquery.min.js"></script> 
<script type="text/javascript">jQuery.noConflict();</script> 
<script type="text/javascript">var example = 'bar-basic', theme = 'default';</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/jquery/scripts.js"></script>
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />
 
<style media="print"> 
 
div.xpto {
page-break-inside: avoid;
page-break-after: always;
}
 
</style> 

<style> 
/* Teacher specific */

div.workload-left {
margin-top: 0px;
}

</style>



</head>

<body class="survey-results teacher">  

<bean:define id="exceptionClass" value=""/>
<logic:present name="first-sem-2010">
	<bean:define id="exceptionClass" type="java.lang.String">
		<bean:write name="first-sem-2010"/>
	</bean:define>
</logic:present>
<div id="page" class="<%= exceptionClass %>"> 
 
<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p>
	<em style="float: left;"><bean:write name="executionPeriod" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/>
		 de <bean:write name="executionPeriod" property="executionYear.year"/></em>
	<em style="float: right;">Data de produção dos resultados: <fr:view name="resultsDate" layout="no-time"/></em>
</p>

<div style="clear: both;"></div>
<h1>QUC - Resultados dos Inquéritos aos Alunos: <bean:write name="executionCourse" property="name"/></h1>

<p>Docente: <b><bean:write name="professorship" property="person.name"/></b></p>
<p>Tipo de aula: <b><bean:message name="shiftType" property="name"  bundle="ENUMERATION_RESOURCES"/></b></p>

<h2>Resultados gerais do Docente</h2>
 
<div style="width: 300px;">
	<fr:view name="teacherGroupResultsSummaryBean" layout="general-result-resume"/>
	</div>
	<ul class="legend-general-teacher" style="margin-top: 15px;">
		<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li>
		<li><span class="legend-bar-1">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.excelent" bundle="INQUIRIES_RESOURCES"/></li>
		<logic:notPresent name="first-sem-2010">
			<li><span class="legend-bar-7">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.veryGood" bundle="INQUIRIES_RESOURCES"/></li>	
		</logic:notPresent>
		<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
		<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
		<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
		<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li>
	</ul>

	<logic:iterate indexId="iter" id="blockResult" name="blockResultsSummaryBeans">
		<h2 style="clear: both"><bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/></h2>
		<logic:iterate id="groupResult" name="blockResult" property="groupsResults">		
			<fr:view name="groupResult" />
		</logic:iterate>
	</logic:iterate>
	
	<logic:notEmpty name="teacherEvaluation">
		<a href="#" class="helpleft">[?]<span style="width: 500px;"><bean:write name="teacherEvaluation" property="inquiryQuestion.toolTip"/></span></a>
		<bean:write name="teacherEvaluation" property="inquiryQuestion.label"/>:	
		<logic:notEmpty name="teacherEvaluation" property="value">
			<bean:write name="teacherEvaluation" property="value"/>
		</logic:notEmpty>
		<logic:empty name="teacherEvaluation" property="value">
			<bean:message key="label.inquiry.teacher.unsufficientAnswers" bundle="INQUIRIES_RESOURCES"/>
		</logic:empty>
	</logic:notEmpty>
</div>

</body>
</html>
