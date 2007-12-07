<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor"%>

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

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="sectionId" name="section" property="idInternal"/>

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

<div class="mvert1">
    <html:link page="<%= String.format("%s?method=sections&amp;%s", actionName, context) %>">
        <bean:message key="link.breadCrumbs.top" bundle="SITE_RESOURCES"/>
    </html:link> &gt;
    
    <logic:iterate id="crumb" name="sectionBreadCrumbs">
        <html:link page="<%= String.format("%s?method=section&amp;%s", actionName, context) %>" paramId="sectionID" paramName="crumb" paramProperty="idInternal">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <fr:view name="section" property="name"/>
</div>

<div class="infoop2">
    <p>
        <span style="color: #888;">
            <bean:message key="label.section.availableFor" bundle="SITE_RESOURCES"/>:
            <fr:view name="section" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
                <fr:layout>
                    <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
                    <fr:property name="key" value="true"/>
                    <fr:property name="bundle" value="SITE_RESOURCES"/>
                    <fr:property name="subLayout" value="values"/>
                    <fr:property name="subSchema" value="permittedGroup.class.text"/>
                </fr:layout>
            </fr:view>    
        </span>
    </p>
    <logic:present name="directLinkContext">
        <p>
            <span style="color: #888;" class="anchorcaaa">
                <bean:message key="label.section.directLink" bundle="SITE_RESOURCES"/>:
                
                <bean:define id="directLinkContext" name="directLinkContext"/>
                <html:link href="<%= directLinkContext + SectionProcessor.getSectionPath(section) %>">
                    <%= directLinkContext + SectionProcessor.getSectionPath(section) %>
                </html:link>
            </span>
        </p>
    </logic:present>
</div>

<p>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= String.format("%s?method=editSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
			<bean:message key="link.editSection" bundle="SITE_RESOURCES"/>
		</html:link>
	</span>

    <bean:define id="deleteSectionId" value="<%= "deleteSectionForm" + sectionId %>"/>

    <span class="switchNoneStyle">
        	<span class="pleft1">	
        		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        		<html:link page="<%= String.format("%s?method=deleteSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
        			<bean:message key="link.deleteSection" bundle="SITE_RESOURCES"/>
        		</html:link>
        	</span>
    </span>


    <span class="switchInline">
        <span class="pleft1">
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
            <html:link href="#" onclick="<%= String.format("showElement('%s');", deleteSectionId) %>">
                <bean:message key="link.deleteSection" bundle="SITE_RESOURCES"/>
            </html:link>
        </span>
    </span>

    	<span class="pleft1">
    		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
    		<html:link page="<%= String.format("%s?method=createSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
    			<bean:message key="link.insertSubSection" bundle="SITE_RESOURCES"/>
    		</html:link>
    	</span>

    	<span class="pleft1">
    		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
    		<html:link page="<%= String.format("%s?method=editSectionPermissions&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
    		    <bean:message key="link.section.group.edit" bundle="SITE_RESOURCES"/>
    		</html:link>
    	</span>
</p>

<p class="mvert1">
    <span class="error">
        <html:errors property="section" bundle="SITE_RESOURCES"/>
    </span>
</p>

<div id="<%= deleteSectionId %>" class="dnone mvert05">
    <fr:form action="<%= String.format("%s?method=confirmSectionDelete&amp;%s&amp;sectionID=%s&amp;confirm=true", actionName, context, sectionId) %>">
        <p class="warning0" style="padding: 0.5em;">
        <span class="mright075">
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

<%--
<logic:empty name="section" property="orderedSubSections">
    <p class="mtop2">
        <em>
            <bean:message key="message.subSections.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>
--%>

<logic:notEmpty name="site" property="directChildrenAsContent">
    <fr:form action="<%= actionName + "?method=saveSectionsOrder&amp;" + context + "&amp;sectionID=" + sectionId %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "subSectionTree" + site.getIdInternal(); %>
        
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
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
				<fr:property name="imageFor(Item)" value="/images/functionalities/sheet.gif"/>
				
                <fr:property name="current" value="<%= sectionId.toString() %>"/>
                <fr:property name="currentClasses" value="highlight1"/>
                <fr:property name="movedClass" value="highlight3"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= actionName + "?method=section&sectionID=${idInternal}&" + context %>"/>
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
</logic:notEmpty>

<%-------------
     Items
  -------------%>

<h3 class="mtop15 separator2"><bean:message key="title.section.items" bundle="SITE_RESOURCES"/></h3>

<p>
	<span class="error0">
	    <html:errors property="items" bundle="SITE_RESOURCES"/>
	</span>
</p>

<ul class="mbottom2">
    <logic:equal name="section" property="itemAllowed" value="true">
        <li>
            <html:link page="<%= String.format("%s?method=createItem&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
                <bean:message key="link.insertItem" bundle="SITE_RESOURCES"/>
            </html:link>
        </li>
    </logic:equal>
    <logic:notEmpty name="section" property="associatedItems">
        <bean:size id="itemsCount" name="section" property="associatedItems"/>
        
        <logic:greaterThan name="itemsCount" value="1">
            <li>
                <html:link page="<%= actionName + "?method=organizeSectionItems&amp;" + context + "&amp;sectionID=" + sectionId %>">
                    <bean:message key="link.section.items.organize" bundle="SITE_RESOURCES"/>
                </html:link>
            </li>
        </logic:greaterThan>
    </logic:notEmpty>
</ul>

<logic:empty name="section" property="associatedItems">
    <p>
        <em>
            <bean:message key="message.section.items.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>

<logic:notEmpty name="section" property="associatedItems">
	<logic:iterate id="item" name="section" property="orderedItems" type="net.sourceforge.fenixedu.domain.Item">

        <bean:define id="itemId" name="item" property="idInternal"/>
		
		<div>
		<div class="mtop15 mbottom0" style="background: #fafafa; padding: 0.5em;">
		
			<p class="mtop0 mbottom05">
				<%--<span style="color: #888;"><bean:message key="label.item"/></span><br/>--%>
				<strong><fr:view name="item" property="name"/></strong>
	            <logic:present name="directLinkContext">
	                <bean:define id="directLinkContext" name="directLinkContext"/>
	                (<a href="<%= directLinkContext + ItemProcessor.getItemPath(item) %>"><bean:message key="label.item.directLink" bundle="SITE_RESOURCES"/></a>)
	            </logic:present>
	            
	            <span style="color: #888; padding-left: 1em;">
	                <bean:message key="label.item.availableFor" bundle="SITE_RESOURCES"/>:
	                <fr:view name="item" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
	                    <fr:layout>
	                        <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
	                        <fr:property name="key" value="true"/>
	                        <fr:property name="bundle" value="SITE_RESOURCES"/>
	                        <fr:property name="subLayout" value="values"/>
	                        <fr:property name="subSchema" value="permittedGroup.class.text"/>
	                    </fr:layout>
	                </fr:view>
	            </span>
			</p>
	        
	        <span>
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= String.format("%s?method=editItem&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
					<bean:message key="link.editItem" bundle="SITE_RESOURCES"/>
				</html:link>
			</span>
	
	        <bean:define id="deleteUrl" type="java.lang.String" value="<%= String.format("%s?method=deleteItem&amp;%s&amp;itemID=%s", actionName, context, itemId) %>"/>
	        <bean:define id="deleteId" value="<%= "deleteForm" + item.getIdInternal() %>"/>
	
	        <span class="switchNoneStyle">
	        	   <span class="pleft1">
		       		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		       		<html:link page="<%= deleteUrl %>">
		       			<bean:message key="link.deleteItem" bundle="SITE_RESOURCES"/>
		       		</html:link>
		       	</span>
	        </span>
	
	        <span class="switchInline">
		     	<span class="pleft1">
		            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		            <html:link href="#" onclick="<%= String.format("javascript:showElement('%s');", deleteId) %>">
		                <bean:message key="link.deleteItem" bundle="SITE_RESOURCES"/>
		            </html:link>
				</span>
	        </span>
	
			<span class="pleft1">
		        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		        <html:link page="<%= String.format("%s?method=editItemPermissions&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
		            <bean:message key="link.item.group.edit" bundle="SITE_RESOURCES"/>
		        </html:link>
	        </span>
	
			<span class="pleft1">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= String.format("%s?method=uploadFile&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
					<bean:message key="link.insertFile" bundle="SITE_RESOURCES"/>
				</html:link>
			</span>
	
	        <logic:notEmpty name="item" property="fileItems">
	            <bean:size id="filesCount" name="item" property="fileItems"/>
	        
	            <logic:greaterThan name="filesCount" value="1">
	        			<span class="pleft1">
	        		        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
	        	            <html:link page="<%= String.format("%s?method=organizeItemFiles&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
	        	                <bean:message key="link.item.files.organize" bundle="SITE_RESOURCES"/>
	        	            </html:link>
	                </span>
	            </logic:greaterThan>
	            
	        </logic:notEmpty>
	        		        
	        <div id="<%= deleteId %>" class="dnone mvert05">
	            <fr:form action="<%= deleteUrl %>">
	            	<p class="width550px">
	                <span class="warning0 mright075">
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
               	<fr:view name="item" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html">
               	</fr:view>
            </div>
    
            <logic:notEmpty name="item" property="fileItems">
                <div class="mtop2">
                
                    <strong><bean:message key="label.files" bundle="SITE_RESOURCES"/>:</strong>
                    
                    	<table class="tstyle2 thlight width100">
                    		<tr>
	                    		<th><bean:message key="label.section.item.file.title" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.section.item.file.options" bundle="SITE_RESOURCES"/></th>
	                    		<th><bean:message key="label.section.item.file.availability" bundle="SITE_RESOURCES"/></th>
                    		</tr>
                        	<logic:iterate id="fileItem" name="item" property="sortedFileItems" type="net.sourceforge.fenixedu.domain.FileItem">
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
            	                	
            					<td class="nowrap">	
        							<span class="pleft1">
           		                		(<html:link page="<%= String.format("%s?method=deleteFile&amp;%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", actionName, context, sectionId, itemId, fileItem.getIdInternal()) %>"
           		                                   onclick="<%= String.format("return confirm('%s')", message) %>">
           			                        <bean:message key="link.section.item.deleteItemFile" bundle="SITE_RESOURCES"/>
           			                    </html:link>, 
           			                    <html:link page="<%= String.format("%s?method=prepareEditItemFilePermissions&amp;%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", actionName, context, sectionId, itemId, fileItem.getIdInternal()) %>">
           			 	                   <bean:message key="link.section.item.editItemFilePermissions" bundle="SITE_RESOURCES"/>
           			    	            </html:link>)
									</span>
								</td>
								
								<td>
	                                <span class="pleft1" style="color: #888;">
	                                    <bean:message key="label.item.file.availableFor" bundle="SITE_RESOURCES"/>:
	                                    <fr:view name="fileItem" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
	                                        <fr:layout>
	                                            <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
	                                            <fr:property name="key" value="true"/>
	                                            <fr:property name="bundle" value="SITE_RESOURCES"/>
	                                            <fr:property name="subLayout" value="values"/>
	                                            <fr:property name="subSchema" value="permittedGroup.class.text"/>
	                                        </fr:layout>
	                                    </fr:view>
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

<h3 class="mtop15 separator2"><bean:message key="title.section.institutionalContents" bundle="SITE_RESOURCES"/></h3>

<logic:equal name="site" property="template.contentPoolAvailable" value="true">
	<ul>
		<li>
		<html:link page="<%= String.format("%s?method=prepareAddFromPool&amp;sectionID=%s&amp;%s", actionName, sectionId, context) %>">
			<bean:message key="link.institutionSubSection.add" bundle="WEBSITEMANAGER_RESOURCES"/>
		</html:link>
		</li>
	</ul>
</logic:equal>
		
<logic:empty name="section" property="associatedFunctionalities">
	<em><bean:message key="label.noInstitutionalContents" bundle="SITE_RESOURCES"/></em>
</logic:empty>

<logic:notEmpty name="section" property="associatedFunctionalities">
	<bean:define id="containerID" name="section" property="idInternal"/>
	<logic:iterate id="functionality" name="section" property="associatedFunctionalities">
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
			        <html:link action="<%=  actionName + "?method=removeFunctionalityFromContainer&amp;" + context + "&amp;contentID=" + contentID + "&amp;containerID=" + containerID + "&amp;sectionID=" + containerID%>">
						<bean:message key="messaging.delete.label" bundle="WEBSITEMANAGER_RESOURCES"/>
					 </html:link>
				 </p>
			 </p>
			 </div>
			
	</logic:iterate>
</logic:notEmpty>

<!-- Change item delete operation if possible -->
<script type="text/javascript">
    switchGlobal();
</script>

