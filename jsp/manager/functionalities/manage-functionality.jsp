<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.functionality.manage" bundle="FUNCTIONALITY_RESOURCES"/></h2>

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
    
    <html:link page="/functionality/view.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
        <fr:view name="functionality" property="name"/>
    </html:link>
</div>

<!-- ======================
         information
     ======================  -->

<fr:view name="functionality" layout="tabular" 
         schema="functionalities.functionality.view.simple">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mvert05"/>
	</fr:layout>
</fr:view>

<!-- ======================
        enable disable
     ======================  -->

<logic:equal name="functionality" property="enabled" value="true">
	<p class="mtop025">
    <html:link page="/functionality/disable.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
        <bean:message key="link.functionality.disable" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link>
    </p>
</logic:equal>

<logic:equal name="functionality" property="enabled" value="false">
	<p class="mtop025">
    <html:link page="/functionality/enable.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
        <bean:message key="link.functionality.enable" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link>
    </p>
</logic:equal>

<!-- ======================
        availabilities from parents
     ======================  -->

<logic:present name="contextAvailabilities">
    <fr:view name="contextAvailabilities" schema="functionalities.availability.manage.context">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thleft mvert05"/>
		</fr:layout>
        <fr:destination name="viewModule" path="/module/view.do?module=${module.idInternal}"/>
    </fr:view>
</logic:present>

<!-- ======================
        expression help
     ======================  -->

<div class="infoop2 mtop15 mbottom1" style="width: 600px;">
    <bean:message key="functionalities.expression.help" bundle="FUNCTIONALITY_RESOURCES" arg0="<%= request.getContextPath() %>"/>
</div>

<!-- ======================
        expression
     ======================  -->

<logic:messagesPresent property="error" message="true">
    <div>
        <!-- Error message -->
        <html:messages id="errorMessage" property="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
            <bean:write name="errorMessage"/>
        </html:messages>
    
        <div>
            <!-- Expression Report: code + place of error -->
            <logic:present name="parserReport">
                <fr:view name="parserReport">
                    <fr:layout>
                        <fr:property name="errorClass" value="errorRegion"/>
                        <fr:property name="errorStyle" value="background: #FAA;"/>
                    </fr:layout>
                </fr:view>
            </logic:present>
        </div>
    </div>
</logic:messagesPresent>

<logic:messagesPresent property="expression" message="true">
    <div>
        <html:messages id="confirmationMessage" property="expression" message="true" bundle="FUNCTIONALITY_RESOURCES">
            <bean:write name="confirmationMessage"/>
        </html:messages>
    </div>
</logic:messagesPresent>

<bean:define id="oid" name="functionality" property="idInternal"/>

<fr:edit name="bean" schema="functionalities.expression"
         action="<%= "/functionality/parse.do?functionality=" + oid %>">
    <fr:destination name="cancel" path="<%= "/functionality/manage.do?functionality=" + oid %>" redirect="true"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert1"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
