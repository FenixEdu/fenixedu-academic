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

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteId" name="site" property="externalId"/>
<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="sectionId" name="section" property="externalId"/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp"/>
<jsp:include page="siteQuota.jsp"/>

<h2>
	<bean:message key="label.section" bundle="SITE_RESOURCES"/>
	<fr:view name="section" property="name" />
</h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="CONTENT_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<a href="#" onclick="javascript: switchDisplay('help_box')" style="float: right; border: none;"><img src="<%= request.getContextPath() %>/images/icon_help.gif"/></a>

<div id="breadcrumbs" class="mvert1">
    <html:link page="<%= String.format("%s?method=sections&amp;%s", actionName, context) %>">
        <bean:message key="link.breadCrumbs.top" bundle="SITE_RESOURCES"/>
    </html:link> &gt;
    
    <logic:iterate id="crumb" name="sectionBreadCrumbs">
        <html:link page="<%= String.format("%s?method=section&amp;%s", actionName, context) %>" paramId="sectionID" paramName="crumb" paramProperty="externalId">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <fr:view name="section" property="name"/>
</div>

 <bean:define id="deleteSectionId" value="<%= "deleteSectionForm" + sectionId %>"/>

<p class="mvert1">
    <span class="error">
        <html:errors property="section" bundle="APPLICATION_RESOURCES"/>
    </span>
</p>


<%--
<logic:empty name="section" property="orderedSubSections">
    <p class="mtop2">
        <em>
            <bean:message key="message.subSections.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>
--%>

	<div id="help_box" class="dnone">
	    	<p class="mbottom05"><em><bean:message key="label.subtitle" bundle="SITE_RESOURCES"/>:</em></p>
	    	<ul class="nobullet" style="padding-left: 0; margin-left: 1em;">
	    	<li><img src="<%= request.getContextPath() %>/images/icon-section.gif"/> <em><bean:message key="label.section" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.section.description" bundle="SITE_RESOURCES"/></li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-item.gif"/> <em><bean:message key="label.item" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.item.description" bundle="SITE_RESOURCES"/> </li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-attachment.gif"/> <em><bean:message key="label.file" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.file.description" bundle="SITE_RESOURCES"/> </li>
	    	<li><img src="<%= request.getContextPath() %>/images/icon-forum.gif"/> <em><bean:message key="label.forums" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.forums.description" bundle="SITE_RESOURCES"/></li>
			<li><img src="<%= request.getContextPath() %>/images/icon-institutional.gif"/> <em><bean:message key="label.institutionalContent" bundle="SITE_RESOURCES"/>:</em> <bean:message key="label.institutionalContent.descripton" bundle="SITE_RESOURCES"/></li>
	   		</ul>
	</div>

<logic:notEmpty name="site" property="associatedSectionSet">
    <fr:form action="<%= actionName + "?method=saveSectionsOrder&amp;" + context + "&amp;sectionID=" + sectionId %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "subSectionTree" + site.getExternalId(); %>
            
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
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
				<fr:property name="imageFor(FileConten)" value="/images/icon-attachment.gif"/>

                <fr:property name="current" value="<%= sectionId.toString() %>"/>
                <fr:property name="currentClasses" value="highlight1"/>
                <fr:property name="movedClass" value="highlight3"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= actionName + "?method=section&sectionID=${externalId}&" + context %>"/>
            <fr:destination name="item.view" path="<%= actionName + "?method=section&sectionID=${section.externalId}&" + context  + "#item-${externalId}"%>"/>
        	<fr:destination name="functionality.view" path="<%= actionName + "?method=section&siteID=" + siteId + "&sectionID=${section.externalId}&" + context  + "#content-${externalId}"%>"/>
        </fr:view>

		<p class="mtop15">
		    <fr:form action="<%= actionName + "?method=section&amp;" + context + "&amp;sectionID=" + sectionId %>">
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


<div class="infoop2">
	<p>
         <span class="anchorcaaa">
             <strong><bean:message key="label.section" bundle="SITE_RESOURCES"/>: <fr:view name="section" property="name"/></strong>
         </span>
		
		<span style="color: rgb(136, 136, 136); padding-left: 0.75em;">
            <bean:message key="label.item.availableFor" bundle="SITE_RESOURCES"/>:
            <fr:view name="section" property="permittedGroup" layout="null-as-label" type="org.fenixedu.bennu.core.groups.Group">
                <fr:layout>
                    <fr:property name="label" value="label.public"/>
                    <fr:property name="key" value="true"/>
                    <fr:property name="bundle" value="SITE_RESOURCES"/>
                    <fr:property name="subLayout" value="values"/>
                    <fr:property name="subSchema" value="permittedGroup.name"/>
                </fr:layout>
            </fr:view>    
        </span>
          </p>


<div id="<%= deleteSectionId %>" class="dnone mvert05">
    <fr:form action="<%= String.format("%s?method=confirmSectionDelete&amp;%s&amp;sectionID=%s&amp;confirm=true", actionName, context, sectionId) %>">
        <p class="" style="padding: 0.5em 0;">
        <span>
            <logic:equal name="section" property="deletable" value="true">
                <bean:message key="message.section.delete.confirm" bundle="SITE_RESOURCES"/>
            </logic:equal>
            <logic:notEqual name="section" property="deletable" value="true">
                <bean:message key="message.section.delete.notAvailable" bundle="SITE_RESOURCES"/>
            </logic:notEqual>
        </span>                
        
        <logic:equal name="section" property="deletable" value="true">
            <html:submit>
                <bean:message key="button.confirm" bundle="SITE_RESOURCES"/>
            </html:submit>
        </logic:equal>
        <html:button bundle="HTMLALT_RESOURCES" altKey="button.hide" property="hide" onclick="<%= String.format("javascript:hideElement('%s');", deleteSectionId) %>">
            <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
        </html:button>
        </p>
    </fr:form>
</div>



    <p>
        
		<span>
			<html:link page="<%= String.format("%s?method=editSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
				<bean:message key="link.edit" bundle="SITE_RESOURCES"/>
			</html:link>
		</span>
		|
		  <span class="switchNoneStyle">
        	<span>	
        		<html:link page="<%= String.format("%s?method=deleteSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
        			<bean:message key="link.delete" bundle="SITE_RESOURCES"/>
        		</html:link>
        	</span>
	    </span>

	    <span class="switchInline">
    	    <span>
	            <html:link href="#" onclick="<%= String.format("showElement('%s');", deleteSectionId) %>">
	                <bean:message key="link.delete" bundle="SITE_RESOURCES"/>
	            </html:link>
	        </span>
    	</span>
		|

		<span>
    		<html:link page="<%= String.format("%s?method=editSectionPermissions&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
    		    <bean:message key="link.permissions" bundle="SITE_RESOURCES"/>
    		</html:link>
    	</span>
		|

		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a  target="_blank" href="${section.fullPath}">
			<bean:message key="link.view" bundle="SITE_RESOURCES"/> »
		</a>
        
    </p>
</div>

<p>
	<bean:message key="label.selectContentToInsert" bundle="SITE_RESOURCES"/>: 
   	<logic:equal name="section" property="itemAllowed" value="true">    	
    	<span>
    		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= String.format("%s?method=createItem&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
                <bean:message key="label.item" bundle="SITE_RESOURCES"/>
            </html:link>
    	</span>
   	</logic:equal>
   	<span class="pleft05">
   		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
   		<html:link page="<%= String.format("%s?method=createSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
   			<bean:message key="label.subSection" bundle="SITE_RESOURCES"/>
   		</html:link>
   	</span>
	<span class="pleft05">
    		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= String.format("%s?method=prepareAddFromPool&amp;sectionID=%s&amp;%s", actionName, sectionId, context) %>">
				<bean:message key="label.institutionalContent" bundle="SITE_RESOURCES"/>
			</html:link>
    </span>
    <span class="pleft05">
    		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= String.format("%s?method=uploadFile&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
				<bean:message key="link.insertFile" bundle="SITE_RESOURCES"/>
			</html:link>
	</span>
</p>
<%-------------
     Items
  -------------%>

<h3 class="mtop15 separator2"><bean:message key="title.section.items" bundle="SITE_RESOURCES"/></h3>

<p>
	<span class="error0">
	    <html:errors property="items" bundle="SITE_RESOURCES"/>
	</span>
</p>

<logic:equal name="section" property="itemAllowed" value="true">
	<ul class="mbottom2 list5" style="list-style: none;">
		<li>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="<%= String.format("%s?method=createItem&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
	                <bean:message key="link.insertItem" bundle="SITE_RESOURCES"/>
	           </html:link> 
		</li>
	</ul>
</logic:equal>

<logic:empty name="section" property="childrenItems">
    <p>
        <em>
            <bean:message key="message.section.items.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>

<logic:notEmpty name="section" property="childrenItems">
	<logic:iterate id="item" name="section" property="orderedChildItems" type="net.sourceforge.fenixedu.domain.Item">

        <bean:define id="itemId" name="item" property="externalId"/>
		
		<div id="item-<%= itemId %>">
		<div class="mtop15 mbottom0" style="background: #f5f5f5; padding: 0.5em;">
		
			<p class="mtop0 mbottom05">
				<%--<span style="color: #888;"><bean:message key="label.item"/></span><br/>--%>
				<span style="float: right;"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#breadcrumbs"><bean:message key="label.top" bundle="SITE_RESOURCES"/></a></span>
				<strong><fr:view name="item" property="name"/></strong>
	            
	            <span style="color: #888; padding-left: 0.75em;">
	                <bean:message key="label.item.availableFor" bundle="SITE_RESOURCES"/>:
	                <fr:view name="item" property="permittedGroup" layout="null-as-label" type="org.fenixedu.bennu.core.groups.Group">
	                    <fr:layout>
	                        <fr:property name="label" value="label.public"/>
	                        <fr:property name="key" value="true"/>
	                        <fr:property name="bundle" value="SITE_RESOURCES"/>
	                        <fr:property name="subLayout" value="values"/>
	                        <fr:property name="subSchema" value="permittedGroup.name"/>
	                    </fr:layout>
	                </fr:view>
	            </span>
			</p>
	        
	        <span>
				<html:link page="<%= String.format("%s?method=editItem&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
					<bean:message key="link.edit" bundle="SITE_RESOURCES"/>
				</html:link>
			</span>
	
			|
	        <bean:define id="deleteUrl" type="java.lang.String" value="<%= String.format("%s?method=deleteItem&amp;%s&amp;itemID=%s", actionName, context, itemId) %>"/>
	        <bean:define id="deleteId" value="<%= "deleteForm" + item.getExternalId() %>"/>
	
	        <span class="switchNoneStyle">
	        	   <span> 
		       		<html:link page="<%= deleteUrl %>">
		       			<bean:message key="link.delete" bundle="SITE_RESOURCES"/>
		       		</html:link>
		       	</span>
	        </span>
	
	        <span class="switchInline">
		     	<span>
		            <html:link href="#" onclick="<%= String.format("javascript:showElement('%s');", deleteId) %>">
		                <bean:message key="link.delete" bundle="SITE_RESOURCES"/>
		            </html:link>
				</span>
	        </span>
	
			|
			<span>
		        <html:link page="<%= String.format("%s?method=editItemPermissions&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
		            <bean:message key="link.permissions" bundle="SITE_RESOURCES"/>
		        </html:link>
	        </span>
	
			|
			<span>
				<html:link page="<%= String.format("%s?method=uploadFile&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
					<bean:message key="link.insertFile" bundle="SITE_RESOURCES"/>
				</html:link>
			</span>
							
			|
	        		<span>
		               <a href="${item.fullPath}" target="_blank">
		               	<bean:message key="link.view" bundle="SITE_RESOURCES"/> »
		               </a>
	               </span>
				
       		        
	        <div id="<%= deleteId %>" class="dnone mvert05">
	            <fr:form action="<%= deleteUrl %>">
	            	<p class="mvert1">
	                <span>
	                    <logic:equal name="item" property="deletable" value="true">
	                        <bean:message key="message.item.delete.confirm" bundle="SITE_RESOURCES"/>
	                    </logic:equal>
	                    <logic:notEqual name="item" property="deletable" value="true">
	                        <bean:message key="message.item.delete.notAvailable" bundle="SITE_RESOURCES"/>
	                    </logic:notEqual>
	                </span>                
	                
	                <logic:equal name="item" property="deletable" value="true">
	                    <html:submit>
	                        <bean:message key="button.confirm" bundle="SITE_RESOURCES"/>
	                    </html:submit>
	                </logic:equal>
	                <html:button bundle="HTMLALT_RESOURCES" altKey="button.hide" property="hide" onclick="<%= String.format("javascript:hideElement('%s');", deleteId) %>">
	                    <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
	                </html:button>
	                </p>
	            </fr:form>
	        </div>
	        
		</div>



  
        <div class="mtop1" style="background: #fff; border: 1px dotted #ccc; padding: 1em;">
		
            <div>
               	<fr:view name="item" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html">
               	</fr:view>
            </div>
    
            <logic:notEmpty name="item" property="fileContentSet">
                <div class="mtop2">
                
                    <strong><bean:message key="label.files" bundle="SITE_RESOURCES"/>:</strong>
                    
                    	<table class="tstyle2 thlight tdcenter width100">
                    		<tr>
	                    		<th><bean:message key="label.name" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.file" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.section.item.file.availability" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.section.item.file.options" bundle="SITE_RESOURCES"/></th>
                    		</tr>
                        	<logic:iterate id="fileItem" name="item" property="fileContentSet" type="net.sourceforge.fenixedu.domain.FileContent">
							<tr>
								<td>
	                        		<bean:define id="downloadUrl">
	                        			<bean:write name="fileItem" property="downloadUrl"/>
	                        		</bean:define>
           	                		<html:link href="<%= downloadUrl %>">
           	                			<fr:view name="fileItem" property="displayName"/>
           	                		</html:link>
            	    		        <bean:define id="message">
        	                            <bean:message key="message.item.file.delete.confirm" bundle="SITE_RESOURCES" arg0="<%= fileItem.getDisplayName() %>"/>
        	                        </bean:define>
           	                	</td>
            	                <td> <bean:write name="fileItem" property="filename"/></td>	
            					
            					
            					<td>
	                                <span class="pleft1" style="color: #888;">
	                                    <bean:message key="label.item.file.availableFor" bundle="SITE_RESOURCES"/>:
	                                    <fr:view name="fileItem" property="permittedGroup" layout="null-as-label" type="org.fenixedu.bennu.core.groups.Group">
	                                        <fr:layout>
	                                            <fr:property name="label" value="label.public"/>
	                                            <fr:property name="key" value="true"/>
	                                            <fr:property name="bundle" value="SITE_RESOURCES"/>
	                                            <fr:property name="subLayout" value="values"/>
	                                            <fr:property name="subSchema" value="permittedGroup.name"/>
	                                        </fr:layout>
	                                    </fr:view>
	                                </span>
       	    	                </td>
       	    	                
            					<td class="nowrap">	
        							<span class="pleft1">
           		                		<html:link page="<%= String.format("%s?method=deleteFile&amp;%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", actionName, context, sectionId, itemId, fileItem.getExternalId()) %>"
           		                                   onclick="<%= String.format("return confirm('%s')", message) %>">
           			                        <bean:message key="link.delete" bundle="SITE_RESOURCES"/>
           			                    </html:link> |
           			                    <html:link page="<%= String.format("%s?method=editDisplayName&amp;%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", actionName, context, sectionId, itemId, fileItem.getExternalId()) %>">
           			                        <bean:message key="link.edit" bundle="SITE_RESOURCES"/>
           			                    </html:link> | 
           			                    <html:link page="<%= String.format("%s?method=prepareEditItemFilePermissions&amp;%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", actionName, context, sectionId, itemId, fileItem.getExternalId()) %>">
           			 	                   <bean:message key="link.permissions" bundle="SITE_RESOURCES"/>
           			    	            </html:link>
									</span>
								</td>
								
                        	</tr>
                       	</logic:iterate>
                   	</table>
                </div>
            </logic:notEmpty>
        </div>
        </div>
	</logic:iterate>
</logic:notEmpty>



<!-- Functionalities -->

<logic:equal name="site" property="templateAvailable" value="true">
<h3 class="mtop15 separator2"><bean:message key="title.section.institutionalContents" bundle="SITE_RESOURCES"/></h3>

	<ul class="mbottom2 list5" style="list-style: none;">
		<li>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= String.format("%s?method=prepareAddFromPool&amp;sectionID=%s&amp;%s", actionName, sectionId, context) %>">
				<bean:message key="link.insertInstitutionalContent" bundle="SITE_RESOURCES"/>
			</html:link>
		</li>
	</ul>

<logic:empty name="section" property="childrenTemplatedSections">
	<p><em><bean:message key="label.noInstitutionalContents" bundle="SITE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="section" property="childrenTemplatedSections">
	<bean:define id="containerID" name="section" property="externalId"/>
	<logic:iterate id="functionality" name="section" property="childrenTemplatedSections">
			<bean:define id="contentID" name="functionality" property="externalId"/>

			<div id="content-<%= contentID%>" class="mtop15 mbottom0" style="background: #f5f5f5; padding: 0.5em;">
			<span style="float: right;"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#breadcrumbs"><bean:message key="label.top" bundle="SITE_RESOURCES"/></a></span>
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
    					<html:link action="<%=  actionName + "?method=deleteSection&amp;" + context + "&amp;sectionID=" + contentID + "&amp;containerID=" + containerID + "&amp;sectionID=" + containerID%>">
								<bean:message key="link.delete" bundle="SITE_RESOURCES"/>
			 			</html:link>
				</span>
				| 
		
					<a target="_blank" href="${functionality.fullPath}">
						<bean:message key="link.view" bundle="SITE_RESOURCES"/> »
					</a>
				</p>
			 </div>
			
	</logic:iterate>
</logic:notEmpty>
</logic:equal>


<%-------------
	Files
  -------------%>

<h3 class="mtop15 separator2"><bean:message key="title.item.files" bundle="SITE_RESOURCES"/></h3>

	<ul class="mbottom2 list5" style="list-style: none;">
		<li>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="<%= String.format("%s?method=uploadFile&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
					<bean:message key="link.insertFile" bundle="SITE_RESOURCES"/>
				</html:link>
		</li>
	</ul>

<logic:notEmpty name="section" property="fileContentSet">

                    <strong><bean:message key="label.files" bundle="SITE_RESOURCES"/>:</strong>
                    
                    	<table class="tstyle2 thlight tdcenter width100">
                    		<tr>
	                    		<th><bean:message key="label.name" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.file" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.section.item.file.availability" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.section.item.file.options" bundle="SITE_RESOURCES"/></th>
                    		</tr>
                        	<logic:iterate id="fileItem" name="section" property="fileContentSet" type="net.sourceforge.fenixedu.domain.FileContent">
							<tr>
								<td>
	                        		<bean:define id="downloadUrl">
	                        			<bean:write name="fileItem" property="downloadUrl"/>
	                        		</bean:define>
           	                		<html:link href="<%= downloadUrl %>">
           	                			<fr:view name="fileItem" property="displayName"/>
           	                		</html:link>
            	    		        <bean:define id="message">
        	                            <bean:message key="message.item.file.delete.confirm" bundle="SITE_RESOURCES" arg0="<%= fileItem.getDisplayName() %>"/>
        	                        </bean:define>
           	                	</td>
            	                <td> <bean:write name="fileItem" property="filename"/></td>	
            					
            					
            					<td>
	                                <span class="pleft1" style="color: #888;">
	                                    <bean:message key="label.item.file.availableFor" bundle="SITE_RESOURCES"/>:
	                                    <fr:view name="fileItem" property="permittedGroup" layout="null-as-label" type="org.fenixedu.bennu.core.groups.Group">
	                                        <fr:layout>
	                                            <fr:property name="label" value="label.public"/>
	                                            <fr:property name="key" value="true"/>
	                                            <fr:property name="bundle" value="SITE_RESOURCES"/>
	                                            <fr:property name="subLayout" value="values"/>
	                                            <fr:property name="subSchema" value="permittedGroup.name"/>
	                                        </fr:layout>
	                                    </fr:view>
	                                </span>
       	    	                </td>
       	    	                
            					<td class="nowrap">	
        							<span class="pleft1">
           		                		<html:link page="<%= String.format("%s?method=deleteFile&amp;%s&amp;sectionID=%s&amp;fileItemId=%s", actionName, context, sectionId, fileItem.getExternalId()) %>"
           		                                   onclick="<%= String.format("return confirm('%s')", message) %>">
           			                        <bean:message key="link.delete" bundle="SITE_RESOURCES"/>
           			                    </html:link> |
           			                    <html:link page="<%= String.format("%s?method=editDisplayName&amp;%s&amp;sectionID=%s&amp;fileItemId=%s", actionName, context, sectionId, fileItem.getExternalId()) %>">
           			                        <bean:message key="link.edit" bundle="SITE_RESOURCES"/>
           			                    </html:link> | 
           			                    <html:link page="<%= String.format("%s?method=prepareEditItemFilePermissions&amp;%s&amp;sectionID=%s&amp;fileItemId=%s", actionName, context, sectionId, fileItem.getExternalId()) %>">
           			 	                   <bean:message key="link.permissions" bundle="SITE_RESOURCES"/>
           			    	            </html:link>
									</span>
								</td>
								
                        	</tr>
                       	</logic:iterate>
                   	</table>
                   	
</logic:notEmpty>

<!-- Change item delete operation if possible -->
<script type="text/javascript">
    switchGlobal();
</script>

