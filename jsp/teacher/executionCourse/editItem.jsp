<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="title.item.edit" bundle="SITE_RESOURCES"/>
</h2>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="sectionId" name="item" property="section.idInternal"/>

<fr:form action="<%= String.format("/manageExecutionCourse.do?method=section&executionCourseID=%s&sectionID=%s", executionCourseId, sectionId) %>">
    <fr:edit name="item" type="net.sourceforge.fenixedu.domain.Item" schema="net.sourceforge.fenixedu.domain.ItemEditor">
        <fr:layout name="tabular">
            <fr:property name="classes" value="thtop thlight thright mbottom1"/>
        </fr:layout>
    </fr:edit>
    
    <logic:notEmpty name="item" property="fileItems">
        <h3>
            <bean:message key="title.item.files" bundle="SITE_RESOURCES"/>
        </h3>
    
        <fr:edit name="item" property="sortedFileItems" schema="site.item.files.visible">
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
    
    <html:submit property="editItemButton">
        <bean:message key="button.item.edit.submit" bundle="SITE_RESOURCES"/>
    </html:submit>

    <html:cancel>
        <bean:message key="button.item.edit.cancel" bundle="SITE_RESOURCES"/>
    </html:cancel>
</fr:form>
