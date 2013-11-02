<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page language="java" %>
<%@ page import="org.joda.time.LocalDate" %>
<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.inquiries.firstTimeCycle.title" bundle="INQUIRIES_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div id="container">
			<% 
				LocalDate now = new LocalDate();
				LocalDate limitDate = new LocalDate(2013, 12, 1);
			%>
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="message.inquiries.firstTimeCycle.title" bundle="INQUIRIES_RESOURCES"/></h1>
				<div class="mtop1">
					<p><bean:message key="message.inquiries.firstTimeCycle.body" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="INQUIRIES_RESOURCES"/></p>
					<p><span style="background-color: #fbf8cc; color: #805500;" ><bean:message key="message.inquiries.firstTimeCycle.warningDate" arg0="<%= limitDate.toString() %>" bundle="INQUIRIES_RESOURCES"/></span></p>
				</div>
			</div>
			<div align="center">
				<table>
					<tr>
						<td>
							<form method="post" action="<%= request.getContextPath() %>/respondToFirstTimeCycleInquiry.do">
								<html:hidden property="method" value="respondNow"/>
								<html:hidden property="<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>" value="/estudante/estudante"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.now" property="ok">
									<bean:message key="button.inquiries.respond.now" />
								</html:submit>
							</form>
						</td>
						<% 
							if(now.isBefore(limitDate)) {
						%>
								<td>
									<form method="post" action="<%= request.getContextPath() %>/respondToFirstTimeCycleInquiry.do">
										<html:hidden property="method" value="registerStudentResponseRespondLater"/>
										<html:hidden property="<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>" value="/comunicacao/comunicacao"/>
										<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
											<bean:message key="button.inquiries.respond.later" />
										</html:submit>
									</form>
								</td>
						<% } %>
					</tr>
				</table>
			</div>

		</div>
	</body>
</html:html>
