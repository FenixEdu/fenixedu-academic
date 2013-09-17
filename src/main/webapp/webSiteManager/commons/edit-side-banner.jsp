<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.UnitSite"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2><bean:message key="title.site.sideBanner" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.sideBanner" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.site.sideBanner.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<fr:hasMessages for="editSideBanner" type="validation">
    <div class="mbottom15">
        <span class="error0">
            <fr:message for="editSideBanner" type="validation"/>
        </span>
    </div>
</fr:hasMessages>

<bean:define id="oid" name="site" property="externalId"/>
<fr:form action="<%= String.format("%s?method=sideBanner&amp;%s", actionName, context) %>">
    <fr:edit id="editSideBanner" name="site" slot="sideBanner">
        <fr:layout name="rich-text">
            <fr:property name="rows" value="20"/>
            <fr:property name="columns" value="70"/>
            <fr:property name="config" value="advanced"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit styleClass="mtop15">
        <bean:message key="button.submit"/>
    </html:submit>
</fr:form>
