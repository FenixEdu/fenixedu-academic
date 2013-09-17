<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="sectionCrumbs">
    <logic:iterate id="crumbSection" name="sectionCrumbs">
        &nbsp;&gt;&nbsp;
        <bean:define id="crumbSectionId" name="crumbSection" property="externalId"/>
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