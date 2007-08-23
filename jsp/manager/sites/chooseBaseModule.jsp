<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.choose.baseModule" bundle="SITE_RESOURCES"/>
</h2>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<bean:define id="url" value="<%= actionName + "?method=sections&amp;" + context %>" type="java.lang.String"/>

<fr:form action="<%= url %>">
    <fr:hasMessages type="validation" for="functionality">
        <p>
            <span class="error0">
                <bean:message key="message.create.section.functionality.required" bundle="SITE_RESOURCES"/>
            </span>
        </p>
    </fr:hasMessages>

    <fr:edit id="functionality" name="site" schema="siteTemplate.choose.module">
        <fr:layout name="flow">
            <fr:property name="labelExcluded" value="true"/>
            <fr:property name="hideValidators" value="true"/>
        </fr:layout>
        <fr:destination name="module.view" path="/module/view.do?module=${idInternal}" module="/manager/functionalities"/>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.submit" bundle="SITE_RESOURCES"/>
    </html:submit>
    <html:cancel>
        <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
    </html:cancel>
</fr:form>
