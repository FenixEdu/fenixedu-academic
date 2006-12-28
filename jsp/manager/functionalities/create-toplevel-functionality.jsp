<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.functionality.create" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div>
    <html:link page="/toplevel/view.do">
        <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link> //
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

<fr:create type="net.sourceforge.fenixedu.domain.functionalities.Functionality"
           schema="functionalities.concrete.functionality.create"
           action="/toplevel/view.do">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:create>
