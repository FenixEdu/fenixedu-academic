<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="siteId" name="site" property="externalId"/>
<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<logic:notEmpty name="item" property="sortedFileItems">
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
        <fr:view name="item" property="sortedFileItems">
            <fr:layout name="tree">
                <fr:property name="treeId" value="filesOrder"/>
                <fr:property name="fieldId" value="files-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(FileItem)" value="site.item.file.basic"/>
            </fr:layout>
        </fr:view>

	    <fr:form action="<%= actionName + "?method=saveFilesOrder&amp;" + context + "&amp;sectionID=" + section.getExternalId() + "&amp;itemID=" + item.getExternalId() %>">
	        <input alt="input.filesOrder" id="files-order" type="hidden" name="filesOrder" value=""/>
	    </fr:form>

		<p class="mtop15">
		    <fr:form action="<%= actionName + "?method=section&amp;" + context + "&amp;sectionID=" + section.getExternalId() %>">
		        <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('filesOrder');" %>">
		            <bean:message key="button.item.files.order.save" bundle="SITE_RESOURCES"/>
		        </html:button>
		        <html:submit>
		            <bean:message key="button.item.files.order.reset" bundle="SITE_RESOURCES"/>
		        </html:submit>
		    </fr:form>
	    </p>
    </div>

<p style="color: #888;">
	<em><bean:message key="message.file.reorder.tip" bundle="SITE_RESOURCES"/></em>
</p>

</logic:notEmpty>
