<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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

<fr:view name="functionality" layout="tabular" 
         schema="functionalities.functionality.view.simple"/>

<!-- ======================
           links
     ======================  -->

<html:link page="/functionality/edit.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.edit" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<html:link page="/functionality/confirm.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.delete" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<!-- ======================
          availability
     ======================  -->

<fr:view name="functionality" layout="tabular" schema="functionalities.functionality.availability"/>

<html:link page="/functionality/manage.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.manage" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>