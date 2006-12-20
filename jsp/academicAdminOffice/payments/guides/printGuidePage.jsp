<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
    <head>

		<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_calendars.css" rel="stylesheet" media="print" type="text/css" />


    	<title><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.guide"/></title>
    </head>

    <body>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<%-- The Original Payment-Guide --%>
	<jsp:include page="./printGuideTemplatePage.jsp" flush="true" />
	<div style="margin: 500px 0px 0px 0px">
	<%-- Copy of Payment-Guide --%>
	<jsp:include page="./printGuideTemplatePage.jsp" flush="true" />
	</div>
</logic:present>

    </body>
</html>


	