<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

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