<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="title.uploadStructure" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div>  
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="externalId">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
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
         upload form
     ======================  -->

<bean:define id="action">
    /module/importStructure.do?module=<bean:write name="module" property="externalId"/>
</bean:define>

<bean:define id="cancel">
    /module/view.do?module=<bean:write name="module" property="externalId"/>
</bean:define>

<fr:edit id="structure" name="bean" schema="functionalities.structure.bean"
         action="<%= action %>">
         <fr:destination name="cancel" path="<%= cancel %>"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert1"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>