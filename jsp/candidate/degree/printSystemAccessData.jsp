<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/transitional.css" rel="stylesheet" media="screen" type="text/css" />
</head>

<body>

<jsp:include page="./printSystemAccessDataTemplate.jsp" flush="true" />
	<div class="break-before">
</div>

<jsp:include page="./printSystemAccessDataTemplate.jsp" flush="true" />


</body>
</html:html>


