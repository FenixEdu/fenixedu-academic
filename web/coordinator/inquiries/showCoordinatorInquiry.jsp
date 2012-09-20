<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 

<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" /> 

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
	
	<h1><bean:message key="title.inquiry.quc.coordinator" bundle="INQUIRIES_RESOURCES"/></h1>
	
	<p>
		<logic:notEmpty name="person">
			<bean:write name="person" property="name"/> - 
		</logic:notEmpty> 
		<bean:write name="executionDegree" property="degree.name"/> - <bean:write name="executionDegree" property="degree.sigla"/>  
		(<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)
	</p>
	
	<!-- Coordinator Inquiry -->	
	<logic:iterate id="inquiryBlockDTO" name="coordinatorInquiryBlocks">
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