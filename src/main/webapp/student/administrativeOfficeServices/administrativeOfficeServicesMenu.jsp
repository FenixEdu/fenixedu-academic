<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<ul>
	<li><html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>'><bean:message key="link.student.portal.back"/></html:link></li>
	<li class="navheader"><bean:message key="administrative.office.services"/></li>
	<li><html:link page="/documentRequest.do?method=chooseRegistration" titleKey="documents.requirement.link.title"><bean:message key="documents.requirement"/></html:link></li>
	<li><html:link page="/documentRequest.do?method=viewDocumentRequests" titleKey="documents.requirement.consult.link.title"><bean:message key="documents.requirement.consult"/></html:link></li>
	<li><html:link page="/payments.do?method=showEvents" titleKey="link.title.payments"><bean:message key="link.title.payments"/></html:link></li>
	<li><html:link page="/prices.do?method=viewPrices" titleKey="link.title.payments"><bean:message key="label.prices"/></html:link></li>
	<li><html:link page="/generatedDocuments.do?method=showAnnualIRSDocuments" titleKey="label.documents.anualIRS" bundle="STUDENT_RESOURCES"><bean:message key="label.documents.anualIRS" bundle="STUDENT_RESOURCES"/></html:link></li>
</ul>
