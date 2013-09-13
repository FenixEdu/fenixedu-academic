<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.section.edit" bundle="SITE_RESOURCES"/>
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
    <bean:define id="sectionId" name="section" property="externalId"/>

    <bean:define id="formUrl" value="<%= actionName + "?method=functionalitySection&amp;" + context + "&amp;sectionID=" + sectionId %>" toScope="request"/>
</logic:present>

<bean:define id="url" name="formUrl" type="java.lang.String"/>

<fr:form action="<%= url %>">
    <fr:edit id="details" name="section" schema="net.sourceforge.fenixedu.domain.FunctionalitySectionEditor.details">
        <fr:layout>
            <fr:property name="classes" value="tstyle5 thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <strong>
        <bean:message key="title.create.institutionSection.chooseFunctionality" bundle="SITE_RESOURCES"/>
    </strong>
    
    <fr:hasMessages type="validation" for="functionality">
        <p>
            <span class="error0">
                <bean:message key="message.create.institutionalSection.required" bundle="SITE_RESOURCES"/>
            </span>
        </p>
    </fr:hasMessages>

    <fr:edit id="functionality" name="section" schema="net.sourceforge.fenixedu.domain.FunctionalitySectionEditor.unitSite.functionality">
        <fr:layout name="flow">
            <fr:property name="labelExcluded" value="true"/>
            <fr:property name="hideValidators" value="true"/>
        </fr:layout>
        <fr:destination name="module.view" path="/module/view.do?module=${externalId}"/>
        <fr:destination name="functionality.view" path="/functionality/view.do?functionality=${externalId}"/>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.submit" bundle="SITE_RESOURCES"/>
    </html:submit>
    <html:cancel>
        <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
    </html:cancel>
</fr:form>
