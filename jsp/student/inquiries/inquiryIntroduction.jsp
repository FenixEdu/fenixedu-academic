<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="java.lang.StringBuilder" %>

<noscript>
	<span class="error"><!-- Error messages go here --><bean:message key="error.message.inquiries.javascript.disabled" bundle="INQUIRIES_RESOURCES"/></span>
</noscript>

<h2>
	<bean:message key="message.inquiries.title" bundle="INQUIRIES_RESOURCES"/>	
</h2>

<logic:present name="inquiryMessageKey">
	<span class="error"><bean:message name="inquiryMessageKey" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>

<logic:present name="executionPeriod">
	<logic:present name="executionPeriod" property="inquiryResponsePeriod">
		<div class="infoop2 mtop1">
			<bean:write name="executionPeriod" property="inquiryResponsePeriod.introduction" filter="false"/>
		</div>
		<br/>
		<logic:present name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
			<logic:notEmpty name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
				<strong><bean:message key="title.inquiries.choose.course" bundle="INQUIRIES_RESOURCES"/></strong>
				<ul>
					<logic:iterate id="attends" name='<%= InquiriesUtil.STUDENT_ATTENDS %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta">
						<logic:equal name="attends" property="disciplinaExecucao.availableForInquiries" value="true">
							<li>
								<html:link page='<%= new StringBuilder().append("/fillInquiries.do?method=prepare&amp;").append(InquiriesUtil.STUDENT_ATTENDS_ID).append("=").append(attends.getIdInternal().toString()).toString() %>'>
									<bean:write name="attends" property="disciplinaExecucao.nome" />
								</html:link>
							</li>
						</logic:equal>
					</logic:iterate>
				</ul>
			</logic:notEmpty>
		</logic:present>
		<logic:present name='<%= InquiriesUtil.EVALUATED_STUDENT_ATTENDS %>'>
			<logic:notEmpty name='<%= InquiriesUtil.EVALUATED_STUDENT_ATTENDS %>'>
				<p><strong>
					<bean:message key="title.inquiries.evaluated.courses" bundle="INQUIRIES_RESOURCES"/>
				</strong></p>
				<ul>
					<logic:iterate id="evaluatedAttends" name='<%= InquiriesUtil.EVALUATED_STUDENT_ATTENDS %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta">
						<bean:define id="executionCourse" name="evaluatedAttends" property="disciplinaExecucao"/>
						<li>
							<bean:write name="executionCourse" property="nome" />
						</li>
					</logic:iterate>
				</ul>
			</logic:notEmpty>
		</logic:present>
		<logic:notPresent name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
			<logic:present name='<%= InquiriesUtil.INQUIRY_MESSAGE_KEY %>'>
				<bean:define id="messageKey" name='<%= InquiriesUtil.INQUIRY_MESSAGE_KEY %>' />
				<p><strong class="attention"><bean:message key='<%= "" + messageKey %>' bundle="INQUIRIES_RESOURCES"/></strong></p>
			</logic:present>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="executionPeriod" property="inquiryResponsePeriod">
		<bean:message key="message.inquiries.period.not.defined" bundle="INQUIRIES_RESOURCES"/>
	</logic:notPresent>
</logic:present>
<br/>


