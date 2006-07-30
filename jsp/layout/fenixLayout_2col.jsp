<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/transitional.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_calendars.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" media="screen" type="text/css" />
<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <tiles:getAsString name="title" ignore="true" /></title>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<style type="text/css">
div.pass_warning {
font-weight: normal;
color: #000;
line-height: 1.4em;
padding: 0.5em;
margin-bottom: 1em;
}
div.pass_warning strong {
background-color: #ffa;
}
div.pass_container {
float: left;
}
div.pass_left {
margin-top: 1em;
width: 29em;
float: left;
}
div.pass_req {
background-color: #fafadd;
margin-left: 1em;
border: 1px solid #ccc;
margin-left: 30em;
padding: 0.5em;
}
.tdcheck {
vertical-align: top;
width: 0.5em;
}

table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}

div.pp {
border: 1px solid #ccc;
padding-bottom: 0;
margin: 0.5em 0;
width: 100%;
float: left;
}

table.ppid {
padding: 0.5em;
width: 100%;
background-color: #eee;
}
div.pp img {
float: right;
margin: 4px;
border: 1px solid #ccc;
background-color: #eee;
padding: 2px;
}
table.ppdetails {
background-color: #fff;
border-collapse: collapse;
margin: 0.5em 1em;
}
table.pphigh {
background-color: #ffa;
}
table.ppdetails tr td {
padding: 0.25em;
}
table.ppdetails td.ppleft {
text-align: right;
width: 10em;
}
table.ppdetails td.ppright {
}
table.ppdetails td.ppright2 {
width: 10em;
}
table.ppdetails td.ppleft_mail {
text-align: right;
width: 10em;
}
table.ppdetails tr.highlight {
}
table.ppdetails td.highlight {
background-color: #ffffea;
}

.greyBorderClass {
	background-color: #EBECED;
	border-style: solid;
	border-width: 1px;
	border-color: #909090;
	width: 100%
}
.blackBorderClass {
	background-color: #ffffff;
	border-style: solid;
	border-width: 1px;
	border-color: #909090
}
.boldFontClass { 
	font-weight: bold
}

.instructions {
background-color: #fafadd;
border: 1px solid #ccc;
padding: 0.5em;
/*float: left;*/
}

#inquiry p {
margin-top: 0;
margin-bottom: 0;	
}

div.evalcontainer {
padding: 1em 0;
}
table.evallist {
margin-bottom: 1em;
text-align: center;
border-collapse: collapse;
}
table.evallist tr {
}
table.evallist th {
padding: 0.25em 0.5em;
border: 1px solid #ccc;
background-color: #eaeaea;
font-weight: normal;
}
table.evallist td {
border-top: 1px solid #ddd;
border-bottom: 1px solid #ddd;
border-left: 1px solid #ddd;
border-right: 1px solid #ddd;
padding: 0.25em 0.5em;
}
table.evallist td.evallist_empty {
background-color: #fff;
border: none;
padding: 0.75em;
}
.left {
text-align: left;
}
table.evallist td.title {
padding: 0.5em;
font-weight: bold;
text-align: left;
background-color: #f5f5f5;
}

table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
tr.el_highlight {
background-color: #fcfcee;
}
tr.el_highlight span.green {
color: #272;
}
td.el_courses {
text-align: left;
}

div.evalcontainer {
/*background-color: #fafafa;*/
padding: 1em 0;
/*border: 2px solid #aca;*/
}
table.evallist {
margin-bottom: 1em;
text-align: center;
border-collapse: collapse;
}
table.evallist tr {
}
table.evallist th {
padding: 0.25em 0.5em;
background-color: #eaeaea;
font-weight: normal;
}
table.evallist td {
border-bottom: 1px solid #ddd;
border-top: 1px solid #ddd;
padding: 0.25em 0.5em;
}
table.evallist td.evallist_empty {
text-align: left;
background-color: #fff;
border: none;
padding: 1.0em 0 0 0;
}
.left {
text-align: left;
}
table.evallist td.title {
padding: 0.3em;
text-align: left;
background-color: #fcfcee;
}

table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
table tr.eval_gray {
background-color: #fff;
color: #777;
}
td.eval_green {
background-color: #f5f5f5;
color: #000;
}
tr.eval_title {
}
tr.eval_title td {
border: none;
}
tr.eval_title td p {
margin-top: 2em;
text-align: left;
padding-bottom: 0em;
margin-bottom: 0.25em;
}

</style>
</head>
<body>
<%-- Layout component parameters : title, context, header, navGeral, navLocal, body, footer --%>
<!-- Context -->
<tiles:insert attribute="context" ignore="true"/>
<!--End Context -->
<!-- Header -->
<div id="header">	
	<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
    <p><!-- Title goes here --><tiles:getAsString name="serviceName" /></p>
</div>
<bean:define id="supportLink" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
<div id="hdr-nav"><a href="<%= supportLink %>"><img src="<%= request.getContextPath() %>/images/sup-bar.gif" alt="<bean:message key="sup-bar" bundle="IMAGE_RESOURCES" />"/></a><a href="<%= request.getContextPath() %>/logoff.do"><img src="<%= request.getContextPath() %>/images/logoff-bar.gif" alt="<bean:message key="logoff-bar" bundle="IMAGE_RESOURCES" />"/></a></div>
<div><!-- hack to make "hdr-nav" appear in IE --></div>
<!-- End Header -->
<!-- NavGeral -->
<tiles:insert attribute="navGeral" />

<tiles:insert page="/commons/personalInfoTitleBar.jsp" />

<!-- End NavGeral -->
<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0">
   <tr>
     <td id="navlateral" align="left" valign="top" nowrap="nowrap">
     	<tiles:insert attribute="navLocal" ignore="true"/>
    </td>
     <td id="bodycontent" width="100%" align="left" valign="top" >
     	<tiles:insert attribute="body-context" ignore="true"/>
     	<tiles:insert attribute="body" ignore="true"/>
     	<tiles:getAsString name="body-inline" ignore="true"/>
	</td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<div id="footer">
    <tiles:insert attribute="footer" />
</div>
<!--End Footer -->

<script type="text/javascript">
	hideButtons()
</script>

</body>
</html:html>