<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="thisModule" name="module" property="idInternal"/>

<!-- ======================
         bread crumbs
     ======================  -->

<html:link page="/toplevel/view.do">
    <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link> //
<logic:iterate id="crumb" name="crumbs">
    <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="idInternal">
        <fr:view name="crumb" property="name"/>
    </html:link> &gt;
</logic:iterate>

<fr:view name="module" property="name"/>

<!-- ======================
       module information
     ======================  -->
  
<fr:view name="module" layout="tabular" schema="functionalities.module.view.simple"/>

<html:link page="<%= "/module/edit.do?module=" + thisModule %>">
    <bean:message key="link.module.edit" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<html:link page="<%= "/functionality/delete.do?functionality=" + thisModule %>">
    <bean:message key="link.module.delete" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<!-- ======================
             tree
     ======================  -->

<!-- form used to submit the tree structure 
     search for: saveTree()
  -->
<fr:form action="<%= "/module/organize.do?module=" + thisModule %>">
    <input id="tree-structure" type="hidden" name="tree" value="" />
</fr:form>

<% String tree = "moduleTree" + thisModule; %>

<fr:view name="functionalities" layout="tree">
    <fr:layout>
        <fr:property name="treeId" value="<%= tree %>"/>
        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
        <fr:property name="hideLinks" value="true"/>

        <fr:property name="eachLayout" value="values-dash"/>
        
        <fr:property name="schemaFor(Module)" value="functionalities.module.tree"/>
        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>
        
        <fr:property name="imageFor(Module)" value="/javaScript/drag-drop-folder-tree/images/folder.gif"/>
        <fr:property name="imageFor(Functionality)" value="/javaScript/drag-drop-folder-tree/images/sheet.gif"/>

        <fr:property name="childrenFor(Module)" value="orderedFunctionalities"/>

        <fr:property name="links">
            <html:link page="<%= "/functionality/up.do?module=" + thisModule + "&functionality=${idInternal}" %>">
                <bean:message key="link.functionality.up" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="<%= "/functionality/down.do?module=" + thisModule + "&functionality=${idInternal}" %>">
                <bean:message key="link.functionality.down" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="<%= "/functionality/top.do?module=" + thisModule + "&functionality=${idInternal}" %>">
                <bean:message key="link.functionality.top" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="<%= "/functionality/bottom.do?module=" + thisModule + "&functionality=${idInternal}" %>">
                <bean:message key="link.functionality.bottom" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,           
            <html:link page="<%= "/functionality/indent.do?module=" + thisModule + "&functionality=${idInternal}" %>">
                <bean:message key="link.functionality.indent" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="<%= "/functionality/outdent.do?module=" + thisModule + "&functionality=${idInternal}" %>">
                <bean:message key="link.functionality.outdent" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link> 
        </fr:property>
    </fr:layout>

    <fr:destination name="module.view" path="/module/view.do?module=${idInternal}"/>
    <fr:destination name="functionality.view" path="/functionality/view.do?functionality=${idInternal}"/>
</fr:view>

<!-- ======================
         tree controls
     ======================  -->

<div id="tree-controls" style="display: none;">
    <p>
        <a href="#" onclick="<%= tree %>.expandAll();"><bean:message key="link.tree.expand-all" bundle="FUNCTIONALITY_RESOURCES"/></a>
        | <a href="#" onclick="<%= tree %>.collapseAll();"><bean:message key="link.tree.collapse-all" bundle="FUNCTIONALITY_RESOURCES"/></a>
    </p>
    
    <fr:form action="<%= "/module/view.do?module=" + thisModule %>">
        <!-- submits the form on top of the page, search for: tree-structure -->
        <html:button property="saveButton" onclick="<%= tree + ".saveTree();" %>">
            <bean:message key="button.tree.save" bundle="FUNCTIONALITY_RESOURCES"/>
        </html:button>
    
        <html:submit>
            <bean:message key="button.tree.reset" bundle="FUNCTIONALITY_RESOURCES"/>
        </html:submit>
    </fr:form>
</div>

<script type="text/javascript">
    // show hidden div
    document.getElementById("tree-controls").style.display = 'block';
</script>

<!-- ======================
         create links
     ======================  -->

<html:link page="<%= "/module/create.do?parent=" + thisModule %>">
    <bean:message key="link.module.create" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<html:link page="<%= "/functionality/create.do?module=" + thisModule %>">
    <bean:message key="link.functionality.create" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>
