<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html xhtml="true" locale="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/></h1>
				<div class="infoop2 mtop1">
					<bean:write name="executionPeriod" property="inquiryResponsePeriod.introduction" filter="false"/>
				</div>
			</div>
			<br />

			<div align="center">
				<table>
					<tr>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToInquiriesQuestion.do">
								<html:hidden property="method" value="respondNow"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.now" property="ok">
									<bean:message key="button.inquiries.respond.now" />
								</html:submit>
							</form>
						</td>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToInquiriesQuestion.do">
								<html:hidden property="method" value="registerStudentResponseRespondLater"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
									<bean:message key="button.inquiries.respond.later" />
								</html:submit>
							</form>
						</td>
<!--
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToInquiriesQuestion.do">
								<html:hidden property="method" value="registerStudentResponseDontWantToRespond"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.dont.respond" property="ok">
									<bean:message key="button.inquiries.respond.dont.respond" />
								</html:submit>
							</form>
						</td>
-->
					</tr>
				</table>
			</div>

			<br />
			<div>
				<center>
					<p>
						<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES" />:
						<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES" />
					</p>
				</center>
			</div>
		</div>
	</body>
</html:html>
