<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="java.lang.StringBuilder" %>

<noscript>
	<span class="error"><!-- Error messages go here --><bean:message key="error.message.inquiries.javascript.disabled" bundle="INQUIRIES_RESOURCES"/></span>
</noscript>

<bean:message key="message.inquiries.introduction" bundle="INQUIRIES_RESOURCES"/>

<br/>

<logic:present name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
	<logic:notEmpty name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
		<strong><bean:message key="title.inquiries.choose.course" bundle="INQUIRIES_RESOURCES"/></strong>
		<ul>
			<logic:iterate id="attends" name='<%= InquiriesUtil.STUDENT_ATTENDS %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta">
				<li>
					<html:link page='<%= new StringBuilder().append("/fillInquiries.do?method=prepare&amp;").append(InquiriesUtil.STUDENT_ATTENDS_ID).append("=").append(attends.getIdInternal().toString()).toString() %>'>
						<bean:write name="attends" property="disciplinaExecucao.nome" />
					</html:link>
				</li>
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
				<bean:define id="executionCourse" name="evaluatedAttends" property="disciplinaExecucao.nome"/>
				<logic:equal name="executionCourse" property="availableForInquiries" value="true">
					<li>
						<bean:write name="executionCourse" property="nome" />
					</li>
				</logic:equal>
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


