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
 
<style media="print"> 
 
div.xpto {
page-break-inside: avoid;
page-break-after: always;
}
 
</style> 

<style> 
 
body {
font-size: 12px;
line-height: 15px;
font-family: Arial;
background: #eee;
text-align: center;
margin: 40px 20px 80px 20px;
}
h1 {
font-size: 22px;
line-height: 30px;
margin: 15px 0;
}
h2 {
font-size: 17px;
line-height: 30px;
margin: 40px 0 10px 0;
}
p {
margin: 10px 0 5px 0;
}
 
#page {
margin: 20px auto;
text-align: left;
padding: 20px 30px;
width: 900px;
background: #fff;
border: 1px solid #ddd;
}
 
/* ---------------------------
      STRUCTURAL TABLE 
--------------------------- */
 
table.structural {
border-collapse: collapse;
}
table.structural tr td {
padding: 0;
vertical-align: top;
}
 
/* ---------------------------
      TABLE GRAPH 
--------------------------- */
 
div.graph {
margin: 15px 0px 30px 0px;
}
 
table.graph, table.graph-2col {
color: #555;
}
 
table.graph {
border-collapse: collapse;
margin: 5px 0 5px 0;
/* width: 1000px; */
}
table.graph th {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 0px;
font-weight: normal;
}
table.graph td {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 5px !important;
text-align: center;
	vertical-align: middle !important;
}
table.graph tr.thead th {
border-bottom: 2px solid #ccc;
}
table.graph tr th:first-child {
padding: 5px 0px 5px 0px;
}
table.graph tr.thead th {
vertical-align: bottom;
text-align: center;
color: #555;
text-transform: uppercase;
font-size: 9px;
padding: 5px 5px;
background: #f5f5f5;
}
table.graph tr th {
width: 300px;
}
table.graph tr.thead th {
width: 55px;
}
table.graph tr.thead th.first {
width: 300px;
}
 
table.graph tr td.x1, table tr td.x2, table tr td.x3, table tr td.x4 {
background: #f5f5f5;
}
table.graph tr td.x1 {
border-right: 1px solid #ccc;
}
 
/* specific */
 
table.general-results td {
width: 30px;
}
 
div.workload-left, div.workload-right   {
float: left;
width: 435px;
margin-top: 30px;
}
div.workload-left   {
padding-right: 30px;
}
 
 
tr.sub th {
padding-left: 20px !important;
}
 
/* ---------------------------
      TABLE GRAPH 2COL
--------------------------- */
 
table.graph-2col {
border-collapse: collapse;
margin: 5px 0 5px 0;
/* width: 1000px; */
}
table.graph-2col th {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 0px;
font-weight: normal;
/* width: 380px; */
}
table.graph-2col td {
text-align: right !important;
border-top: none;
border-bottom: 1px solid #ccc !important;
padding: 5px 5px !important;
text-align: center;
}
 
/* ---------------------------
      INSIDE TABLE 
--------------------------- */
 
table.graph table {
width: 500px;
border-collapse: collapse;
}
table.graph table tr td {
border: none;
padding: 0 !important;
}
table.graph table tr td div {
}
 
/* ---------------------------
      GRAPH BARS 
--------------------------- */
 
div.graph-bar-horz {
height: 21px;
-moz-border-radius: 3px;
border-radius: 3px;
float: left;
background: #3573a5;
}
div.graph-bar-horz-number {
float: left;
padding-top: 2px;
padding-left: 6px;
}
 
/* right-aligned bars */
 
table.bar-right div {
float: right;
text-align: right;
}
 
table.bar-right div.graph-bar-horz-number {
padding-right: 10px;
}
 
 
div.bar-yellow, div.bar-red, div.bar-green, div.bar-blue {
width: 40px;
height: 19px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
}
div.bar-yellow {
background: #3574a5;
}
div.bar-red {
background: #3574a5;
}
div.bar-green {
background: #3574a5;
}
div.bar-blue {
background: #3574a5;
}
 
div.graph-bar-19-1,
div.graph-bar-19-2,
div.graph-bar-19-3,
div.graph-bar-19-4,
div.graph-bar-19-5,
div.graph-bar-19-6,
div.graph-bar-19-7,
div.graph-bar-19-8,
div.graph-bar-19-9,
div.graph-bar-grey {
height: 18px;
/*
-moz-border-radius: 3px;
border-radius: 3px;
*/
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
}
div.first-bar {
-moz-border-radius-topleft: 3px;
-moz-border-radius-bottomleft: 3px;
border-top-left-radius: 3px;
border-bottom-left-radius: 3px;
}
div.last-bar {
-moz-border-radius-topright: 3px;
-moz-border-radius-bottomright: 3px;
border-top-right-radius: 3px;
border-bottom-right-radius: 3px;
}
div.graph-bar-yellow {
background: #e9a73d;
}
div.graph-bar-red {
background: #ce423d;
}
div.graph-bar-green {
background: #2d994a;
}
div.graph-bar-blue {
background: #006ca2;
}
div.graph-bar-grey {
background: #eee;
color: #888;
font-weight: normal;
-moz-border-radius: 3px;
border-radius: 3px;
}
 
.neutral div.graph-bar-19-1 { background: #3573a5; } /* red */ 
.neutral div.graph-bar-19-2 { background: #3573a5; } /* red */
.neutral div.graph-bar-19-3 { background: #3573a5; } /* red */
.neutral div.graph-bar-19-4 { background: #3573a5; } /* yellow */
.neutral div.graph-bar-19-5 { background: #3573a5; } /* green */
.neutral div.graph-bar-19-6 { background: #3573a5; } /* green */
.neutral div.graph-bar-19-7 { background: #3573a5; } /* green */
.neutral div.graph-bar-19-8 { background: #3573a5; } /* green */
.neutral div.graph-bar-19-9 { background: #3573a5; } /* green */
 
.classification div.graph-bar-19-1 { background: #c04439; } /* red */ 
.classification div.graph-bar-19-2 { background: #ca623a; } /* red */
.classification div.graph-bar-19-3 { background: #cc7d3f; } /* red */
.classification div.graph-bar-19-4 { background: #ddb75b; } /* yellow */
.classification div.graph-bar-19-5 { background: #91a749; } /* green */
.classification div.graph-bar-19-6 { background: #74a14e; } /* green */
.classification div.graph-bar-19-7 { background: #5c9b4e; } /* green */
.classification div.graph-bar-19-8 { background: #478f47; } /* green */
.classification div.graph-bar-19-9 { background: #438a43; } /* green */
 
span.legend-bar {
padding: 0 3px;	
}
 
span.legend-bar-19-1,
span.legend-bar-19-2,
span.legend-bar-19-3,
span.legend-bar-19-4,
span.legend-bar-19-5,
span.legend-bar-19-6,
span.legend-bar-19-7,
span.legend-bar-19-8,
span.legend-bar-19-9 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 2px 5px;
font-size: 6px;
font-weight: bold;
}
 
span.legend-bar-19-1 { background: #3574a5; }
span.legend-bar-19-2 { background: #3574a5; }
span.legend-bar-19-3 { background: #3574a5; }
span.legend-bar-19-4 { background: #3574a5; }
span.legend-bar-19-5 { background: #3574a5; }
span.legend-bar-19-6 { background: #3574a5; }
span.legend-bar-19-7 { background: #3574a5; }
span.legend-bar-19-8 { background: #3574a5; }
span.legend-bar-19-9 { background: #3574a5; }
 
 
/* ---------------------------
      SUMMARY
--------------------------- */
 
div.summary table th {
border: none;
padding: 3px 0;
width: 200px;
}
div.summary table td {
text-align: left;
border: none;
padding: 3px 0;
}
 
 
/* ---------------------------
      TOOLTIPS
--------------------------- */
 
a {
color: #105c93;
}
a.help, a.helpleft {
position: relative;
text-decoration: none;
border: none !important;
width: 20px;
text-transform: none;
font-size: 12px;
font-weight: normal;
}
a.help span, a.helpleft span { display: none; }
a.help:hover, a.helpleft:hover {
z-index: 100;
border: 1px solic transparent;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
}
a.helpleft:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 20px;
left: 20px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
}
a.helpleft[class]:hover span {
right: 20px;
}
 
/* ---------------------------
      CHARTS
--------------------------- */
 
div.chart {
clear:both;
/* min-width: 600px; */
background: #eee;
padding: 10px;
}
 
 
table.graph tr td div.chart {
clear:both;
/* min-width: 600px; */
background: none;
padding: 0;
}
</style>

</head>

<body class="survey">  
 
<div id="page"> 
 
<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p>
	<em><bean:write name="executionPeriod" property="semester"/>º Semestre de <bean:write name="executionPeriod" property="executionYear.year"/></em>
</p>

<h1>QUC - Resultados dos Inquéritos aos Alunos: <bean:write name="executionCourse" property="name"/></h1>

<p>Docente: <bean:write name="professorship" property="person.name"/></p>

<h2>Resultados gerais do Docente</h2>
<table class="structural" style="margin-top: 5px;"> 
	<tr> 
		<td style="padding-right: 20px;"> 
			<!-- fr:view name="teacherGroupResultsSummaryBean" layout="general-result-resume"/-->
		</td>
	</tr> 
</table> 

<logic:iterate indexId="iter" id="blockResult" name="blockResultsSummaryBeans">
	<h2><bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/></h2>
	<logic:iterate id="groupResult" name="blockResult" property="groupsResults">		
		<fr:view name="groupResult" />
	</logic:iterate>
</logic:iterate>

</div>

</body>
</html>
