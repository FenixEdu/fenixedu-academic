<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!-- ======================
         bread crumbs
     ======================  -->

<html:link page="/toplevel/view.do">
    <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link> //
<logic:iterate id="crumb" name="crumbs">
    <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="idInternal">
        <fr:view name="crumb" property="name"/>
    </html:link> &gt;
</logic:iterate>
<html:link page="/functionality/view.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <fr:view name="functionality" property="name"/>
</html:link>

<!-- error message -->

<logic:messagesPresent message="true">
    <html:messages id="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
        <bean:write name="error"/>
    </html:messages>
</logic:messagesPresent>

<bean:define id="id" name="functionality" property="idInternal"/>

<fr:edit name="functionality" layout="tabular" 
         schema="functionalities.functionality.edit"
         action="<%= "/functionality/view.do?functionality=" + id %>"/>
