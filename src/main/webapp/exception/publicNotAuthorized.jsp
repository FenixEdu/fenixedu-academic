<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@page import="org.joda.time.YearMonthDay"%>

<html:xhtml/>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/>Error Page</title>
    <link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
					<%  
						String logo = "/images/logo-fenix.gif"; 	
						if (new YearMonthDay().getMonthOfYear() == 12) {
							logo = "/images/logo-fenix-xmas.gif";    
						}
					%>
					<img src="<%= request.getContextPath() + logo%>" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" />
				</center>
			</td>
			<td>
				<span class="error"><!-- Error messages go here --><bean:message key="message.error.notAuthorizedContents" /></span>
			</td>
		</tr>
	</table>
</div>
</body>
</html>