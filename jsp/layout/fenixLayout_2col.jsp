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
    <p><tiles:getAsString name="serviceName" /></p>
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