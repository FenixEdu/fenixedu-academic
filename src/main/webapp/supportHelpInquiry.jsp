<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="pt.ist.fenixWebFramework.security.User"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%>
<%@page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean"%>
<html:html xhtml="true">

<%
	try {
		final HttpSession httpSession = request.getSession(false);
		if (httpSession != null) {
		    final User user = (User) httpSession.getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
	    	if (user != null) {
				UserView.setUser(user);
	    	}
		}
%>

<head>
	<title>
		<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/><bean:message key="label.support.page" bundle="GLOBAL_RESOURCES"/>
	</title>
   	<link href="<%= request.getContextPath() %>/CSS/logdotist_new.css" rel="stylesheet" type="text/css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>

<body>

<table id="wrapper">
<tr>
<td>

<div id="container">

	<div id="dotist_id">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
	</div>

	<div id="txt">

		<logic:present name="exceptionInfo">
			<h1><bean:message key="error.Contact" bundle="APPLICATION_RESOURCES" /></h1>
			<p><bean:message key="error.contact.welcome" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.allFieldsRequired" bundle="APPLICATION_RESOURCES" /></p>
		</logic:present>

		<logic:notPresent name="exceptionInfo">
			<h1><bean:message key="support.Contact" bundle="APPLICATION_RESOURCES" /></h1>
			<p><bean:message key="support.contact.welcome" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.allFieldsRequired" bundle="APPLICATION_RESOURCES" /></p>
		</logic:notPresent>
		
		<div id="alert">
			<p><span class="error"></span></p>
		</div>
	</div>

	<div class="form">

		<fr:form id="supportForm" action="/exceptionHandlingAction.do">
			<html:hidden property="method" value="processSupportRequest"/>

			<html:hidden property="userAgent" value="<%= request.getHeader("User-Agent") %>" />
	
			<bean:define id="schema" value="support.request.form" />
			<logic:present name="requestBean">
			<logic:notEmpty name="requestBean">
				<fr:edit id="requestBean" name="requestBean" visible="false" />

				<logic:notPresent name="exceptionInfo">
					<fr:edit id="view_state_id" name="requestBean" schema="<%= schema %>" >
						<fr:layout name="tabular">
							<fr:property name="classes" value="tform"/>
							<fr:property name="columnClasses" value=",,tderror1"/>
							<fr:property name="rowClasses" value="inputtext,select,select,inputtext inputw400,textarea textareaw400 textareah100,inobullet,,,"/>
							<fr:property name="labelTerminator" value=""/>
						</fr:layout>
						<fr:destination name="invalid" path="/exceptionHandlingAction.do?method=supportFormFieldValidation" />
					</fr:edit>
				</logic:notPresent>

				<logic:present name="exceptionInfo">
					<bean:define id="exceptionInfo" name="exceptionInfo" type="java.lang.String"/>

					<html:hidden property="exceptionInfo" value="<%= exceptionInfo %>"/>
				
					<bean:define id="schema" value="support.error.form" />
					<logic:empty name="requestBean" property="responseEmail">
						<bean:define id="schema" value="support.error.form.unknown.user"/>
					</logic:empty>

					<fr:edit id="view_state_id" name="requestBean" schema="<%= schema %>" >
						<fr:layout name="tabular">
							<fr:property name="classes" value="tform"/>
							<fr:property name="columnClasses" value=",,tderror1"/>
							<fr:property name="rowClasses" value="inputtext,inputtext inputw400,textarea textareaw400 textareah100,,,,"/>
							<fr:property name="labelTerminator" value=""/>
						</fr:layout>
						<fr:destination name="invalid" path="/exceptionHandlingAction.do?method=supportFormFieldValidation" />
					</fr:edit>
				</logic:present>

				<p>
					<html:submit>
						<bean:message key="label.submit.support.form" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</p>
			</logic:notEmpty>
			</logic:present>
	
		</fr:form>

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

<%
	} finally {
	    UserView.setUser(null);
	}
%>

</html:html>
