<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<div id="foot_links">
	<bean:define id="contactsUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.link"/></bean:define>
	<a href="<%= contactsUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.label"/></a>
	| 
	<bean:define id="webmasterUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.link"/></bean:define>
	<a href="<%= webmasterUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.label"/></a>
</div>

<div id="foot_copy">&copy;<dt:format pattern="yyyy"><dt:currentTime/></dt:format>, <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>. <bean:message key="footer.copyright.alrightsreserved" bundle="GLOBAL_RESOURCES"/>.</div>
<div class="clear"></div>
