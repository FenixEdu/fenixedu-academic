<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<bean:define id="thisModule" name="module" property="externalId"/>

<h2><bean:message key="title.module" bundle="FUNCTIONALITY_RESOURCES"/>: <fr:view name="module" property="name"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div>
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="externalId">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <fr:view name="module" property="name"/>
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
          information
     ======================  -->
  
<fr:view name="module" layout="tabular" schema="functionalities.module.view.simple">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mtop05 mbottom025"/>
	</fr:layout>
</fr:view>

<!-- ======================
           links
     ======================  -->
<p class="mtop025">
	<html:link page="/module/edit.do" paramId="module" paramName="module" paramProperty="externalId">
	    <bean:message key="link.module.edit" bundle="FUNCTIONALITY_RESOURCES"/>
	</html:link>, 
	<html:link page="/functionality/confirm.do" paramId="functionality" paramName="module" paramProperty="externalId">
	    <bean:message key="link.module.delete" bundle="FUNCTIONALITY_RESOURCES"/>
	</html:link>
</p>

<!-- ======================
          availability
     ======================  -->

<fr:view name="module" layout="tabular" schema="functionalities.functionality.availability">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mtop2 mtop05 mbottom025"/>
	</fr:layout>
</fr:view>
<p class="mtop025">
	<html:link page="/functionality/manage.do" paramId="functionality" paramName="module" paramProperty="externalId">
	    <bean:message key="link.module.manage" bundle="FUNCTIONALITY_RESOURCES"/>
	</html:link>
</p>

<!-- ======================
         create links
     ======================  -->
<br><p>
	<html:link page="<%= "/module/create.do?parent=" + thisModule %>">
	    <bean:message key="link.module.create" bundle="FUNCTIONALITY_RESOURCES"/>
	</html:link>,
	<html:link page="<%= "/functionality/create.do?module=" + thisModule %>">
	    <bean:message key="link.functionality.create" bundle="FUNCTIONALITY_RESOURCES"/>
	</html:link>,
	<html:link page="/module/uploadStructure.do" paramId="module" paramName="module" paramProperty="externalId">
	    <bean:message key="link.module.import" bundle="FUNCTIONALITY_RESOURCES"/>
	</html:link>
</p><br>

<!-- ======================
             tree
     ======================  -->


<logic:notEmpty name="functionalities">
	<p>
		<html:link page="/functionality/exportStructure.do" paramId="functionality" paramName="module" paramProperty="externalId">
		    <bean:message key="link.functionality.export" bundle="FUNCTIONALITY_RESOURCES"/>
		</html:link>
	</p>
	<!-- form used to submit the tree structure 
	     search for: saveTree()
	  -->	  
	<fr:form action="<%= "/module/organize.do?module=" + thisModule %>">
	    <input alt="input.tree" id="tree-structure" type="hidden" name="tree" value="" />
	</fr:form>
	
	<% String tree = "moduleTree" + thisModule; %>
	
	<fr:view name="functionalities" layout="functionalities-tree">
	    <fr:layout>
	        <fr:property name="treeId" value="<%= tree %>"/>
	        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
	
	        <fr:property name="eachLayout" value="values-dash"/>
	        
	        <fr:property name="schemaFor(Module)" value="functionalities.module.tree"/>
	        <fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>
	        
	        <fr:property name="movedClass" value="highlight3"/>
	   </fr:layout>
	
	   <fr:destination name="module.view" path="/module/view.do?module=${externalId}"/>
	   <fr:destination name="functionality.view" path="/functionality/view.do?functionality=${externalId}"/>
	</fr:view>
	
	<!-- ======================
	         tree controls
	     ======================  -->
	
	<div id="tree-controls" style="display: none;">
	    <p>
	        <a href="#" onclick="<%= "treeRenderer_expandAll('" + tree + "');" %>"><bean:message key="link.tree.expand-all" bundle="FUNCTIONALITY_RESOURCES"/></a>
	       | <a href="#" onclick="<%= "treeRenderer_collapseAll('" + tree + "');" %>"><bean:message key="link.tree.collapse-all" bundle="FUNCTIONALITY_RESOURCES"/></a>
	   </p>
	   
	   <fr:form action="<%= "/module/view.do?module=" + thisModule %>">
	       <!-- submits the form on top of the page, search for: tree-structure -->
	       <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('" + tree + "');" %>">
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
</logic:notEmpty>

<logic:empty name="functionalities">
	<p class="mvert1"><em><bean:message key="tree.unavailable" bundle="FUNCTIONALITY_RESOURCES"/></em></p>
</logic:empty>
