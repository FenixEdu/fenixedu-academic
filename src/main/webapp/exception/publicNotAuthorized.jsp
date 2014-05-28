<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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