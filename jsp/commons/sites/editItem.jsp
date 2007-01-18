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

<h2>
	<bean:message key="title.item.edit" bundle="SITE_RESOURCES"/>
</h2>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>


<bean:define id="siteId" name="site" property="idInternal"/>
<bean:define id="sectionId" name="item" property="section.idInternal"/>

<fr:form action="<%= String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
    <fr:edit id="edit-item" name="item" type="net.sourceforge.fenixedu.domain.Item" schema="net.sourceforge.fenixedu.domain.ItemEditor">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
    </fr:edit>
    
    <logic:notEmpty name="item" property="fileItems">
        <h3>
            <bean:message key="title.item.files" bundle="SITE_RESOURCES"/>
        </h3>
    
        <fr:edit id="edit-files" name="item" property="sortedFileItems" schema="site.item.files.visible">
            <fr:layout name="tabular-editable">
                <fr:property name="customLink(add)">
                    <span class="switchInline">
                        (<a href="javascript:insertLink('${downloadUrl}', '${displayName}');"><bean:message key="link.item.edit.add.file" bundle="SITE_RESOURCES"/></a>)
                    </span>
                </fr:property>
            </fr:layout>
        </fr:edit>
        
        <script type="text/javascript">
            function hasTinyMCE() {
                return tinyMCE.configs.length > 0;
            }
            
            function insertLink(url, name) {
                if (hasTinyMCE()) {
                    tinyMCE.execCommand('mceInsertContent', false, '<a href="' + url + '">' + name + '</a>');
                }
            }
    
            if (hasTinyMCE()) {
                switchGlobal();
            }
        </script>
    </logic:notEmpty>
    
    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.editItemButton" property="editItemButton">
        <bean:message key="button.item.edit.submit" bundle="SITE_RESOURCES"/>
    </html:submit>

    <html:cancel>
        <bean:message key="button.item.edit.cancel" bundle="SITE_RESOURCES"/>
    </html:cancel>
</fr:form>
