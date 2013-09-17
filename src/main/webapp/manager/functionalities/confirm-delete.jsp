<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="title.delete.confirm" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div>
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="externalId">
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
     
<bean:define id="id" name="functionality" property="externalId"/>

<fr:form action="<%= "/functionality/delete.do?functionality=" + id %>">
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
        <bean:message key="link.functionality.delete" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:submit>
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
        <bean:message key="link.functionality.delete.cancel" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:submit>
</fr:form>
