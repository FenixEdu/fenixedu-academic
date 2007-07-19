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

<bean:define id="functionId" name="function" property="idInternal"/>

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<div>
	<strong>
		<bean:message key="title.site.manage.functions.deleteFunction" bundle="SITE_RESOURCES"/>
		<bean:message key="label.from" bundle="DEFAULT"/>
		<fr:view name="function" property="unit.nameI18n"/>
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

<fr:view name="function" schema="site.functions.delete.function">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright"/>
        <fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<fr:form action="<%= String.format("%s?method=confirmDeleteFunction&amp;%s&amp;functionID=%s", actionName, context, functionId) %>">
	<html:submit>
		<bean:message key="button.confirm" bundle="SITE_RESOURCES"/>
	</html:submit>
	<html:submit property="cancel">
		<bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
	</html:submit>
</fr:form>
