<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="optional" name="optional" type="java.lang.Boolean"/>

<h2><bean:message key="link.bibliography" /></h2>

<p>
    <strong>
        <logic:equal name="optional" value="false">
            <bean:message key="message.bibliography.order"/>
        </logic:equal>
        <logic:equal name="optional" value="true">
            <bean:message key="message.bibliography.optional.order"/>
        </logic:equal>
    </strong>
</p>

<p>
    <span class="error"><!-- Error messages go here -->
        <html:errors/>
    </span>
</p>

<fr:form action="<%= "/manageExecutionCourse.do?method=sortBibliographyReferences&amp;executionCourseID=" + executionCourseId + (optional ? "&amp;optional=true" : "") %>">
    <input id="referencesOrder" type="hidden" name="referencesOrder" value=""/>
</fr:form>
    
<fr:view name="references">
    <fr:layout name="tree">
        <fr:property name="treeId" value="referencesOrderTree"/>
        <fr:property name="fieldId" value="referencesOrder"/>
        
        <fr:property name="eachLayout" value="values-comma"/>
        <fr:property name="eachSchema" value="executionCourse.bibliographicReference.simple"/>
    </fr:layout>
</fr:view>

<p class="mtop15">
    <fr:form action="<%="/manageExecutionCourse.do?method=bibliographicReference&amp;executionCourseID=" + executionCourseId %>">
       <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('referencesOrderTree');" %>">
           <bean:message key="button.items.order.save" bundle="SITE_RESOURCES"/>
       </html:button>
       <html:submit>
           <bean:message key="button.items.order.reset" bundle="SITE_RESOURCES"/>
       </html:submit>
    </fr:form>
</p>
