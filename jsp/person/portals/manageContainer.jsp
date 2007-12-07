<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<html:xhtml/>

<h2><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></h2>

<bean:define id="cid" name="content" property="idInternal"/>

<bean:define id="contentClass" name="content" property="class.simpleName"/>

<fr:view name="content">
	<fr:layout name="manage-content-bread-crumbs">
		<fr:property name="linkFor(Section)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(Portal)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(Functionality)" value="/contentManagement.do?method=viewElement&contentId=${idInternal}"/>
	</fr:layout>
</fr:view>

<fr:view name="content" schema="<%= "view.contents.details."  + contentClass %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:view>

<fr:view name="content" schema="content.view.availability">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright mtop05 mbottom025"/>
	</fr:layout>
</fr:view>

 <bean:message key="label.availableOperations" bundle="CONTENT_RESOURCES"/>:
 
<html:link page="<%= "/contentManagement.do?method=editContent&amp;contentId=" + cid %>">
	<bean:message key="label.edit"/>
</html:link>,
<html:link page="<%= "/contentManagement.do?method=prepareAddPortal&amp;contentId=" + cid %>">
	<bean:message key="add.portal" bundle="CONTENT_RESOURCES"/>
</html:link>,
<html:link page="<%= "/contentManagement.do?method=prepareCreateSection&amp;contentId=" + cid %>">
	<bean:message key="create.section" bundle="CONTENT_RESOURCES"/>
</html:link>,
<html:link page="<%= "/contentManagement.do?method=prepareAddFunctionality&amp;contentId=" + cid %>" >
	<bean:message key="add.functionality" bundle="CONTENT_RESOURCES"/>
</html:link>
<html:link page="<%= "/contentManagement.do?method=prepareEditAvailabilityPolicy&amp;contentId=" + cid %>" >
	<bean:message key="edit.availability.policy" bundle="CONTENT_RESOURCES"/>
</html:link>

<logic:notEmpty name="parentContainer">
	<bean:define id="parentId" name="parentContainer" property="idInternal"/>
	,
	<html:link page="<%= "/contentManagement.do?method=deleteContent&amp;contentId=" + cid + "&amp;contentParentId=" + parentId %>" >
		<bean:message key="delete.content" bundle="CONTENT_RESOURCES"/>
	</html:link>
</logic:notEmpty>

<logic:equal name="content" property="ableToSpecifyInitialContent" value="true">
		,
		<html:link page="<%= "/contentManagement.do?method=addInitialContentToSection&amp;contentId=" + cid %>">
			<bean:message key="label.add.initial.content" bundle="CONTENT_RESOURCES"/>
		</html:link>
</logic:equal>

<logic:equal name="content" property="class.simpleName" value="MetaDomainObjectPortal">
		,
		<html:link page="<%= "/contentManagement.do?method=managePool&amp;contentId=" + cid %>">
			<bean:message key="label.manage.pool.content" bundle="CONTENT_RESOURCES"/>
		</html:link>
</logic:equal>

<html:link page="<%= "/contentManagement.do?method=activateLogging&amp;contentId=" + cid %>">
	<bean:message key="label.activate.logging" bundle="CONTENT_RESOURCES"/>
</html:link>
<html:link page="<%= "/contentManagement.do?method=deactivateLogging&amp;contentId=" + cid %>">
	<bean:message key="label.deactivate.logging" bundle="CONTENT_RESOURCES"/>
</html:link>

<fr:form action="<%= "/contentManagement.do?method=organizeStructure&amp;contentId=" + cid %>">
	    <input alt="input.tree" id="tree-structure" type="hidden" name="tree" value="" />
</fr:form>
	
<bean:define id="treeID" value="contentTree"/>
	
<p>
<fr:view name="content">
	<fr:layout name="content-tree">

            <fr:property name="treeId" value="<%= treeID %>"/>
	        <fr:property name="fieldId" value="tree-structure"/>
		    <fr:property name="eachLayout" value="values"/>

		    <fr:property name="schemaFor(Section)" value="site.section.name"/>
		    <fr:property name="childrenFor(Section)" value="childrenAsContent"/>
	    
		   <fr:property name="schemaFor(Portal)" value="contents.portal.tree"/>
		   <fr:property name="childrenFor(Portal)" value="childrenAsContent"/>		   			

		   <fr:property name="schemaFor(MetaDomainObjectPortal)" value="contents.portal.tree"/>
   		   <fr:property name="childrenFor(MetaDomainObjectPortal)" value="childrenAsContent"/>

			<fr:property name="schemaFor(Functionality)" value="functionalities.functionality.tree"/>


   		    <fr:property name="movedClass" value="highlight3"/>
			<fr:property name="parentParameterName" value="contentParentId"/>
	</fr:layout>
	<fr:destination name="section.view" path="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
	<fr:destination name="portal.view" path="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
	<fr:destination name="functionality.view" path="/contentManagement.do?method=viewElement&contentId=${idInternal}"/>
</fr:view>

	<div id="tree-controls" style="display: none;">

	    <p>
	        <a href="#" onclick="<%= "treeRenderer_expandAll('" + treeID + "');" %>"><bean:message key="link.tree.expand-all" bundle="CONTENT_RESOURCES"/></a>
	       | <a href="#" onclick="<%= "treeRenderer_collapseAll('" + treeID + "');" %>"><bean:message key="link.tree.collapse-all" bundle="CONTENT_RESOURCES"/></a>
	   </p>
	   
 	   <fr:form action="<%= "/contentManagement.do?method=organizeStructure&amp;containerId=" + cid %>">
	       <!-- submits the form on top of the page, search for: tree-structure -->
	       <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('" + treeID + "');" %>">
	           <bean:message key="button.tree.save" bundle="CONTENT_RESOURCES"/>
	       </html:button>	   
	       <html:submit>
	           <bean:message key="button.tree.reset" bundle="CONTENT_RESOURCES"/>
	       </html:submit>
	   </fr:form>

	</div>
	
	<script type="text/javascript">
	   // show hidden div
	   document.getElementById("tree-controls").style.display = 'block';
	</script>
	
</p>