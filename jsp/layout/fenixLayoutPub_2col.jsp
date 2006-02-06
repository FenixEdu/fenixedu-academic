<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist_pub.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<title><tiles:getAsString name="title" ignore="true" /></title>
</head>
<body>
<%-- Layout component parameters : title, header, navGeral, navLocal, body, footer --%>
<!-- Header -->
<table width="100%%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="header" width="25%"><img alt="IST" height="60" src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="192" /></td>
    <td class="header"><div align="right"><h1><tiles:getAsString name="serviceName" /></h1></div></td>
  </tr>
</table>
<!-- End Header -->
<!-- NavGeral -->
<table width="100%%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="navbargeral"><tiles:insert attribute="navGeral" />
    </td>
  </tr>
</table>
<!-- End NavGeral -->
<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td width="25%" align="left" valign="top" nowrap="nowrap" class="navlateral"><tiles:insert attribute="navLocal" />
    </td>
     <td width="100%" align="left" valign="top"  class="bodycontent"><tiles:insert attribute="body" />
	</td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<table width="100%%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="footer"><tiles:insert attribute="footer" />
    </td>
  </tr>
</table>
<!--End Footer -->
</body>
</html:html>

