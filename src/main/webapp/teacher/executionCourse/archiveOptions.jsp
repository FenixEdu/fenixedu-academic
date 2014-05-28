<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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