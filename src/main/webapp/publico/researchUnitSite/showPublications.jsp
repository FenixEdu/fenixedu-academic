<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="researchUnit" property="nameWithAcronym" /></h1>

<h2 class="mtop15"><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES" /></h2>

<bean:define id="sotisURL">
    <%= net.sourceforge.fenixedu.FenixIstConfiguration.getConfiguration().sotisURL() %>
</bean:define>

<bean:define id="lang">
    <%= org.fenixedu.commons.i18n.I18N.getLocale().toLanguageTag() %>
</bean:define>

<bean:define id="researchers" name="researchers" />

<script src="<%= sotisURL %>/js/sotis-embedded.js" data-sotis-use="list" data-sotis-users="<%= researchers %>" data-sotis-links="yes" data-sotis-lang="<%= lang %>"></script>