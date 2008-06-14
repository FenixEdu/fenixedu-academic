<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteId" name="site" property="idInternal"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<jsp:include page="/commons/sites/siteQuota.jsp"/>

<h2>
	<bean:message key="label.executionCourseManagement.menu.sections"/>
</h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.sections.top.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<a href="#" onclick="javascript: switchDisplay('help_box')" style="float: right; border: none;"><img src="<%= request.getContextPath() %>/images/icon_help.gif"/></a>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="CONTENT_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
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
		<html:link page="<%= actionName + "?method=chooseIntroductionSections&amp;" + context %>">
			<bean:message key="link.introductionSections.choose" bundle="WEBSITEMANAGER_RESOURCES"/>
		</html:link>
	</span>
	
	<logic:equal name="site" property="templateAvailable" value="true">
		<logic:equal name="site" property="template.contentPoolAvailable" value="true">
			<span class="pleft1">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= actionName + "?method=prepareAddFromPool&amp;" + context %>">
							<bean:message key="link.institutionSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
				</html:link>
			</span>
		</logic:equal>
	</logic:equal>
</p>

<logic:empty name="site" property="directChildrenAsContent">
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
    
   	<div id="help_box" class="dblock">
	    	<p class="mbottom05"><em><bean:message key="label.subtitle" bundle="SITE_RESOURCES"/>:</em></p>
	    	<ul class="nobullet" style="padding-left: 0; margin-left: 1em;">
	    	<li><img src="<%= request.getContextPath() %>/images/icon-section.gif"/> <em><bean:message key="label.section" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.section.description" bundle="SITE_RESOURCES"/></li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-item.gif"/> <em><bean:message key="label.item" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.item.description" bundle="SITE_RESOURCES"/> </li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-attachment.gif"/> <em><bean:message key="label.file" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.file.description" bundle="SITE_RESOURCES"/> </li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-forum.gif"/> <em><bean:message key="label.foruns" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.foruns.description" bundle="SITE_RESOURCES"/></li>
			<li><img src="<%= request.getContextPath() %>/images/icon-institutional.gif"/> <em><bean:message key="label.institutionalContent" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.institutionalContent.descripton" bundle="SITE_RESOURCES"/></li>
	   		</ul>
    </div>
    <script type="text/javascript"> hideElement('help_box'); </script>
    
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
				<fr:property name="schemaFor(Forum)" value="content.in.tree"/>
				<fr:property name="schemaFor(Functionality)" value="site.functionality.name"/>
      		    <fr:property name="movedClass" value="highlight3"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= actionName + "?method=section&amp;sectionID=${idInternal}&amp;" + context %>"/>
            <fr:destination name="item.view" path="<%= actionName + "?method=section&sectionID=${section.idInternal}&" + context  + "#item-${idInternal}"%>"/>
            <fr:destination name="functionality.view" path="<%= actionName + "?method=functionality&siteID=" + siteId + "&functionalityID=${idInternal}&" + context  + "#content-${idInternal}"%>"/>
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

<logic:equal name="site" property="templateAvailable" value="true">
<logic:equal name="site" property="template.contentPoolAvailable" value="true">
<h3 class="mtop15 separator2"><bean:message key="title.section.institutionalContents" bundle="SITE_RESOURCES"/></h3>

	<ul class="mbottom2 list5" style="list-style: none;">
		<li>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= actionName + "?method=prepareAddFromPool&amp;" + context %>">
							<bean:message key="link.institutionSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>

<logic:empty name="site" property="associatedFunctionalities">
	<p><em><bean:message key="label.site.noInstitutionalContents" bundle="SITE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="site" property="associatedFunctionalities">
	<bean:define id="containerID" name="site" property="idInternal"/>
	<logic:iterate id="functionality" name="site" property="associatedFunctionalities">
			<bean:define id="contentID" name="functionality" property="idInternal"/>

			<div id="content-<%= contentID%>" class="mtop15 mbottom0" style="background: #f5f5f5; padding: 0.5em;">
			<strong><fr:view name="functionality" property="name"/></strong>
					
				<span style="color: #888; padding-left: 0.75em;">
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
				<span>
    					<html:link action="<%=  actionName + "?method=removeFunctionalityFromContainer&amp;" + context + "&amp;contentID=" + contentID + "&amp;containerID=" + containerID %>">
								<bean:message key="link.delete" bundle="SITE_RESOURCES"/>
			 			</html:link>
				</span>
				| 
			
				<app:defineContentPath id="url" name="functionality"/>
					<bean:define id="url" name="url" type="java.lang.String"/>
					<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a  target="_blank" href="<%= request.getContextPath() + url %>">
					<bean:message key="link.view" bundle="SITE_RESOURCES"/> »
					</a>
				</p>
			 </div>
			
	</logic:iterate>
</logic:notEmpty>
</logic:equal>
</logic:equal>

