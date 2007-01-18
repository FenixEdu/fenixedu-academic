<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="section">
    <h2><fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></h2>

    <bean:define id="sectionId" name="section" property="idInternal"/>

    <p>
       <em><bean:message key="message.section.view.mustLogin" bundle="SITE_RESOURCES"/></em>
       <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       </html:link>.
    </p>
</logic:present>
