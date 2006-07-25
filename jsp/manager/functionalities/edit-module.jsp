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
<html:link page="/module/view.do" paramId="module" paramName="module" paramProperty="idInternal">
        <fr:view name="module" property="name"/>
</html:link>

<bean:define id="id" name="module" property="idInternal"/>

<fr:edit name="module" 
         layout="tabular" 
         schema="functionalities.module.edit"
         action="<%= "/module/view.do?module=" + id %>"/>
