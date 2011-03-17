<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/jquery.min.js"></script> 
<script type="text/javascript">jQuery.noConflict();</script> 
<script type="text/javascript">var example = 'bar-basic', theme = 'default';</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/jquery/scripts.js"></script>
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen" type="text/css" />
 
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

<body class="survey-results">  
 
<div id="page"> 
 
<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p>
	<em style="float: left;"><bean:write name="executionPeriod" property="semester"/>º Semestre de <bean:write name="executionPeriod" property="executionYear.year"/></em>
	<em style="float: right;">Data de produção dos resultados: <fr:view name="resultsDate" layout="no-time"/></em>
</p>

<div style="clear: both;"></div>
<h1>QUC - Resultados dos Inquéritos aos Alunos: <bean:write name="executionCourse" property="name"/></h1>

<p>Docente: <b><bean:write name="professorship" property="person.name"/></b></p>
<p>Tipo de aula: <b><bean:message name="shiftType" property="name"  bundle="ENUMERATION_RESOURCES"/></b></p>

<h2>Resultados gerais do Docente</h2>
<%-- <table class="structural" style="margin-top: 5px;"> 
	<tr> 
		<td style="padding-right: 20px;">--%> 
		<div style="width: 300px;">
			<fr:view name="teacherGroupResultsSummaryBean" layout="general-result-resume"/>
			</div>
			<ul class="legend-general-teacher" style="margin-top: 15px;">
				<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li>
				<li><span class="legend-bar-1">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.excelent" bundle="INQUIRIES_RESOURCES"/></li>
				<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
				<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
				<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
				<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li>
			</ul>
		<%-- </td>
	</tr> 
</table> --%>

<logic:iterate indexId="iter" id="blockResult" name="blockResultsSummaryBeans">
	<h2 style="clear: both"><bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/></h2>
	<logic:iterate id="groupResult" name="blockResult" property="groupsResults">		
		<fr:view name="groupResult" />
	</logic:iterate>
</logic:iterate>

</div>

</body>
</html>
