<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>

<logic:present name='<%= InquiriesUtil.INQUIRY_MESSAGE_KEY %>'>
	<bean:define id="messageKey" name='<%= InquiriesUtil.INQUIRY_MESSAGE_KEY %>' />
	<h2>
		<bean:message key='<%= "" + messageKey %>' bundle="INQUIRIES_RESOURCES"/>
	</h2>
</logic:present>