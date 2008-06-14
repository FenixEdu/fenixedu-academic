<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.unitSite.chooseManagers" bundle="SITE_RESOURCES"/>
</h2>

<logic:present name="backLink">
	<bean:write name="backLink" filter="false"/>
</logic:present>

<bean:define id="siteId" name="site" property="idInternal"/>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.unitSite.info.chooseManagers" bundle="SITE_RESOURCES"/>
    </p>
</div>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error" bundle="SITE_RESOURCES">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<p class="mbottom05">
    <strong><bean:message key="title.unitSite.managers" bundle="SITE_RESOURCES"/></strong>
</p>

<logic:empty name="managers">
	<p>
	    <em><bean:message key="message.unitSite.managers.empty" bundle="SITE_RESOURCES"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="managers">
    <fr:view name="managers" schema="unitSite.manager">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05"/>
            <fr:property name="link(delete)" value="<%= String.format("%s?method=removeManager&amp;%s", actionName, context) %>"/>
            <fr:property name="param(delete)" value="idInternal/managerID"/>
            <fr:property name="key(delete)" value="link.unitSite.managers.remove"/>
            <fr:property name="bundle(delete)" value="SITE_RESOURCES"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<div class="mtop2">
	<p class="mbottom05">
	    <strong><bean:message key="title.unitSite.managers.add" bundle="SITE_RESOURCES"/></strong>
    </p>
    
    <p class="mvert05">
        <em><bean:message key="title.unitSite.managers.add.message" bundle="SITE_RESOURCES"/></em>
    </p>

    <logic:messagesPresent message="true" property="addError">
        <html:messages id="message" message="true" property="addError" bundle="SITE_RESOURCES">
            <p><span class="error0"><bean:write name="message"/></span></p>
        </html:messages>
    </logic:messagesPresent>
    
    <fr:form action="<%=String.format("%s?method=addManager&amp;%s", actionName, context) %>">
        <fr:edit id="add" name="managersBean" schema="unitSite.managers.add">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= String.format("%s?method=chooseManagers&amp;%s", actionName, context) %>"/>
        </fr:edit>

        <html:submit styleClass="mtop05">
            <bean:message key="button.add" bundle="SITE_RESOURCES"/> 
        </html:submit>
    </fr:form>
</div>
