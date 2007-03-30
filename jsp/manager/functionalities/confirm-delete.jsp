<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.delete.confirm" bundle="FUNCTIONALITY_RESOURCES"/></h2>

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

    <fr:view name="functionality" property="name"/>
</div>

<!-- ======================
         message
     ======================  -->
     
<bean:message key="functionalities.delete.confirm" bundle="FUNCTIONALITY_RESOURCES"/>

<!-- ======================
         information
     ======================  -->

<fr:view name="functionality" layout="tabular" 
         schema="functionalities.functionality.view.simple">
     <fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mtop05 mbottom025"/>
	</fr:layout>
</fr:view>

<!-- ======================
         buttons
     ======================  -->
     
<bean:define id="id" name="functionality" property="idInternal"/>

<fr:form action="<%= "/functionality/delete.do?functionality=" + id %>">
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
        <bean:message key="link.functionality.delete" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:submit>
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
        <bean:message key="link.functionality.delete.cancel" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:submit>
</fr:form>
