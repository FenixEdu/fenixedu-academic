<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
    <bean:message key="title.departmentSite.site"/>
</h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.departmentSite.info.information"/>
    </p>
</div>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.departmentSite.information.changed"/>
        </span>
    </p>
</logic:present>

<fr:form action="/departmentSite.do?method=information">
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
