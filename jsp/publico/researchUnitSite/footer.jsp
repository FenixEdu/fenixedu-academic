<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<div id="foot_links">
	<bean:define id="contactsUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.link"/></bean:define>
	<a href="<%= contactsUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.label"/></a>
	|  
	<bean:define id="webmasterUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.link"/></bean:define>
	<a href="<%= webmasterUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.label"/></a>
</div>

<div id="foot_copy">
	<bean:message bundle="GLOBAL_RESOURCES" key="footer.copyright.label"/>
	<dt:format pattern="yyyy"><dt:currentTime/></dt:format>
	-
	<logic:present name="site">
		<bean:write name="site" property="unit.name"/>
	</logic:present>
	<logic:notPresent name="site">
		<bean:message bundle="GLOBAL_RESOURCES" key="institution.name"/>
	</logic:notPresent>
</div>

<div class="clear"></div>