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
<%@ page isELIgnored="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<%=org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getApplicationTitle().getContent() %>"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:write name="cerimonyInquiryPerson" property="cerimonyInquiry.description"/></h1>
				<div class="mtop1">
					<bean:write name="cerimonyInquiryPerson" property="cerimonyInquiry.text" filter="false"/>
				</div>
			</div>

<style>
.forminline form { display: inline !important; }
.dinline { display: inline; }
.thlight th { font-weight: normal; }
</style>


			<div class="dinline forminline" align="center">
				<fr:form action="/respondToAlumniInquiriesQuestion.do">
					<html:hidden property="method" value="registerAlumniResponseNow"/>
					<fr:edit id="cerimonyInquiryPerson" name="cerimonyInquiryPerson">
						<fr:schema bundle="APPLICATION_RESOURCES" type="org.fenixedu.academic.domain.alumni.CerimonyInquiryPerson">
							<fr:slot name="cerimonyInquiryAnswer" key="label.response" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.CerimonyInquiryAnswerProvider"/>
								<fr:property name="format" value="${text}" />
							</fr:slot>
							<logic:equal name="cerimonyInquiryPerson" property="cerimonyInquiry.allowComments" value="true">
								<fr:slot name="comment" key="label.observations"/>
							</logic:equal>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="thlight"/>
							<fr:property name="columnClasses" value=",,"/>
						</fr:layout>
					</fr:edit>
					<fr:destination name="cancel" path="/respondToAlumniInquiriesQuestion.do?method=registerAlumniResponseRespondLater"/>
					<br/>
					<span style="padding-left: 100px;">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.now" property="ok">
							<bean:message key="button.inquiries.respond.now" />
						</html:submit>
					</span>
				</fr:form>


				<form method="post" action="<%= request.getContextPath() %>/respondToAlumniInquiriesQuestion.do">
					<html:hidden property="method" value="registerAlumniResponseRespondLater"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="inquiries.respond.later" property="ok">
						<bean:message key="button.inquiries.respond.later" />
					</html:submit>
				</form>
			</div>




		</div>
	</body>
</html:html>
