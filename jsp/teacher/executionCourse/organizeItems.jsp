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

<p><strong><bean:message key="link.section.items.organize" bundle="SITE_RESOURCES"/></strong></p>

<logic:notEmpty name="section" property="orderedItems">
    <fr:form action="<%= "/manageExecutionCourse.do?method=saveItemsOrder&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
        <input alt="input.itemsOrder" id="items-order" type="hidden" name="itemsOrder" value=""/>
    </fr:form>
    
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
        <fr:view name="section" property="orderedItems">
            <fr:layout name="tree">
                <fr:property name="treeId" value="itemsTree"/>
                <fr:property name="fieldId" value="items-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(Item)" value="site.item.name"/>
            </fr:layout>
        </fr:view>
        
		<p class="mtop15">
			<fr:form action="<%= "/manageExecutionCourse.do?method=section&amp;executionCourseID=" + executionCourseId + "&amp;sectionID=" + section.getIdInternal() %>">
		       <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('itemsTree');" %>">
		           <bean:message key="button.items.order.save" bundle="SITE_RESOURCES"/>
		       </html:button>
		       <html:submit>
		           <bean:message key="button.items.order.reset" bundle="SITE_RESOURCES"/>
		       </html:submit>
			</fr:form>
		</p>
    </div>

	<p style="color: #888;"><em><bean:message key="message.item.reorder.tip" bundle="SITE_RESOURCES"/></em></p>

</logic:notEmpty>
