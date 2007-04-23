<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.UnitSite"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
</h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<div>
    <strong><bean:message key="title.site.configuration.visualization" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</div>

<logic:present name="visualizationChanged">
    <p>
        <span class="success0">
            <bean:message key="message.site.configuration.visualization.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<fr:form action="<%= String.format("%s?method=updateConfiguration&amp;%s", actionName, context) %>">
    <fr:edit id="visualization" name="site" schema="custom.unitSite.visualization">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.save"/>
    </html:submit>
</fr:form>

<div class="mtop15 mbottom1">
    <strong><bean:message key="title.site.configuration.layout" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</div>

<bean:define id="imageBase" value="<%= request.getContextPath() + "/images/site/layouts/" %>"/>

<logic:equal name="site" property="layout" value="BANNER_INTRO">
    <bean:define id="layoutClass1" toScope="request" value="aimageselected"/>
</logic:equal>

<logic:equal name="site" property="layout" value="INTRO_BANNER">
    <bean:define id="layoutClass2" toScope="request" value="aimageselected"/>
</logic:equal>

<logic:equal name="site" property="layout" value="BANNER_INTRO_COLLAPSED">
    <bean:define id="layoutClass3" toScope="request" value="aimageselected"/>
</logic:equal>

<div>
    <html:link page="<%= String.format("%s?method=changeLayout&amp;layout=BANNER_INTRO&amp;%s", actionName, context) %>" 
               styleClass="<%= (request.getAttribute("layoutClass1") == null ? "aimage" : request.getAttribute("layoutClass1")).toString() %>">
        <img src="<%= imageBase + "pos1.gif" %>"/>
    </html:link>
    
    <html:link page="<%= String.format("%s?method=changeLayout&amp;layout=INTRO_BANNER&amp;%s", actionName, context) %>"
               styleClass="<%= (request.getAttribute("layoutClass2") == null ? "aimage" : request.getAttribute("layoutClass2")).toString() %>">
        <img src="<%= imageBase + "pos2.gif" %>"/>
    </html:link>
    
    <html:link page="<%= String.format("%s?method=changeLayout&amp;layout=BANNER_INTRO_COLLAPSED&amp;%s", actionName, context) %>"
               styleClass="<%= (request.getAttribute("layoutClass3") == null ? "aimage" : request.getAttribute("layoutClass3")).toString() %>">
        <img src="<%= imageBase + "pos3.gif" %>"/>
    </html:link>
</div>
