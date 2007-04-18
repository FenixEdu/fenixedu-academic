<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.WebSiteManagement"/></h2>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="message.webSiteManagement"/>
    </p>
</div>

<fr:view name="sites" schema="websiteManagement.site.choose">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle1"/>
        
        <fr:property name="linkFormat(delete)" value="/manage${class.simpleName}.do?method=prepare&amp;oid=${idInternal}"/>
        <fr:property name="key(delete)" value="link.websiteManagement.site.manage"/>
    </fr:layout>
</fr:view>
