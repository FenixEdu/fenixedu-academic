<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<!-- ======================
         bread crumbs
     ======================  -->

<html:link page="/toplevel/view.do">
    <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link> //

<fr:create type="net.sourceforge.fenixedu.domain.functionalities.Module"
           schema="functionalities.module.create"
           action="/toplevel/view.do"/>
