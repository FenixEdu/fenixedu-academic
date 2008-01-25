<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<jsp:include page="siteQuota.jsp"/>

<h2>
    <bean:message key="link.sectionsManagement" bundle="SITE_RESOURCES"/>
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
			<bean:message key="link.createSection" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>
</p>

<logic:empty name="site" property="orderedTopLevelSections">
    <p>
        <span class="warning0">
            <bean:message key="message.sections.empty" bundle="SITE_RESOURCES"/>
        </span>
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
                <fr:property name="childrenFor(Section)" value="childrenAsContent"/>
                <fr:property name="schemaFor(Section)" value="site.section.name"/>
                <fr:property name="imageFor(Section)" value="/images/site/section.gif"/>

				<fr:property name="schemaFor(Functionality)" value="content.in.tree"/>
				<fr:property name="imageFor(Functionality)" value="/images/site/institutionalSection.gif"/>

				<fr:property name="schemaFor(Item)" value="content.in.tree"/>
				<fr:property name="imageFor(Item)" value="/images/site/section.gif"/>
                <fr:property name="childrenFor(Item)" value="childrenAsContent"/>

				<fr:property name="schemaFor(Attachment)" value="content.in.tree"/>
				<fr:property name="imageFor(Attachment)" value="/images/functionalities/sheet.gif"/>
				
				<fr:property name="schemaFor(Forum)" value="content.in.tree"/>
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

<!-- Functionalities -->

<h3 class="mtop15 separator2"><bean:message key="title.section.institutionalContents" bundle="SITE_RESOURCES"/></h3>

<logic:equal name="site" property="templateAvailable" value="true">
<logic:equal name="site" property="template.contentPoolAvailable" value="true">
	<ul>
		<li>
			<html:link page="<%= actionName + "?method=prepareAddFromPool&amp;" + context %>">
				<bean:message key="link.institutionSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</logic:equal>
</logic:equal>
	
<logic:empty name="site" property="associatedFunctionalities">
	<p><em><bean:message key="label.noInstitutionalContents" bundle="SITE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="site" property="associatedFunctionalities">
	<bean:define id="containerID" name="site" property="idInternal"/>
	<logic:iterate id="functionality" name="site" property="associatedFunctionalities">
			<bean:define id="contentID" name="functionality" property="idInternal"/>

			<div class="mtop15 mbottom0" style="background: #fafafa; padding: 0.5em;">
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

				<p class="mtop05 mbottom0">
		        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 				
			        <html:link action="<%=  actionName + "?method=removeFunctionalityFromContainer&amp;" + context + "&amp;contentID=" + contentID + "&amp;containerID=" + containerID %>">
						<bean:message key="messaging.delete.label" bundle="WEBSITEMANAGER_RESOURCES"/>
					 </html:link>
				 </p>
			 </div>
			
	</logic:iterate>
</logic:notEmpty>


</logic:notEmpty>
