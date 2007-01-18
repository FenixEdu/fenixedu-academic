<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="fileItem" type="net.sourceforge.fenixedu.domain.FileItem" name="fileItem"/>
<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>
<bean:define id="siteId" name="site" property="idInternal"/>

<h2>
    <fr:view name="item" property="name" />
</h2>

<p class="mbottom05">
	<strong>
		<bean:message key="label.teacher.siteAdministration.editItemFilePermissions.editPermissions"/>
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
         action="<%= String.format("%s?method=editItemFilePermissions&%s&itemID=%s&fileItemId=%s", actionName, context, item.getIdInternal(), fileItem.getIdInternal()) %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("%s?method=section&%s&sectionID=%s", actionName, context, item.getSection().getIdInternal()) %>"/>
</fr:edit>

