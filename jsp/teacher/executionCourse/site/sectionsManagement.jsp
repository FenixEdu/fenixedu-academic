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

<p>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=createSection&amp;" + context %>">
			<bean:message key="link.createSection"/>
		</html:link>
	</span>
	
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= actionName + "?method=prepareImportSections&amp;" + context %>">
			<bean:message key="label.import.sections"/>
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

<logic:notEmpty name="site" property="orderedTopLevelSections">
    <fr:form action="<%= actionName + "?method=saveSectionsOrder&amp;" + context %>">
        <input alt="input.sectionsOrder" id="sections-order" type="hidden" name="sectionsOrder" value=""/>
    </fr:form>
    
    <% String treeId = "sectionsTree." + contextParam + "." + contextParamValue; %>
    
    <div class="section1">
        <fr:view name="site" property="orderedTopLevelSections">
            <fr:layout name="tree">
                <fr:property name="treeId" value="<%= treeId %>"/>
                <fr:property name="fieldId" value="sections-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(Section)" value="site.section.name"/>
                <fr:property name="childrenFor(Section)" value="orderedSubSections"/>
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
