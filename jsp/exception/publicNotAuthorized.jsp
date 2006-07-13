<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - Error Page</title>
    <link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  </head>
<body>
<div id="container">
	<center>
		<h1><font color="red"><html:errors/></font></h1>
	</center>
	<br />
	<table>
		<tr>
			<td>
				<center>
					<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" />
				</center>
			</td>
			<td>
				<span class="error"><bean:message key="message.error.notAuthorizedContents" /></span>
			</td>
		</tr>
	</table>
</div>
</body>
</html>