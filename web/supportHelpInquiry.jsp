<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="org.joda.time.YearMonthDay"%>

<html:xhtml/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   	<title>
   		<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - Support Page
	</title>
   	<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>

	<div id="container" style="width: 800px;">

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
					<logic:present name="requestBean">
						<fr:form id="supportForm" action="/exceptionHandlingAction.do?method=processSupportRequest">
					
							<fr:edit id="requestBean" name="requestBean" visible="false" />
							<html:hidden property="userAgent" value="<%= request.getHeader("User-Agent") %>" />
							
							<logic:present name="exceptionInfo">
								<h1 style="color: red; padding: 0;">
									<bean:message key="error.Exception" />
								</h1>
								<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>
								<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
								<p>
									<bean:message key="message.error.help"/>
								</p>
								<br/>
								<fr:edit id="view_state_id" name="requestBean" schema="support.error.form" >
									<fr:layout name="tabular-break">
										<fr:property name="classes" value="thleft thlight"/>
										<fr:property name="columnClasses" value=",tderror1"/>
										<fr:property name="labelTerminator" value=""/>
									</fr:layout>
									<fr:destination name="invalid" path="/exceptionHandlingAction.do?method=supportHelpException"/>
								</fr:edit>
							</logic:present>
							
							<logic:notPresent name="exceptionInfo">
								<h1 style="color: red; padding: 0;">
									<bean:message key="support.Contact" />
								</h1>
								<p>
									<bean:message key="message.support.help"/>
								</p>
								<br/>
								<fr:edit id="view_state_id" name="requestBean" schema="support.request.form" >
									<fr:layout name="tabular-break">
										<fr:property name="classes" value="thleft thlight"/>
										<fr:property name="columnClasses" value=",tderror1"/>
										<fr:property name="labelTerminator" value=""/>
									</fr:layout>
									<fr:destination name="invalid" path="/exceptionHandlingAction.do?method=supportHelpError"/>
								</fr:edit>
							</logic:notPresent>
							<html:submit>
								<bean:message key="label.submit" bundle="APPLICATION_RESOURCES" />
							</html:submit>
					
						</fr:form>
					</logic:present>

					<%-- HACK due to action exceptions not passing through exception handlers --%>
					<logic:notPresent name="requestBean">
						<h1 style="color: red; padding: 0;">
							<bean:message key="error.Exception" />
						</h1>
						<html:form action="/exceptionHandlingAction.do?method=processException">
						
							<html:hidden property="userAgent" value="<%= request.getHeader("User-Agent") %>" />

							<p>
								<bean:message key="message.error.help"/>
							</p>
							<logic:present name="exceptionInfo">
								<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>
								<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
							</logic:present>
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

						</html:form>
					</logic:notPresent>
				</td>
				</tr>
		</table>
	</div>

</body>
</html>
