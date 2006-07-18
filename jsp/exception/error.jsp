<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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
				<center>
				    <bean:message key="message.error.help"/>
				    <html:form action="/exceptionHandlingAction.do?method=sendEmail">
				    <br />
				    <table align="center" >
				    	<tr><td>
				    	<bean:message key="property.email"/></td><td>
				    	<html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value=""/> </td>
				    	</tr>
				    	<tr><td>
				    	<bean:message key="property.subject"/></td><td>
				   		<html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" value=""/></td>
				   		</tr>
				    	<tr><td>
				   		<bean:message key="property.message"/></td><td>
				    	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.body" property="body" value=""/></td>
				    	</tr>	
				      </table>	
				      <center>
				    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
				   		 <bean:message key="label.submit"/>
				    	</html:submit>
				   	  </center>
				    </html:form>
				</center>
			</td>
		</tr>
	</table>
</div>
     <%-- Invalidate session. 
     	  This is to work with FenixActionServlet --%>
     <%-- 	try{
     			session.invalidate();
     		}catch (Exception e){}
     --%>
  </body>
</html>