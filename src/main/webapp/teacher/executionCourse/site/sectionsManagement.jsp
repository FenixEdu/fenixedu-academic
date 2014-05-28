<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>


<html:xhtml/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>
<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteId" name="site" property="externalId"/>

<bean:define id="executionCourseId" name="executionCourse" property="externalId"/>

<jsp:include page="/commons/sites/siteQuota.jsp"/>

<h2>
	<bean:message key="label.executionCourseManagement.menu.sections"/>
</h2>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<a href="#" onclick="javascript: switchDisplay('help_box')" style="float: right; border: none;"><img src="<%= request.getContextPath() %>/images/icon_help.gif"/></a>
	
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
	
	<logic:equal name="site" property="templateAvailable" value="true">
			<span class="pleft1">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= actionName + "?method=prepareAddFromPool&amp;" + context %>">
							<bean:message key="link.institutionSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
				</html:link>
			</span>
	</logic:equal>
	
</p>

<logic:empty name="site" property="associatedSectionSet">
    <p class="mvert15">
        <em>
            <bean:message key="message.sections.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>

	<div id="help_box" class="dblock">
	    	<p class="mbottom05"><em><bean:message key="label.subtitle" bundle="SITE_RESOURCES"/>:</em></p>
	    	<ul class="nobullet" style="padding-left: 0; margin-left: 1em;">
	    	<li><img src="<%= request.getContextPath() %>/images/icon-section.gif"/> <em><bean:message key="label.section" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.section.description" bundle="SITE_RESOURCES"/></li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-item.gif"/> <em><bean:message key="label.item" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.item.description" bundle="SITE_RESOURCES"/> </li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-attachment.gif"/> <em><bean:message key="label.file" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.file.description" bundle="SITE_RESOURCES"/> </li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-forum.gif"/> <em><bean:message key="label.forums" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.forums.description" bundle="SITE_RESOURCES"/></li>
			<li><img src="<%= request.getContextPath() %>/images/icon-institutional.gif"/> <em><bean:message key="label.institutionalContent" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.institutionalContent.descripton" bundle="SITE_RESOURCES"/></li>
	   		</ul>
    </div>
    <script type="text/javascript"> hideElement('help_box'); </script>

<logic:notEmpty name="site" property="associatedSectionSet">
    <fr:form action="<%= actionName + "?method=saveSectionsOrder&amp;" + context %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "sectionsTree." + contextParam + "." + contextParamValue; %>
    
    <div class="section1">
        <fr:view name="site" property="orderedAssociatedSections">
            <fr:layout name="tree">
                <fr:property name="treeId" value="<%= treeId %>"/>
                <fr:property name="fieldId" value="sections-order"/>
                
	             <fr:property name="eachLayout" value="values"/>
                <fr:property name="childrenFor(Section)" value="everythingForTree"/>
                <fr:property name="schemaFor(Section)" value="site.section.name"/>

				<fr:property name="schemaFor(TemplatedSectionInstance)" value="site.template.name"/>
				<fr:property name="imageFor(TemplatedSectionInstance)" value="/images/icon-institutional.gif"/>

				<fr:property name="schemaFor(Item)" value="site.item.name"/>
                <fr:property name="childrenFor(Item)" value="fileContentSet"/>

				<fr:property name="schemaFor(FileContent)" value="item.file.filename"/>
				<fr:property name="imageFor(FileContent)" value="/images/icon-attachment.gif"/>
				
				<fr:property name="schemaFor(Forum)" value="content.in.tree"/>
				
      		    <fr:property name="movedClass" value="highlight3"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= actionName + "?method=section&amp;sectionID=${externalId}&amp;" + context %>"/>
            <fr:destination name="item.view" path="<%= actionName + "?method=section&sectionID=${section.externalId}&" + context  + "#item-${externalId}"%>"/>
            <fr:destination name="functionality.view" path="<%= actionName + "?method=sections&siteID=" + siteId + "&functionalityID=${externalId}&" + context  + "#content-${externalId}"%>"/>
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
<h3 class="mtop15 separator2"><bean:message key="title.section.institutionalContents" bundle="SITE_RESOURCES"/></h3>

	<ul class="mbottom2 list5" style="list-style: none;">
		<li>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= actionName + "?method=prepareAddFromPool&amp;" + context %>">
							<bean:message key="link.institutionSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>

<logic:empty name="site" property="templatedSectionInstances">
	<p><em><bean:message key="label.site.noInstitutionalContents" bundle="SITE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="site" property="templatedSectionInstances">
	<bean:define id="containerID" name="site" property="externalId"/>
	<logic:iterate id="functionality" name="site" property="templatedSectionInstances">
			<bean:define id="contentID" name="functionality" property="externalId"/>

			<div id="content-<%= contentID%>" class="mtop15 mbottom0" style="background: #f5f5f5; padding: 0.5em;">
			<strong><fr:view name="functionality" property="name"/></strong>
					
				<span style="color: #888; padding-left: 0.75em;">
	                <bean:message key="label.item.availableFor" bundle="SITE_RESOURCES"/>:
	                <fr:view name="functionality" property="permittedGroup" layout="null-as-label" type="org.fenixedu.bennu.core.groups.Group">
	                    <fr:layout>
	                        <fr:property name="label" value="label.public"/>
	                        <fr:property name="key" value="true"/>
	                        <fr:property name="bundle" value="SITE_RESOURCES"/>
	                        <fr:property name="subLayout" value="values"/>
	                        <fr:property name="subSchema" value="permittedGroup.name"/>
	                    </fr:layout>
	                </fr:view>
	            </span>

				<p>
				<span>
    					<html:link action="<%=  actionName + "?method=deleteSection&amp;" + context + "&amp;sectionID=" + contentID + "&amp;containerID=" + containerID %>">
								<bean:message key="link.delete" bundle="SITE_RESOURCES"/>
			 			</html:link>
				</span>
				| 
			

					<a  target="_blank" href="${functionality.fullPath}">
					<bean:message key="link.view" bundle="SITE_RESOURCES"/> »
					</a>
				</p>
			 </div>
			
	</logic:iterate>
</logic:notEmpty>
</logic:equal>


