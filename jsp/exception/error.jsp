<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>.IST - Error Page</title>
    <link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  </head>
<body>
<div id="darkcontainer">
	<center>
		<h1><font color="red"><html:errors/></font></h1>
	</center>
	<br />
	<table bgcolor="#000" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#000">
				<center>
					<img alt="Logo dotist" src="<%= request.getContextPath() %>/images/fenix_euro_onblack.gif"/>
				</center>
			</td>
			<td bgcolor="#000">
				<center>
					<font color="white">
					    <bean:message key="message.error.help"/>
					    <html:form action="/exceptionHandlingAction.do?method=sendEmail">
				    <font/>
				    <br />
				    <table align="center"bgcolor="#000" cellpadding="0" cellspacing="0">
				    	<tr><td bgcolor="#000"><font color="white">
						    	<bean:message key="property.email"/>
						    <font/></td>
					    	<td bgcolor="#000">
					    		<html:text property="email" value=""/>
					    	</td>
				    	</tr>
				    	<tr><td bgcolor="#000"><font color="white">
						    	<bean:message key="property.subject"/>
						    <font/></td>
						    <td bgcolor="#000">
					   			<html:text property="subject" value=""/>
					   		</td>
				   		</tr>
				    	<tr><td bgcolor="#000"><font color="white">
						   		<bean:message key="property.message"/>
						   	<font/></td>
						   	<td bgcolor="#000">
						    	<html:textarea property="body" value=""/>
						    </td>
				    	</tr>	
				      </table>
				      <center>
				    	<html:submit >
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