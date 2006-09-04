<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<!-- ======================
         bread crumbs
     ======================  -->

<html:link page="/toplevel/view.do">
    <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link> //

<!-- error message -->

<logic:messagesPresent message="true">
    <html:messages id="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
        <bean:write name="error"/>
    </html:messages>
</logic:messagesPresent>

<fr:create type="net.sourceforge.fenixedu.domain.functionalities.Module"
           schema="functionalities.module.create"
           action="/toplevel/view.do"/>
