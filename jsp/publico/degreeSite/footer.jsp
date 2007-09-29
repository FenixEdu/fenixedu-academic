<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<div id="foot_links">
<bean:define id="institutionUrl"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<a href="<%= institutionUrl %>/html/instituto/index.shtml#cont"><bean:message key="link.contacts" bundle="GLOBAL_RESOURCES"/></a>  |  
<bean:define id="webmasterUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.link"/></bean:define>
<a href="<%= webmasterUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.label"/></a>
</div>
<div id="foot_copy">&copy;<dt:format pattern="yyyy"><dt:currentTime/></dt:format>, <bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/>. <bean:message key="footer.copyright.alrightsreserved" bundle="GLOBAL_RESOURCES"/>.</div>
<div class="clear"></div>
