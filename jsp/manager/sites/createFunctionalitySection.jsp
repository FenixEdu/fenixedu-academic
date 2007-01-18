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
	<bean:message key="title.create.FunctionalitySection" bundle="SITE_RESOURCES"/>
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

<logic:notPresent name="section">
    <bean:define id="formUrl" value="<%= actionName + "?method=sections&amp;" + context %>" toScope="request"/>
</logic:notPresent>

<logic:present name="section">
    <bean:define id="sectionId" name="section" property="idInternal"/>

    <bean:define id="formUrl" value="<%= actionName + "?method=section&amp;" + context + "&amp;sectionID=" + sectionId %>" toScope="request"/>
</logic:present>

<bean:define id="url" name="formUrl" type="java.lang.String"/>

<fr:form action="<%= url %>">
    <fr:edit id="details" name="creator" schema="net.sourceforge.fenixedu.domain.FunctionalitySectionCreator.details">
        <fr:layout>
            <fr:property name="classes" value="tstyle5 thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <strong>
        <bean:message key="title.create.FunctionalitySection.step.chooseFunctionality" bundle="SITE_RESOURCES"/>
    </strong>
    
    <fr:hasMessages type="validation" for="functionality">
        <p>
            <span class="error0">
                <bean:message key="message.create.section.functionality.required" bundle="SITE_RESOURCES"/>
            </span>
        </p>
    </fr:hasMessages>

    <fr:edit id="functionality" name="creator" schema="net.sourceforge.fenixedu.domain.FunctionalitySectionCreator.functionality">
        <fr:layout name="flow">
            <fr:property name="labelExcluded" value="true"/>
            <fr:property name="hideValidators" value="true"/>
        </fr:layout>
        <fr:destination name="module.view" path="/module/view.do?module=${idInternal}"/>
        <fr:destination name="functionality.view" path="/functionality/view.do?functionality=${idInternal}"/>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.submit" bundle="SITE_RESOURCES"/>
    </html:submit>
    <html:cancel>
        <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
    </html:cancel>
</fr:form>
