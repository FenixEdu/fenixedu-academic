<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="org.joda.time.YearMonthDay"%>

<html:html xhtml="true">

<head>
<title>
	<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <bean:message key="label.support.page" bundle="GLOBAL_RESOURCES"/>
</title>
<link href="<%= request.getContextPath() %>/CSS/logdotist_new.css" rel="stylesheet" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>

<table id="wrapper">
	<tr>
		<td>

			<div id="container">
				<table class="feedback">
					<tr>
						<td>
							<%  
								String logo = "";
								if (new YearMonthDay().getMonthOfYear() != 12) {
								    logo = "/images/logo-fenix-small.gif";
								} else {
									logo = "/images/logo-fenix-xmas-small.gif";    
								}
							%>
							<img src="<%= request.getContextPath() + logo%>" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />"/>
						</td>
						<td>
						
						<div id="txt">
							<h1><bean:message key="message.error.submitted" bundle="APPLICATION_RESOURCES" /></h1>
							<br/>
							<p><bean:message key="support.mail.submitted" bundle="APPLICATION_RESOURCES" /></p>
						</div>
						</td>
					</tr>
				</table>
			</div>

		</td>
	</tr>
</table>

</body>
</html:html>
