<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/></h1>
				<div class="mtop1">
					<bean:write name="executionPeriod" property="delegateInquiryTemplate.inquiryMessage" filter="false"/>
				</div>
			</div>
			
			<div align="center">
				<table>
					<tr>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToYearDelegateInquiriesQuestion.do">
								<html:hidden property="method" value="respondNow"/>
								<html:hidden property="<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>" value="/delegado/delegado"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.now" property="ok">
									<bean:message key="button.inquiries.respond.now" />
								</html:submit>
							</form>
						</td>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToYearDelegateInquiriesQuestion.do">
								<html:hidden property="method" value="respondLater"/>
								<html:hidden property="<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>" value="/comunicacao/comunicacao"/>
									<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
										<bean:message key="button.inquiries.respond.later" />
									</html:submit>
							</form>
						</td>
					</tr>
				</table>				
			</div>
		</div>
	</body>
</html:html>
