<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.module.edit" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->
<div>
    <html:link page="/toplevel/view.do">
        <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link> //
    
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="idInternal">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <html:link page="/module/view.do" paramId="module" paramName="module" paramProperty="idInternal">
        <fr:view name="module" property="name"/>
    </html:link>
</div>

<!-- ======================
         error message
     ======================  -->

<logic:messagesPresent message="true">
    <div>
        <html:messages id="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
            <bean:write name="error"/>
        </html:messages>
    </div>
</logic:messagesPresent>

<!-- ======================
         edit form
     ======================  -->

<bean:define id="id" name="module" property="idInternal"/>

<fr:edit name="module" layout="tabular" schema="functionalities.module.edit" action="<%= "/module/view.do?module=" + id %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
