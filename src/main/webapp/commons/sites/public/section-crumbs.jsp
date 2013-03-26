<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="sectionCrumbs">
    <logic:iterate id="crumbSection" name="sectionCrumbs">
        &nbsp;&gt;&nbsp;
        <bean:define id="crumbSectionId" name="crumbSection" property="idInternal"/>
        <html:link page="<%= String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, crumbSectionId) %>">
            <fr:view name="crumbSection" property="name"/>
        </html:link>
    </logic:iterate>

    <logic:notPresent name="item">
        &nbsp;&gt;&nbsp;
        <fr:view name="section" property="name"/>
    </logic:notPresent>
</logic:present>

<logic:present name="item">
    &nbsp;&gt;&nbsp;
    <fr:view name="item" property="name"/>
</logic:present>