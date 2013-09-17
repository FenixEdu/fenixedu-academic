<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/general.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/color.css" rel="stylesheet" media="screen" type="text/css" />
</head>

<body>

<jsp:include page="./printSystemAccessDataTemplate.jsp" flush="true" />
	<div class="break-before">
</div>

<jsp:include page="./printSystemAccessDataTemplate.jsp" flush="true" />


</body>
</html:html>


