<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
    <bean:message bundle="PEDAGOGICAL_COUNCIL" key="title.pedagogicalCouncil.site"/>
</h2>

<bean:define id="siteId" name="site" property="idInternal"/>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message bundle="PEDAGOGICAL_COUNCIL" key="label.pedagogicalCouncil.info.chooseManagers"/>
    </p>
</div>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error" bundle="PEDAGOGICAL_COUNCIL">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<p class="mbottom05">
    <strong><bean:message bundle="PEDAGOGICAL_COUNCIL" key="title.pedagogicalCouncil.site.chooseManagers"/>:</strong>
</p>

<logic:empty name="managers">
	<p>
	    <em><bean:message bundle="PEDAGOGICAL_COUNCIL" key="message.pedagogicalCouncil.managers.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="managers">
    <fr:view name="managers" schema="pedagogicalCouncil.manager">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05"/>
            <fr:property name="link(delete)" value="<%= String.format("/manageSitePermissions.do?method=removeManager&amp;siteID=%s", siteId) %>"/>
            <fr:property name="param(delete)" value="idInternal/managerID"/>
            <fr:property name="key(delete)" value="link.pedagogicalCouncil.managers.remove"/>
            <fr:property name="bundle(delete)" value="PEDAGOGICAL_COUNCIL"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<div class="mtop2">
	<p class="mbottom05">
	    <strong><bean:message bundle="PEDAGOGICAL_COUNCIL" key="title.pedagogicalCouncil.managers.add"/></strong>
    </p>
    
    <p class="mvert05">
        <em><bean:message bundle="PEDAGOGICAL_COUNCIL" key="title.pedagogicalCouncil.managers.add.message"/></em>
    </p>

    <logic:messagesPresent message="true" property="addError">
        <html:messages id="message" message="true" property="addError" bundle="PEDAGOGICAL_COUNCIL">
            <p><span class="error0"><bean:write name="message"/></span></p>
        </html:messages>
    </logic:messagesPresent>
    
    <fr:form action="<%="/manageSitePermissions.do?method=addManager&amp;siteID=" + siteId %>">
        <fr:edit id="add" name="managersBean" schema="pedagogicalCouncil.managers.add">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= "/manageSitePermissions.do?method=chooseManagers&siteID=" + siteId %>"/>
        </fr:edit>

        <html:submit styleClass="mtop05">
            <bean:message bundle="PEDAGOGICAL_COUNCIL" key="button.add"/> 
        </html:submit>
    </fr:form>
</div>
