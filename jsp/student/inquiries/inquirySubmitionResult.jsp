<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>

<style type="text/css">
@import "<%= request.getContextPath() %>/CSS/inquiries_style.css";
</style>

<logic:present name='<%= InquiriesUtil.INQUIRY_MESSAGE_KEY %>'>
	<bean:define id="messageKey" name='<%= InquiriesUtil.INQUIRY_MESSAGE_KEY %>' />
	<h2>
		<bean:message key='<%= "" + messageKey %>' bundle="INQUIRIES_RESOURCES"/>
	</h2>
	<html:link href="<%= request.getContextPath() + "/student/fillInquiries.do?method=prepareCourses&amp;page=0" %>">
		<bean:message key="link.inquiries.new.course" bundle="INQUIRIES_RESOURCES"/>
	</html:link>
</logic:present>
