<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="unit" name="site" property="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="targetId" name="target" property="idInternal"/>

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<div>
	<strong>
		<bean:message key="title.site.manage.functions.createFunction" bundle="SITE_RESOURCES"/>
		<bean:message key="label.in" bundle="DEFAULT"/>
		<fr:view name="target" property="nameI18n"/>
	</strong>
</div>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<fr:edit id="create" name="bean" schema="site.functions.create.function"
         action="<%= String.format("%s?method=createFunction&amp;%s&amp;unitID=%s", actionName, context, targetId) %>">
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="invalid" path="<%= String.format("%s?method=addFunction&%s&unitID=%s", actionName, context, targetId) %>"/>
</fr:edit>
