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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="net.sourceforge.fenixedu.domain.Installation"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean"%>
<html:html xhtml="true">


<head>
	<title>
		<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/><bean:message key="label.support.page" bundle="GLOBAL_RESOURCES"/>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style>

body {
	font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
	font-size: 14px;
	font-weight:300 !important;
	background-color: #F6F4ED;
	color: #4b565c;
}

h2 {
	color: #eee;
	font-weight: 300;
	margin: 0;
}

#container {
	margin: 80px auto 0 auto;
	width: 800px;
	background-color: #f7f7f7;
	padding: 0 0 20px 0;
	-moz-border-radius: 2px;
	-webkit-border-radius: 2px;
	border-radius: 2px;
	-moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	-webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

#content {
	width: 90%;
	margin: 0 auto;
}

#header {
	height: 60px;
	background: #16576b;
	border-bottom: 4px solid #277b97;
	padding: 30px 20px 0 20px;
}

.error {
	color: #c00;
	margin-bottom: 4px;
}

input[type="text"], input[type="password"], textarea {
	padding: 10px;
	border: 1px solid #ccc;
	-webkit-border-radius: 2px;
	width: 500px;
}

ul {
	padding-left: 0px;
}

.smalltxt {
	color: #888;
	font-size: 12px;
}
</style>

</head>

<body>


<div id="container">

		<div id="header">
				<img alt="FenixEdu" src="${pageContext.request.contextPath}/images/newImage2012/logo-fenixedu.png" />
				<h2 style="float:right"><bean:message key="support.Contact" bundle="APPLICATION_RESOURCES" /></h2>
		</div>
		<div id="content">

	<div id="txt">

		<logic:present name="exceptionInfo">
			<h1><bean:message key="error.Contact" bundle="APPLICATION_RESOURCES" /></h1>
			<p><bean:message key="error.contact.welcome" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.allFieldsRequired" bundle="APPLICATION_RESOURCES" /></p>
		</logic:present>

		<logic:notPresent name="exceptionInfo">
			<p><bean:message key="support.contact.welcome" arg0="<%=Installation.getInstance().getInstituitionURL() %>" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.allFieldsRequired" bundle="APPLICATION_RESOURCES" /></p>
		</logic:notPresent>
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
							<fr:property name="columnClasses" value=",,error"/>
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
	</div>

</div> <!-- container -->

</body>

</html:html>
