<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print_new.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print_new.css" rel="stylesheet" media="print" type="text/css" />
<title>.IST Intranet</title>
</head>
<body>
<tiles:insert attribute="body" ignore="true"/>
</body>
</html:html>