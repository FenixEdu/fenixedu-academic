<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<jsp:include page="context.jsp"/>

<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

<jsp:include page="/commons/sites/siteQuota.jsp"/>

<h2>
	<bean:message key="label.executionCourseManagement.menu.sections"/>
</h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="CONTENT_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages"/></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<p>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=createSection&amp;" + context %>">
			<bean:message key="link.createSection"/>
		</html:link>
	</span>
	
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= "/manageExecutionCourse.do?method=prepareImportSections&amp;" + context %>">
			<bean:message key="label.import.sections"/>
		</html:link>
	</span>
</p>

<logic:empty name="site" property="directChildrenAsContent">
    <p class="mvert15">
        <em>
            <bean:message key="message.sections.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>

<logic:notEmpty name="site" property="directChildrenAsContent">
    <fr:form action="<%= actionName + "?method=saveSectionsOrder&amp;" + context %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "sectionsTree." + contextParam + "." + contextParamValue; %>
    
    <div class="section1">
        <fr:view name="site" property="directChildrenAsContent">
            <fr:layout name="tree">
                <fr:property name="treeId" value="<%= treeId %>"/>
                <fr:property name="fieldId" value="sections-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(Section)" value="site.section.name"/>
                <fr:property name="childrenFor(Section)" value="directChildrenAsContent"/>
				<fr:property name="schemaFor(Item)" value="site.item.name"/>
                <fr:property name="childrenFor(Item)" value="directChildrenAsContent"/>
				<fr:property name="schemaFor(Attachment)" value="content.in.tree"/>
      		    <fr:property name="movedClass" value="highlight3"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= actionName + "?method=section&amp;sectionID=${idInternal}&amp;" + context %>"/>
        </fr:view>
	
		<p class="mtop15">
	    <fr:form action="<%= actionName + "?method=sections&amp;" + context %>">
	        <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('" + treeId + "');" %>">
	            <bean:message key="button.sections.order.save" bundle="SITE_RESOURCES"/>
	        </html:button>
	        <html:submit>
	            <bean:message key="button.sections.order.reset" bundle="SITE_RESOURCES"/>
	        </html:submit>
	    </fr:form>
	    </p>
    </div>
    
<p style="color: #888;">
	<em><bean:message key="message.section.reorder.tip" bundle="SITE_RESOURCES"/></em>
</p>

</logic:notEmpty>

<!-- Functionalities -->

<h3 class="mtop15 separator2"><bean:message key="title.section.institutionalContents" bundle="SITE_RESOURCES"/></h3>

<logic:equal name="site" property="template.contentPoolAvailable" value="true">
	<ul>
		<li>
			<html:link page="<%= actionName + "?method=prepareAddFromPool&amp;" + context %>">
				<bean:message key="link.institutionSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</logic:equal>
		
<logic:empty name="site" property="associatedFunctionalities">
	<em><bean:message key="label.noInstitutionalContents" bundle="SITE_RESOURCES"/></em>
</logic:empty>

<logic:notEmpty name="site" property="associatedFunctionalities">
	<bean:define id="containerID" name="site" property="idInternal"/>
	<logic:iterate id="functionality" name="site" property="associatedFunctionalities">
			<bean:define id="contentID" name="functionality" property="idInternal"/>

			<div class="mtop15 mbottom0" style="background: #fafafa; padding: 0.5em;">
			<p>
			<strong><fr:view name="functionality" property="name"/></strong>
				<span style="color: #888; padding-left: 1em;">
	                <bean:message key="label.item.availableFor" bundle="SITE_RESOURCES"/>:
	                <fr:view name="functionality" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
	                    <fr:layout>
	                        <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
	                        <fr:property name="key" value="true"/>
	                        <fr:property name="bundle" value="SITE_RESOURCES"/>
	                        <fr:property name="subLayout" value="values"/>
	                        <fr:property name="subSchema" value="permittedGroup.class.text"/>
	                    </fr:layout>
	                </fr:view>
	            </span>

				<p>
		        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 				
			        <html:link action="<%=  actionName + "?method=removeFunctionalityFromContainer&amp;" + context + "&amp;contentID=" + contentID + "&amp;containerID=" + containerID %>">
						<bean:message key="messaging.delete.label" bundle="WEBSITEMANAGER_RESOURCES"/>
					 </html:link>
				 </p>
			 </p>
			 </div>
			
	</logic:iterate>
</logic:notEmpty>

