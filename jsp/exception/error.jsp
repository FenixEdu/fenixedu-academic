<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<title>
    		<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - Error Page
   		</title>
    	<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css"/>
	    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
		<html:form action="/exceptionHandlingAction.do?method=sendEmail">

			<table style="margin-top: 6.5em; padding: 0.5em;" border="0" align="center">
				<tr>
					<td rowspan="7">
						<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" />
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center; font-size: medium; font-weight: bold; color: red; padding-top: 1em;">
						<html:errors/>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: justify; font-size: small; padding-bottom: 2em;">
					    <bean:message key="message.error.help"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right; width: 2em;">
					    <bean:message key="property.email"/>
					</td>
					<td>
						<html:text size="30" bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value=""/> 
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
					    	<bean:message key="property.subject"/>
			    	</td>
			    	<td>
				   		<html:text size="64" bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" value=""/>
			   		</td>
		   		</tr>
		    	<tr>
		    		<td style="text-align: right;">
				   		<bean:message key="property.message"/>
			   		</td>
			   		<td style="width: 10em;">
				    	<html:textarea rows="10" cols="63" bundle="HTMLALT_RESOURCES" altKey="textarea.body" property="body" value=""/>
			    	</td>
		   		</tr>
		    	<tr>
					<td colspan="2" style="text-align: right; padding-top: 1em; padding-bottom: 1em; padding-right: 2em;">
				    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
							<bean:message key="label.submit"/>
				    	</html:submit>
					</td>
				</tr>
			</table>

		</html:form>
	     <%-- Invalidate session. 
	     	  This is to work with FenixActionServlet --%>
	     <%-- 	try{
	     			session.invalidate();
	     		}catch (Exception e){}
	     --%>
	</body>
</html>
