<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print_new.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print_new.css" rel="stylesheet" media="print" type="text/css" />
<title><%=Instalation.getInstance().getInstalationName() %> Intranet</title>
</head>
<body>
<tiles:insert attribute="body" ignore="true"/>
</body>
</html:html>