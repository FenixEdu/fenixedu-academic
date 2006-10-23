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
	
	<div id="container" style="width: 620px;">

		<html:form action="/exceptionHandlingAction.do?method=sendEmail">

		<table>
			<tr>
				<td>
					<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" />
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
					    	<html:textarea cols="30" rows="5" bundle="HTMLALT_RESOURCES" altKey="textarea.body" property="body" value=""/>
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
