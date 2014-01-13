<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml/>

<em><bean:message key="label.researchPortal" bundle="RESEARCHER_RESOURCES"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.search"/></h2>

<div class="infoop2">
	<bean:message key="label.search.description" bundle="RESEARCHER_RESOURCES"/> 
</div>

<bean:define id="sotisURL">
	<%= net.sourceforge.fenixedu.util.FenixConfigurationManager.getConfiguration().sotisURL() %>
</bean:define>

<bean:define id="lang">
	<%= org.fenixedu.commons.i18n.I18N.getLocale().toLanguageTag() %>
</bean:define>

<script src="<%= sotisURL %>/js/sotis-embedded.js" data-sotis-use="search" data-sotis-links="yes" data-sotis-lang="<%= lang %>"></script>