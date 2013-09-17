<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
	<bean:message key="title.unitSite.institutionSection" bundle="SITE_RESOURCES"/>
</h2>

<bean:define id="parentId" name="parent" property="externalId"/>
<bean:define id="siteId" name="site" property="externalId"/>

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
	<fr:destination name="functionality.view" path="<%= actionName + "?method=addFromPool&amp;containerId=" +  parentId + "&amp;contentId=${externalId}&amp;oid=" + siteId + "&amp;sectionID=" + parentId + "&amp;" + context%>"/>
</fr:view>
