<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.functionality.create" bundle="FUNCTIONALITY_RESOURCES"/></h2>

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
         create form
     ======================  -->

<bean:define id="id" name="module" property="externalId"/>

<fr:create type="net.sourceforge.fenixedu.domain.functionalities.Functionality" 
           schema="functionalities.concrete.functionality.create"
           action="<%= "/module/view.do?module=" + id %>">
    <fr:hidden slot="module" name="module"/>
    <fr:destination name="invalid" path="<%= "/functionality/create.do?parent=" + id %>"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:create>
