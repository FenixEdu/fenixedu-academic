<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="title.functionality.manage" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div> 
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="externalId">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <html:link page="/functionality/view.do" paramId="functionality" paramName="functionality" paramProperty="externalId">
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
        availabilities from parents
     ======================  -->

<logic:present name="contextAvailabilities">
    <fr:view name="contextAvailabilities" schema="functionalities.availability.manage.context">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thleft mvert05"/>
		</fr:layout>
        <fr:destination name="viewModule" path="/module/view.do?module=${module.externalId}"/>
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
        	<span class="success0">
	            <bean:write name="confirmationMessage"/>
            </span>
        </html:messages>
    </div>
</logic:messagesPresent>

<bean:define id="oid" name="functionality" property="externalId"/>

<fr:edit name="bean" schema="functionalities.expression"
         action="<%= "/functionality/parse.do?functionality=" + oid %>">
    <fr:destination name="cancel" path="<%= "/functionality/manage.do?functionality=" + oid %>" redirect="true"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert1"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
