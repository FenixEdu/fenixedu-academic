<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li><html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>'><bean:message key="link.student.portal.home"/></html:link></li>
	<li class="navheader"><bean:message key="administrative.office.services"/></li>
<%-- 	
	<li><html:link page="/documentRequest.do?method=chooseRegistration" titleKey="documents.requirement.link.title"><bean:message key="documents.requirement"/></html:link></li>
<%-- 	
	<li><html:link page="/documentRequest.do?method=viewDocumentRequests" titleKey="documents.requirement.consult.link.title"><bean:message key="documents.requirement.consult"/></html:link></li>
	<li><html:link page="/payments.do?method=showEvents" titleKey="link.title.payments"><bean:message key="link.title.payments"/></html:link></li>
</ul>
