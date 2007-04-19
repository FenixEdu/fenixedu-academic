<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
    <bean:message key="title.departmentSite.site.information" bundle="WEBSITEMANAGER_RESOURCES"/>
</h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.departmentSite.info.information" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.departmentSite.information.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<bean:define id="oid" name="site" property="idInternal"/>
<fr:form action="<%= "/manageDepartmentSite.do?method=information&amp;oid=" + oid %>">
    <fr:edit name="site" slot="description">
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
