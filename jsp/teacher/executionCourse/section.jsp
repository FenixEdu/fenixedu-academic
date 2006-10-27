<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.Language"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<div style="margin-bottom: 1em;">
    <span class="error">
        <html:errors property="section" bundle="SITE_RESOURCES"/>
    </span>
</div>

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editSection&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.editSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=deleteSection&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.deleteSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=createSection&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.insertSubSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editSectionPermissions&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
    <bean:message key="link.section.group.edit" bundle="SITE_RESOURCES"/>
</html:link>

<logic:empty name="section" property="orderedSubSections">
    <p class="mtop2">
        <span class="warning0">
            <bean:message key="message.subSections.empty" bundle="SITE_RESOURCES"/>
        </span>
    </p>
</logic:empty>

<logic:notEmpty name="section" property="orderedSubSections">
    <fr:form action="<%= "/manageExecutionCourse.do?method=saveSectionsOrder&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
        <input id="sections-order" type="hidden" name="sectionsOrder" value=""/>
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
    </div>
    
    <fr:form action="<%= "/manageExecutionCourse.do?method=section&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
        <html:button property="saveButton" onclick="<%= "treeRenderer_saveTree('" + treeId + "');" %>">
            <bean:message key="button.sections.order.save" bundle="SITE_RESOURCES"/>
        </html:button>
        <html:submit>
            <bean:message key="button.sections.order.reset" bundle="SITE_RESOURCES"/>
        </html:submit>
    </fr:form>
</logic:notEmpty>

<br/>

<%-------------
     Items
  -------------%>

<h3><bean:message key="title.section.items" bundle="SITE_RESOURCES"/></h3>

<span class="error">
    <html:errors property="items" bundle="SITE_RESOURCES"/>
</span>

<ul>
    <li>
        <bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=createItem&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
        <html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
            <bean:message key="button.insertItem"/>
        </html:link>
    </li>
    <logic:notEmpty name="section" property="associatedItems">
        <li>
            <html:link page="<%= "/manageExecutionCourse.do?method=organizeSectionItems&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
                <bean:message key="link.section.items.organize" bundle="SITE_RESOURCES"/>
            </html:link>
        </li>
    </logic:notEmpty>
</ul>

<logic:empty name="section" property="associatedItems">
    <p>
        <span class="warning0">
            <bean:message key="message.section.items.empty" bundle="SITE_RESOURCES"/>
        </span>
    </p>
</logic:empty>

<logic:notEmpty name="section" property="associatedItems">
	<logic:iterate id="item" name="section" property="orderedItems" type="net.sourceforge.fenixedu.domain.Item">
		<h3>
			<bean:message key="label.item"/>:&nbsp;
			<fr:view name="item" property="name" />
		</h3>
		
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editItem&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.editItem"/>
		</html:link>

        <bean:define id="deleteUrl" type="java.lang.String" value="<%= String.format("/manageExecutionCourse.do?method=deleteItem&amp;executionCourseID=%s&amp;itemID=%s", executionCourseId, item.getIdInternal()) %>"/>
        <bean:define id="deleteId" value="<%= "deleteForm" + item.getIdInternal() %>"/>

        <span class="switchNoneStyle">
        		&nbsp;&nbsp;
            
        		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        		<html:link page="<%= deleteUrl %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
        			<bean:message key="button.deleteItem"/>
        		</html:link>
        </span>

        <span class="switchInline">
            &nbsp;&nbsp;
            
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
            <html:link href="<%= String.format("javascript:showElement('%s');", deleteId) %>">
                <bean:message key="button.deleteItem"/>
            </html:link>
        </span>
        
        &nbsp;&nbsp;

        <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        <bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editItemPermissions&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
        <html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
            <bean:message key="link.item.group.edit" bundle="SITE_RESOURCES"/>
        </html:link>

		&nbsp;&nbsp;

		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=uploadFile&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.insertFile"/>
		</html:link>

        <logic:notEmpty name="item" property="fileItems">
            &nbsp;&nbsp;
    
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
            <bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=organizeItemFiles&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
            <html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
                <bean:message key="link.item.files.organize" bundle="SITE_RESOURCES"/>
            </html:link>
        </logic:notEmpty>
        
        <div id="<%= deleteId %>" class="dnone mvert05">
            <fr:form action="<%= deleteUrl %>">
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
                <html:button property="hide" onclick="<%= String.format("javascript:hideElement('%s');", deleteId) %>">
                    <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
                </html:button>
            </fr:form>
        </div>
        
        <div style="margin-top: 1em; margin-left: 2em;">
            <div>
                	<fr:view name="item" property="information" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html"/>
            </div>
    
            <logic:notEmpty name="item" property="sortedFileItems">
                <div style="margin-top: 1em;">
                    <fr:view name="item" property="sortedFileItems">
                        <fr:layout name="tabular-list">
                            <fr:property name="subSchema" value="site.item.file.basic"/>
                            <fr:property name="subLayout" value="values"/>
                            
                            <fr:property name="customLink(delete)">
                                <bean:define id="message">
                                    <bean:message key="message.item.file.delete.confirm" bundle="SITE_RESOURCES" arg0="${displayName}"/>
                                </bean:define>
                                <html:link page="<%= String.format("/manageExecutionCourse.do?method=deleteFile&amp;executionCourseID=%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", executionCourseId, section.getIdInternal(), item.getIdInternal(), "${idInternal}") %>"
                                           onclick="<%= String.format("return confirm('%s')", message) %>">
                                    <bean:message key="label.teacher.siteAdministration.viewSection.deleteItemFile"/>
                                </html:link>
                            </fr:property>
                            <fr:property name="order(delete)" value="0"/>
            
                            <fr:property name="customLink(permissions)">
                                <html:link page="<%= String.format("/manageExecutionCourse.do?method=prepareEditItemFilePermissions&amp;executionCourseID=%s&amp;sectionID=%s&amp;itemID=%s&amp;fileItemId=%s", executionCourseId, section.getIdInternal(), item.getIdInternal(), "${idInternal}") %>">
                                    <bean:message key="label.teacher.siteAdministration.viewSection.editItemFilePermissions"/>
                                </html:link>
                            </fr:property>
                            <fr:property name="order(permissions)" value="1"/>
                        </fr:layout>
                    </fr:view>
                </div>
            </logic:notEmpty>
        </div>
	</logic:iterate>

    <!-- Change item delete operation if possible -->
    <script type="text/javascript">
        switchGlobal();
    </script>
</logic:notEmpty>