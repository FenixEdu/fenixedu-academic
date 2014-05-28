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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 

<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 

<style>
.question {
width: 100%;
border-collapse: collapse;
margin: 10px 0;
}
 
.question, textarea {
color: #444;
}
 
.question th {
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
vertical-align: top;
font-weight: normal;
background: #f5f5f5;
padding: 7px 5px;
line-height: 17px;
}
.question th.firstcol {
width: 400px;
text-align: left;
}
.question td {
padding: 5px;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
text-align: center;
}
.q1col td { text-align: left; }
 
.q9col .col1, .q9col .col9  { width: 30px; }
.q10col .col1, .q10col .col2, .q10col .col10  { width: 20px; }
.q11col .col1, .q11col .col2, .q11col .col3, .q11col .col11  { width: 20px; }
 
th.col1, th.col2, th.col3, th.col4, th.col5, th.col6, th.col7, th.col8, th.col9, th.col10 {
text-align: center !important;
padding: 5px 10px;
}
 
textarea {
border: none;
padding: 5px;
} 
</style>

</head>

<body class="survey-results">  
 
<div id="page">

	<p>
		<em style="float: left;">
			<bean:write name="executionPeriod" property="semester"/>º Semestre - <bean:write name="executionPeriod" property="executionYear.year"/>
		</em>
	</p>
	
	<div style="clear: both;"></div>
	
	<h1><bean:message key="title.inquiry.quc.teacher" bundle="INQUIRIES_RESOURCES"/></h1>
	
	<p><bean:write name="person" property="name"/> - <bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/>  
	 (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</p>
	
	<!-- Teacher Inquiry -->	
	<logic:iterate id="inquiryBlockDTO" name="teacherInquiryBlocks">
		<h2 class="separator2 mtop25">
			<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
		</h2>					
		<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups">					
			<fr:edit name="inquiryGroup">
				<fr:layout>
					<fr:property name="readOnly" value="true"/>
				</fr:layout>
			</fr:edit>
		</logic:iterate>
	</logic:iterate>

</div>
</body>
</html>