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

<bean:define id="personId" name="personFunction" property="person.idInternal"/>
<bean:define id="functionId" name="personFunction" property="idInternal"/>

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<div>
	<strong>
		<bean:message key="title.site.manage.functions.editPersonFunction" bundle="SITE_RESOURCES"/>
		<bean:message key="label.in" bundle="DEFAULT"/>
		<fr:view name="personFunction" property="function.name"/>
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

<fr:form action="<%= String.format("%s?method=managePersonFunctions&%s&personID=%s", actionName, context, personId) %>">
	<fr:edit id="editPersonFunction"  name="personFunction" schema="site.functions.person.personFunction.edit">
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
		    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>

		<fr:destination name="cancel" path="<%= String.format("%s?method=managePersonFunctions&%s&personID=%s", actionName, context, personId) %>"/>
		<fr:destination name="invalid" path="<%= String.format("%s?method=editPersonFunction&%s&personID=%s&personFunctionID=%s", actionName, context, personId, functionId) %>"/>
		<fr:destination name="exception" path="<%= String.format("%s?method=editPersonFunction&%s&personID=%s&personFunctionID=%s", actionName, context, personId, functionId) %>"/>
	</fr:edit>
	
	<html:submit>
		<bean:message key="button.submit" bundle="SITE_RESOURCES"/>
	</html:submit>
	<html:cancel>
		<bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
	</html:cancel>
</fr:form>
