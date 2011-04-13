<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 

<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
 
<style>
.question {
border-collapse: collapse;
margin: 10px 0;
}
.question th {
padding: 5px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
vertical-align: top;
}
.question td {
padding: 5px;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
}
 
textarea {
border: none;
}
</style>

</head>

<body class="survey-results">  
 
<div id="page">
	<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>
	
	<p><em style="float: left;">
		<bean:write name="executionPeriod" property="semester"/>º Semestre - <bean:write name="executionPeriod" property="executionYear.year"/>
	</em></p>
	<div style="clear: both;"></div>
	
	<h1><bean:message key="title.inquiry.quc.delegate" bundle="INQUIRIES_RESOURCES"/></h1>
	
	<bean:define id="degreeSigla" name="executionDegree" property="degree.sigla" type="java.lang.String"/>
	
	<p><span class="highlight1"><bean:write name="executionCourse" property="name"/> (<bean:write name="executionCourse" property="sigla"/>)</span> -  
	<bean:write name="degreeSigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</p>
	
	<p>
	<logic:notEmpty name="year">
		<bean:define id="delegateYear" name="year"/>
		<bean:message key="message.teacher.delegate.inquiry" bundle="INQUIRIES_RESOURCES" arg0="<%= delegateYear.toString() %>" arg1="<%= degreeSigla %>"/>
	</logic:notEmpty>
	<logic:empty name="year">
		<bean:message key="message.teacher.delegate.inquiry.notAnswered" bundle="INQUIRIES_RESOURCES" arg0="<%= degreeSigla %>"/>
	</logic:empty>
	</p>
	
	<!-- Delegate Inquiry -->	
	<logic:iterate id="inquiryBlockDTO" name="delegateInquiryBlocks">
		<h3 class="separator2 mtop25">
			
				<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
			
		</h3>					
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