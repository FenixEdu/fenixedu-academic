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

<!-- error message -->

<logic:messagesPresent message="true">
    <html:messages id="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
        <bean:write name="error"/>
    </html:messages>
</logic:messagesPresent>

<bean:define id="id" name="parent" property="idInternal"/>

<fr:create type="net.sourceforge.fenixedu.domain.functionalities.Module" 
           schema="functionalities.module.create"
           action="<%= "/module/view.do?module=" + id %>">
    <fr:hidden slot="parent" name="parent"/>
</fr:create>
