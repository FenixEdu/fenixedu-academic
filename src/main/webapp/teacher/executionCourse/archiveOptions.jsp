<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="link.executionCourse.archive" /></h2>

<p>
    <span class="error"><!-- Error messages go here -->
        <html:errors/>
    </span>
</p>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="link.executionCourse.archive.explanation"/> 
    </p>
</div>

<bean:define id="executionCourseId" name="executionCourse" property="externalId"/>

<fr:form action="<%= "/generateArchive.do?method=generate&amp;executionCourseID=" + executionCourseId %>">
    <fr:edit id="options" name="options" schema="executionCourse.archive.options">
         <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear"/>
         </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.executionCourse.archive.generate"/>
    </html:submit>
</fr:form>