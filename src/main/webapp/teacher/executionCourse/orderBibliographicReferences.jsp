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

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

<bean:define id="executionCourseId" name="executionCourse" property="externalId"/>
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


	<fr:form action="<%= "/manageBibliographicReference.do?method=sortBibliographyReferences&amp;executionCourseID=" + executionCourseId + (optional ? "&amp;optional=true" : "") %>">
	    <input id="referencesOrder" type="hidden" name="referencesOrder" value=""/>
	</fr:form>

<div class="section1">
<fr:view name="references">
    <fr:layout name="tree">
        <fr:property name="treeId" value="referencesOrderTree"/>
        <fr:property name="fieldId" value="referencesOrder"/>
	    <fr:property name="eachLayout" value="values-comma"/>
        <fr:property name="eachSchema" value="executionCourse.bibliographicReference.simple"/>
    </fr:layout>
</fr:view>


<p class="mtop15">
    <fr:form action="<%="/manageBibliographicReference.do?method=bibliographicReference&amp;executionCourseID=" + executionCourseId %>">
       <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('referencesOrderTree');" %>">
           <bean:message key="button.items.order.save" bundle="SITE_RESOURCES"/>
       </html:button>
       <html:submit>
           <bean:message key="button.items.order.reset" bundle="SITE_RESOURCES"/>
       </html:submit>
    </fr:form>
</p>

</div>

<p style="color: #888;">
	<em><bean:message key="message.section.reorder.tip" bundle="SITE_RESOURCES"/></em>
</p>