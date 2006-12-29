<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils"%>
<%@ page import="net.sourceforge.fenixedu.domain.Language"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<div class="mvert1">
    <html:link page="<%= String.format("/manageExecutionCourse.do?method=sections&amp;executionCourseID=%s", executionCourseId) %>">
        <bean:message key="link.breadCrumbs.top" bundle="SITE_RESOURCES"/>
    </html:link> &gt;
    
    <logic:iterate id="crumb" name="sectionBreadCrumbs">
        <html:link page="<%= String.format("/manageExecutionCourse.do?method=section&amp;executionCourseID=%s", executionCourseId) %>" paramId="sectionID" paramName="crumb" paramProperty="idInternal">
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

<p>
    <span style="color: #888;" class="anchorcaaa">
        <bean:message key="label.section.directLink" bundle="SITE_RESOURCES"/>:
        
        <bean:define id="sectionDirectLink" type="java.lang.String">
            <%= request.getContextPath() + SectionProcessor.getSectionAbsolutePath(executionCourse, section) %>
        </bean:define>
        <html:link href="<%= sectionDirectLink %>">
            <%= RequestUtils.requestToServerStringBuffer(request).toString() + sectionDirectLink %>
        </html:link>
    </span>
</p>
</div>

<p>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editSection&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.editSection"/>
		</html:link>
	</span>

    <span class="switchNoneStyle">
        	<span class="pleft1">	
        		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=deleteSection&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
        		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
        			<bean:message key="button.deleteSection"/>
        		</html:link>
        	</span>
    </span>

    <bean:define id="deleteSectionId" value="<%= "deleteSectionForm" + section.getIdInternal() %>"/>

    <span class="switchInline">
        <span class="pleft1">
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
            <html:link href="<%= String.format("javascript:showElement('%s');", deleteSectionId) %>">
                <bean:message key="button.deleteSection"/>
            </html:link>
        </span>
    </span>

	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=createSection&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.insertSubSection"/>
		</html:link>
	</span>
	
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editSectionPermissions&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
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
    <fr:form action="<%= String.format("/manageExecutionCourse.do?method=confirmSectionDelete&amp;executionCourseID=%s&amp;sectionID=%s&amp;confirm=true", executionCourseId, section.getIdInternal()) %>">
        <p class="width550px">
        <span class="warning0 mright075">
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

<logic:empty name="section" property="orderedSubSections">
    <p class="mtop2">
        <em>
            <bean:message key="message.subSections.empty" bundle="SITE_RESOURCES"/>
        </em>
    </p>
</logic:empty>

<logic:notEmpty name="section" property="orderedSubSections">
    <fr:form action="<%= "/manageExecutionCourse.do?method=saveSectionsOrder&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "sectionTree" + section.getIdInternal(); %>
        
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
        <fr:view name="section" property="orderedSubSections">
            <fr:layout name="tree">
                <fr:property name="treeId" value="<%= treeId %>"/>
                <fr:property name="fieldId" value="sections-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(Section)" value="site.section.name"/>
                <fr:property name="childrenFor(Section)" value="orderedSubSections"/>
            </fr:layout>
            <fr:destination name="section.view" path="<%= "/manageExecutionCourse.do?method=section&sectionID=${idInternal}&executionCourseID=" + executionCourseId %>"/>
        </fr:view>

		<p class="mtop15">
		    <fr:form action="<%= "/manageExecutionCourse.do?method=section&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
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

<span class="error">
    <html:errors property="items" bundle="SITE_RESOURCES"/>
</span>

<ul class="mbottom2">
    <li>
        <bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=createItem&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
        <html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
            <bean:message key="button.insertItem"/>
        </html:link>
    </li>
    <logic:notEmpty name="section" property="associatedItems">
        <bean:size id="itemsCount" name="section" property="associatedItems"/>
        
        <logic:greaterThan name="itemsCount" value="1">
            <li>
                <html:link page="<%= "/manageExecutionCourse.do?method=organizeSectionItems&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
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

		<div class="separator1 mtop15 mbottom0"></div>
		
		<p class="mtop0 mbottom05">
			<%--<span style="color: #888;"><bean:message key="label.item"/></span><br/>--%>
			<strong><fr:view name="item" property="name"/></strong>
            (<a href="<%= request.getContextPath() + ItemProcessor.getItemAbsolutePath(executionCourse, item) %>"><bean:message key="label.item.directLink" bundle="SITE_RESOURCES"/></a>)
            
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
			<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editItem&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
			<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="button.editItem"/>
			</html:link>
		</span>

        <bean:define id="deleteUrl" type="java.lang.String" value="<%= String.format("/manageExecutionCourse.do?method=deleteItem&amp;executionCourseID=%s&amp;itemID=%s", executionCourseId, item.getIdInternal()) %>"/>
        <bean:define id="deleteId" value="<%= "deleteForm" + item.getIdInternal() %>"/>

        <span class="switchNoneStyle">
        	   <span class="pleft1">
	       		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
	       		<html:link page="<%= deleteUrl %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	       			<bean:message key="button.deleteItem"/>
	       		</html:link>
	       	</span>
        </span>

        <span class="switchInline">
	     	<span class="pleft1">
	            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
	            <html:link href="<%= String.format("javascript:showElement('%s');", deleteId) %>">
	                <bean:message key="button.deleteItem"/>
	            </html:link>
			</span>
        </span>

		<span class="pleft1">
	        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
	        <bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editItemPermissions&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
	        <html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	            <bean:message key="link.item.group.edit" bundle="SITE_RESOURCES"/>
	        </html:link>
        </span>

		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=uploadFile&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
			<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="button.insertFile"/>
			</html:link>
		</span>

		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<bean:define id="url" type="java.lang.String">/searchScormContent.do?method=prepareSearchScormContents&amp;searchType=simple&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
			<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.searhScormContent" bundle="SITE_RESOURCES"/>
			</html:link>
		</span>
		
        <logic:notEmpty name="item" property="fileItems">
            <bean:size id="filesCount" name="item" property="fileItems"/>
        
            <logic:greaterThan name="filesCount" value="1">
        			<span class="pleft1">
        		        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        	            <bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=organizeItemFiles&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
        	            <html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
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
        
        <div class="mtop1 pleft2">
        	<%--
			<div class="separator1"></div>
			--%>
		
            <div class="width550px">
               	<fr:view name="item" property="information" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html">
	               	<fr:layout>
						<fr:property name="classes" value="coutput1 mbottom0"/>
					</fr:layout>
               	</fr:view>
            </div>
    
            <logic:notEmpty name="item" property="fileItems">
                <div>
                    <span style="color: #888;"><bean:message key="label.files" bundle="SITE_RESOURCES"/>: </span>
                    	<ul>
                        	<logic:iterate id="fileItem" name="item" property="sortedFileItems" type="net.sourceforge.fenixedu.domain.FileItem">
                        		<bean:define id="downloadUrl">
                        			<bean:write name="fileItem" property="downloadUrl"/>
                        		</bean:define>
                        		
                        		<li>
           	                		<html:link href="<%= downloadUrl %>">
           	                			<fr:view name="fileItem" property="displayName"/>
           	                		</html:link>
            	                		
            	    		        <bean:define id="message">
        	                            <bean:message key="message.item.file.delete.confirm" bundle="SITE_RESOURCES" arg0="<%= fileItem.getDisplayName() %>"/>
        	                        </bean:define>
            							
        							<span class="pleft1">
            		                		(<html:link page="<%= String.format("/manageExecutionCourse.do?method=deleteFile&amp;executionCourseID=%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", executionCourseId, section.getIdInternal(), item.getIdInternal(), fileItem.getIdInternal()) %>"
            		                                   onclick="<%= String.format("return confirm('%s')", message) %>">
            			                        <bean:message key="label.teacher.siteAdministration.viewSection.deleteItemFile"/>
            			                    </html:link>, 
            			                    <html:link page="<%= String.format("/manageExecutionCourse.do?method=prepareEditItemFilePermissions&amp;executionCourseID=%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", executionCourseId, section.getIdInternal(), item.getIdInternal(), fileItem.getIdInternal()) %>">
            			 	                   <bean:message key="label.teacher.siteAdministration.viewSection.editItemFilePermissions"/>
            			    	            </html:link>)
            		    	            </span>

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
        	    	                </li>
                        	</logic:iterate>
                    	</ul>
                </div>
            </logic:notEmpty>
        </div>
	</logic:iterate>
</logic:notEmpty>

<!-- Change item delete operation if possible -->
<script type="text/javascript">
    switchGlobal();
</script>