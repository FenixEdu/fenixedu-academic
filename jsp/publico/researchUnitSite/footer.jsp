<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<div id="foot_links">
<logic:empty name="researchUnit" property="site.footerLinks">
	<bean:define id="contactsUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.link"/></bean:define>
	<a href="<%= contactsUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.label"/></a>
	|  
	<bean:define id="webmasterUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.link"/></bean:define>
	<a href="<%= webmasterUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.label"/></a>
</logic:empty>

<logic:notEmpty name="researchUnit" property="site.footerLinks">
	<fr:view name="researchUnit" property="site.footerLinks">
		<fr:layout name="flowLayout">
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="showFooterLink"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
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