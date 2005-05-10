<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>

<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" type="text/css" />

<noscript>
	<font class="error">
		<bean:message key="error.message.inquiries.javascript.disabled" bundle="INQUIRIES_RESOURCES"/>
	</font>
</noscript>


<logic:present name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
	<logic:notEmpty name='<%= InquiriesUtil.STUDENT_ATTENDS %>'>
		<h2>
			Escolher disciplina para avaliar:
		</h2>
		<logic:iterate id="attends" name='<%= InquiriesUtil.STUDENT_ATTENDS %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta">
			<html:link page='<%= "/fillInquiries.do?method=prepare&amp;" + InquiriesUtil.STUDENT_ATTENDS_ID + "=" + attends.getIdInternal() %>'>
				<bean:write name="attends" property="disciplinaExecucao.nome" />
			</html:link>
			<br/>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

<logic:present name='<%= InquiriesUtil.EVALUATED_STUDENT_ATTENDS %>'>
	<logic:notEmpty name='<%= InquiriesUtil.EVALUATED_STUDENT_ATTENDS %>'>
		<h2>
			Disciplinas j&aacute; avaliadas:
		</h2>
		<logic:iterate id="evaluatedAttends" name='<%= InquiriesUtil.EVALUATED_STUDENT_ATTENDS %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta">
				<bean:write name="evaluatedAttends" property="disciplinaExecucao.nome" />
			<br/>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>


