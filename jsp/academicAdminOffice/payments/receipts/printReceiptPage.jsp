<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html xhtml="true">

<head>
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_calendars.css" rel="stylesheet" media="print" type="text/css" />
<title></title>
</head>
<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<%-- The Original Payment-Receipt --%>
	<jsp:include page="./printReceiptTemplatePage.jsp" flush="true" />

	<div class="breakafter"></div>
	
	<%-- Copy of Payment-Receipt --%>
	<jsp:include page="./printReceiptTemplatePage.jsp" flush="true" />

</logic:present>

</html:html>