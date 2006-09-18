<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />

</head>

<body class="registration" id="pagewrapper_registration">

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />



</body>

</html:html>
