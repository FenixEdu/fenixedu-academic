<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
    <bean:message key="title.departmentSite.site"/>
</h2>

<strong>
    <bean:message key="title.departmentSite.site.chooseManagers"/>
</strong>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.departmentSite.info.chooseManagers"/>
    </p>
</div>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="managers">
    <em><bean:message key="message.departmentSite.managers.empty"/></em>
</logic:empty>

<logic:notEmpty name="managers">
    <fr:view name="managers" schema="departmentSite.manager">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1"/>
        
            <fr:property name="link(delete)" value="<%= String.format("/departmentSite.do?method=removeManager") %>"/>
            <fr:property name="param(delete)" value="idInternal/managerID"/>
            <fr:property name="key(delete)" value="link.departmentSite.managers.remove"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<div class="mtop2">
    <strong><bean:message key="title.departmentSite.managers.add"/></strong>
    
    <p>
        <em><bean:message key="title.departmentSite.managers.add.message"/></em>
    </p>

    <logic:messagesPresent message="true" property="addError">
        <html:messages id="message" message="true" property="addError">
            <p><span class="error0"><bean:write name="message"/></span></p>
        </html:messages>
    </logic:messagesPresent>
    
    <fr:form action="/departmentSite.do?method=addManager">
        <fr:edit id="add" name="managersBean" schema="departmentSite.managers.add">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="/departmentSite.do?method=chooseManagers"/>
        </fr:edit>

        <html:submit styleClass="mtop05">
            <bean:message key="button.add"/> 
        </html:submit>
    </fr:form>
</div>
