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
   	<title>
   		<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> Error Page
	</title>
   	<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<body>
	
	<div id="container" style="width: 650px;">

		<html:form action="/exceptionHandlingAction.do?method=sendEmail">
			<logic:present name="exceptionInfo">
				<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>
				<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
			</logic:present>

		<table>
			<tr>
				<%  
					String logo = "/images/logo-fenix.gif"; 	
					if (new YearMonthDay().getMonthOfYear() == 12) {
						logo = "/images/logo-fenix-xmas.gif";    
					}
				%>
				<td>
					<img src="<%= request.getContextPath() + logo %>" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" />
				</td>

				<td>
					<br/>
					<h1 style="color: red; padding: 0;"><html:errors/></h1>
					<p><bean:message key="message.error.help"/></p>
					<br/>
	
				<table align="center" >
					<tr>
						<td>
						    <bean:message key="label.yourEmail"/>:
						</td>
						<td>
							<logic:present name="loggedPersonEmail">
								<bean:define id="loggedPersonEmail" name="loggedPersonEmail" type="java.lang.String"/>
								<html:text size="30" bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value="<%= loggedPersonEmail %>"/>
							</logic:present>
							<logic:notPresent name="loggedPersonEmail">
								<html:text size="30" bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value=""/>
							</logic:notPresent>
							 (<bean:message key="message.optional"/>)
						</td>
					</tr>
					<tr>
						<td>
						    <bean:message key="property.subject"/>
				    	</td>
				    	<td>
					   		<html:text size="30" bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" value=""/>
							 (<bean:message key="message.optional"/>)
				   		</td>
			   		</tr>
			    	<tr>
			    		<td>
					   		<bean:message key="property.message"/>
				   		</td>
				   		<td>
					    	<html:textarea cols="36" rows="5" bundle="HTMLALT_RESOURCES" property="body" value=""/>
				    	</td>
			   		</tr>
			    	<tr>
			    		<td></td>
						<td>
					    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
								<bean:message key="label.send"/>
					    	</html:submit>
						</td>
					</tr>
				</table>
			
			</td>
			</tr>
		</table>

		</html:form>
		
	</div>
	     <%-- Invalidate session. 
	     	  This is to work with FenixActionServlet --%>
	     <%-- 	try{
	     			session.invalidate();
	     		}catch (Exception e){}
	     --%>
	</body>
</html>
