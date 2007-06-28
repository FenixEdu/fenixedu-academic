<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
    <bean:message key="title.scientificCouncil.site"/>
</h2>

<bean:define id="siteId" name="site" property="idInternal"/>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.scientificCouncil.info.chooseManagers"/>
    </p>
</div>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error" bundle="scientific_COUNCIL">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<p class="mbottom05">
    <strong><bean:message key="title.scientificCouncil.site.chooseManagers"/>:</strong>
</p>

<logic:empty name="managers">
	<p>
	    <em><bean:message key="message.scientificCouncil.managers.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="managers">
    <fr:view name="managers" schema="departmentSite.manager">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05"/>
            <fr:property name="link(delete)" value="<%= String.format("/manageSitePermissions.do?method=removeManager&amp;siteID=%s", siteId) %>"/>
            <fr:property name="param(delete)" value="idInternal/managerID"/>
            <fr:property name="key(delete)" value="link.scientificCouncil.managers.remove"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<div class="mtop2">
	<p class="mbottom05">
	    <strong><bean:message key="title.scientificCouncil.managers.add"/></strong>
    </p>
    
    <p class="mvert05">
        <em><bean:message key="title.scientificCouncil.managers.add.message"/></em>
    </p>

    <logic:messagesPresent message="true" property="addError">
        <html:messages id="message" message="true" property="addError">
            <p><span class="error0"><bean:write name="message"/></span></p>
        </html:messages>
    </logic:messagesPresent>
    
    <fr:form action="<%="/manageSitePermissions.do?method=addManager&amp;siteID=" + siteId %>">
        <fr:edit id="add" name="managersBean" schema="departmentSite.managers.add">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= "/manageSitePermissions.do?method=chooseManagers&siteID=" + siteId %>"/>
        </fr:edit>

        <html:submit styleClass="mtop05">
            <bean:message key="button.add"/> 
        </html:submit>
    </fr:form>
</div>
