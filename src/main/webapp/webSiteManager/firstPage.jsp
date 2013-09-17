<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.WebSiteManagement"/></h2>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="message.webSiteManagement"/>
    </p>
</div>

<fr:view name="sites" schema="websiteManagement.site.choose">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle1 thlight"/>
    </fr:layout>
</fr:view>
