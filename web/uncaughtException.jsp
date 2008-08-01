<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%>
<%@page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
<html:html xhtml="true">

<head>
<title>
	<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <bean:message key="label.support.page" bundle="GLOBAL_RESOURCES"/>
	</title>
   	<link href="<%= request.getContextPath() %>/CSS/logdotist_new.css" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>

<style>
form {
padding: 0 20px;
}
</style>


<table id="wrapper">
<tr>
<td>

<div id="container">

	<div id="dotist_id">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
	</div>

	<div id="txt">
		<h1><bean:message key="error.Contact" bundle="APPLICATION_RESOURCES" /></h1>
		<p><bean:message key="error.contact.welcome" bundle="APPLICATION_RESOURCES" /></p>
		<div id="alert">
			<p><span class="error"></span></p>
		</div>
	</div>

	<div class="form">

		<logic:present name="requestBean">
			<fr:form id="supportForm" action="/exceptionHandlingAction.do?method=processSupportRequest" >
	
				<fr:edit id="requestBean" name="requestBean" visible="false" />
				<html:hidden property="userAgent" value="<%= request.getHeader("User-Agent") %>" />
		
				<bean:define id="schema" value="support.request.form" />
				
				<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>
				<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
				<bean:define id="schema" value="support.error.form" />

				<fr:edit id="view_state_id" name="requestBean" schema="<%= schema %>" >
					<fr:layout name="tabular">
						<fr:property name="classes" value="tform"/>
						<fr:property name="columnClasses" value=",,tderror1"/>
						<fr:property name="rowClasses" value="inputtext,inputtext inputw400,textarea textareaw400 textareah100,,,,"/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
					<fr:destination name="invalid" path="/exceptionHandlingAction.do?method=supportExceptionFieldValidation"/>
				</fr:edit>
		
				<p>
					<html:submit>
						<bean:message key="label.submit.support.form" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</p>
			</fr:form>
		</logic:present>

		<logic:notPresent name="requestBean">

			<html:form action="/exceptionHandlingAction.do?method=processExceptionLegacy">
			
				<html:hidden property="userAgent" value="<%= request.getHeader("User-Agent") %>" />
	
				<logic:present name="exceptionInfo">
					<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>
					<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
				</logic:present>
				
				<table class="tform">
					<tr class="inputtext">
						<th>
						    <bean:message key="label.support.form.responseEmail" bundle="APPLICATION_RESOURCES" />
						</th>
						<td>
							<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
								<bean:define id="exceptionInfoUserMail" name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" scope="session" property="person.institutionalOrDefaultEmailAddress.value" /> 
								<html:text readonly="true" size="30" bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value="<%= exceptionInfoUserMail.toString() %>" />
							</logic:present>
							<logic:notPresent name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person">
								<html:text size="30" bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value=""/>
							</logic:notPresent>
						</td>
					</tr>
					<tr class="inputtext inputw400">
						<td>
						    <bean:message key="label.support.form.subject" bundle="APPLICATION_RESOURCES" />
				    	</td>
				    	<td>
					   		<html:text size="30" bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" value=""/>
				   		</td>
			   		</tr>
			    	<tr class="textarea textareaw400 textareah100">
			    		<th>
					   		<bean:message key="label.support.form.message" bundle="APPLICATION_RESOURCES" />
				   		</th>
				   		<td>
				   			<p class="smalltxt color555">
				   				<bean:message key="support.form.message.exception" bundle="APPLICATION_RESOURCES" />
				   			</p>
					    	<html:textarea cols="36" rows="5" bundle="HTMLALT_RESOURCES" property="body" value=""/>
				    	</td>
			   		</tr>
				</table>
				<p>
			    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
						<bean:message key="label.submit.support.form" bundle="APPLICATION_RESOURCES" />
			    	</html:submit>
		    	</p>

			</html:form>
		</logic:notPresent>

	</div> <!-- form -->
	
	<script type="text/javascript">
	var focusControl = document.forms["authenticationForm"].elements["username"];
	if (focusControl.type != "hidden" && !focusControl.disabled) { focusControl.focus(); }
	</script>

</div> <!-- container -->

</td>
</tr>
</table>



</body>
</html:html>