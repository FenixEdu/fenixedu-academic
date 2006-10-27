<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="fileItem" type="net.sourceforge.fenixedu.domain.FileItem" name="fileItem"/>
<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>
<bean:define id="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse" name="executionCourse"/>

<h2>
    <fr:view name="item" property="name" />
</h2>

<h3>
    <bean:message key="label.teacher.siteAdministration.editItemFilePermissions.editPermissions"/>
</h3>

<span class="error">
    <html:errors />
</span>

<fr:view name="fileItem" schema="item.file.basic">
    <fr:layout name="tabular">
        <fr:property name="classes" value="thtop tstyle1 mtop05"/>
    </fr:layout>
</fr:view>

<fr:edit name="fileItemBean" schema="item.file.permittedGroup" 
         action="<%= String.format("/manageExecutionCourse.do?method=editItemFilePermissions&executionCourseID=%s&itemID=%s&fileItemId=%s", executionCourse.getIdInternal(), item.getIdInternal(), fileItem.getIdInternal()) %>">
        <fr:layout name="tabular">
        <fr:property name="classes" value="thtop tstyle1 mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageExecutionCourse.do?method=section&executionCourseID=%s&sectionID=%s", executionCourse.getIdInternal(), item.getSection().getIdInternal()) %>"/>
</fr:edit>

