<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
					<bean:write name="inquiryTemplate" property="inquiryMessage" filter="false"/>
				</div>
				

				<div class="infoop2">
					<p style="margin: 0.5em 0 0.75em 0;"><strong><bean:message key="label.teachingInquiries.coursesToAnswer" bundle="INQUIRIES_RESOURCES"/>:</strong></p>
	
					<logic:iterate id="executionCourse" name="executionCourses">
						<p style="margin: 0 0 0.5em 0;">
							<bean:write name="executionCourse" property="executionPeriod.semester" />
							<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
							<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />				
							<html:link page="<%="/teacher/teachingInquiry.do?method=showInquiriesPrePage&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/teacher")%>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
								<strong>
									<bean:write name="executionCourse" property="nome"/>
									(<logic:iterate id="executionDegree" name="executionCourse" property="executionDegrees" indexId="iter">
										<logic:notEqual name="iter" value="0">
										,
										</logic:notEqual>
										<bean:write name="executionDegree" property="degree.sigla"/>
									</logic:iterate>)
									»
								</strong>
							</html:link>
						</p>
					</logic:iterate>
				</div>

				<form method="post" action="<%= request.getContextPath() %>/respondToTeachingInquiriesQuestion.do">
					<html:hidden property="method" value="respondLater"/>
					<html:hidden property="<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>" value="/comunicacao/comunicacao"/>
					<p style="margin-top: 2.5em; text-align: center;">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
							<bean:message key="button.inquiries.respond.later" />
						</html:submit>
					</p>
				</form>
			</div>
		</div>
	</body>
</html:html>
