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

<fr:view name="functionality" property="name"/>

<!-- ======================
         information
     ======================  -->

<bean:message key="functionalities.delete.confirm" bundle="FUNCTIONALITY_RESOURCES"/>

<fr:view name="functionality" layout="tabular" 
         schema="functionalities.functionality.view.simple"/>

<bean:define id="id" name="functionality" property="idInternal"/>

<fr:form action="<%= "/functionality/delete.do?functionality=" + id %>">
    <html:submit property="confirm">
        <bean:message key="link.functionality.delete" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:submit>
    <html:submit property="cancel">
        <bean:message key="link.functionality.delete.cancel" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:submit>
</fr:form>
