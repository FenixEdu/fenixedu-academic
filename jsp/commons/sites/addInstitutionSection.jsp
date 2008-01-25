<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
	<bean:message key="title.unitSite.institutionSection" bundle="SITE_RESOURCES"/>
</h2>

<bean:define id="parentId" name="parent" property="idInternal"/>
<bean:define id="siteId" name="site" property="idInternal"/>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><bean:message key="label.unitSite.institutionSection.choose" bundle="SITE_RESOURCES"/>:</p>

<fr:view name="template" property="pool">
	<fr:layout>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="eachSchema" value="functionalities.functionality.tree"/>
	</fr:layout>
	<fr:destination name="functionality.view" path="<%= actionName + "?method=addFromPool&amp;containerId=" +  parentId + "&amp;contentId=${idInternal}&amp;oid=" + siteId + "&amp;sectionID=" + parentId %>"/>
</fr:view>
