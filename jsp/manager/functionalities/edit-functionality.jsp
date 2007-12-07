<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.functionality.edit" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div>   
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="idInternal">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <html:link page="/functionality/view.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
        <fr:view name="functionality" property="name"/>
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

<bean:define id="id" name="functionality" property="idInternal"/>

<fr:edit id="viewFunctionality" name="functionality" layout="tabular" schema="functionalities.functionality.edit" action="<%= "/functionality/view.do?functionality=" + id %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<bean:define id="removeURL" type="java.lang.String">/functionality/removeParameter.do?functionality=<bean:write name="functionality" property="idInternal"/></bean:define>
<fr:view name="functionality" property="parameters" schema="functionalities.functionality.parameter.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mvert05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		<fr:property name="link(removeParameter)" value="<%= removeURL %>" />
		<fr:property name="param(removeParameter)" value="idInternal/functionalityParameter" />
		<fr:property name="key(removeParameter)" value="link.remove" />
	</fr:layout>
</fr:view>

<fr:create id="createFunctionalityParameter" action="<%= "/functionality/view.do?functionality=" + id %>" type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter" 
	schema="functionalities.functionality.parameter.create">
	<fr:hidden slot="functionality" name="functionality"/>
</fr:create>
