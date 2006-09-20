<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.toplevel" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->
     
<div>
    <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/> //
</div>

<!-- ======================
         error message
     ======================  -->

<logic:messagesPresent message="true">
    <div>
        <html:messages id="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
            <bean:write name="error"/>
        </html:messages>
    </div>
</logic:messagesPresent>

<!-- ======================
         export/import 
     ======================  -->
     
<html:link page="/functionality/exportStructure.do">
    <bean:message key="link.functionality.export" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<html:link page="/toplevel/upload.do">
    <bean:message key="link.module.import" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>

<!-- ======================
         tree
     ======================  -->
<!-- form used to submit the tree structure 
     search for: saveTree()
  -->
<fr:form action="/toplevel/organize.do">
    <input id="tree-structure" type="hidden" name="tree" value="" />
</fr:form>

<% String tree = "topLevelTree"; %>

<fr:view name="functionalities" layout="functionalities-tree">
    <fr:layout>
        <fr:property name="treeId" value="<%= tree %>"/>
        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->

        <fr:property name="eachLayout" value="values-dash"/>
        
        <fr:property name="schemaFor(Module)" value="functionalities.module.tree"/>
        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>
        
        <fr:property name="hiddenLinks">
            <html:link page="/functionality/up.do?functionality=${idInternal}">
                <bean:message key="link.functionality.up" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="/functionality/down.do?functionality=${idInternal}">
                <bean:message key="link.functionality.down" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="/functionality/top.do?functionality=${idInternal}">
                <bean:message key="link.functionality.top" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="/functionality/bottom.do?functionality=${idInternal}">
                <bean:message key="link.functionality.bottom" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,           
            <html:link page="/functionality/indent.do?functionality=${idInternal}">
                <bean:message key="link.functionality.indent" bundle="FUNCTIONALITY_RESOURCES"/>
            </html:link>,
            <html:link page="/functionality/outdent.do?functionality=${idInternal}">
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
    
    <fr:form action="/toplevel/view.do">
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

<p>
    <html:link page="/module/create.do">
        <bean:message key="link.module.create" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link>
    
    <html:link page="/functionality/create.do">
        <bean:message key="link.functionality.create" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link>
</p>
