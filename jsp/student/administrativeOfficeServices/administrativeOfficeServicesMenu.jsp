<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->

<ul>
	<li><html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>'><bean:message key="link.student.portal.home"/></html:link></li>

	<li class="navheader"><bean:message key="administrative.office.services"/></li>
	<li><html:link page="/documentRequest.do?method=prepare" titleKey="documents.requirement.link.title"><bean:message key="documents.requirement"/></html:link></li>
	<li><html:link page="/administrativeOfficeServicesSection.do" titleKey="documents.requirement.consult.link.title"><bean:message key="documents.requirement.consult"/></html:link></li>
</ul>
