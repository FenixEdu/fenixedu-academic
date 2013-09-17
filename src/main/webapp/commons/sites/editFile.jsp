<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="fileItem" type="net.sourceforge.fenixedu.domain.FileContent" name="fileItem"/>
<bean:define id="section" type="net.sourceforge.fenixedu.domain.Section" name="section"/>
<bean:define id="siteId" name="site" property="externalId"/>

<h2>
	<logic:present name="item">
    	<fr:view name="item" property="name" />
    </logic:present>
    <logic:notPresent name="item">
    	<fr:view name="section" property="name"/>
    </logic:notPresent> 
</h2>

<p class="mbottom05">
	<strong>
		<bean:message key="label.teacher.siteAdministration.editItemFilePermissions.editPermissions" bundle="SITE_RESOURCES"/>
	</strong>
</p>

<span class="error">
    <html:errors />
</span>

<fr:view name="fileItem" schema="item.file.basic">
    <fr:layout name="tabular">
        <fr:property name="classes" value="thleft thlight thtop tstyle1 mtop05"/>
    </fr:layout>
</fr:view>

<fr:edit name="fileItemBean" schema="item.file.permittedGroup" 
         action="<%= String.format("%s?method=editItemFilePermissions&%s&fileItemId=%s&sectionID=%s", actionName, context, fileItem.getExternalId(), section.getExternalId()) %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("%s?method=section&%s&sectionID=%s", actionName, context, section.getExternalId()) %>"/>
</fr:edit>

