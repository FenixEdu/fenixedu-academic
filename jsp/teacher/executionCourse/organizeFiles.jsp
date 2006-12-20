<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.Language"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<logic:notEmpty name="item" property="sortedFileItems">
    <fr:form action="<%= "/manageExecutionCourse.do?method=saveFilesOrder&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() + "&amp;itemID=" + item.getIdInternal() %>">
        <input id="files-order" type="hidden" name="filesOrder" value=""/>
    </fr:form>
    
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
        <fr:view name="item" property="sortedFileItems">
            <fr:layout name="tree">
                <fr:property name="treeId" value="filesOrder"/>
                <fr:property name="fieldId" value="files-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(FileItem)" value="site.item.file.basic"/>
            </fr:layout>
        </fr:view>

		<p class="mtop15">
		    <fr:form action="<%= "/manageExecutionCourse.do?method=section&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
		        <html:button property="saveButton" onclick="<%= "treeRenderer_saveTree('filesOrder');" %>">
		            <bean:message key="button.item.files.order.save" bundle="SITE_RESOURCES"/>
		        </html:button>
		        <html:submit>
		            <bean:message key="button.item.files.order.reset" bundle="SITE_RESOURCES"/>
		        </html:submit>
		    </fr:form>
	    </p>
    </div>
    
</logic:notEmpty>
