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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/></em>
<h2><bean:message key="title.files.sources" bundle="MESSAGING_RESOURCES"/></h2>

<div class="infoop2">
	<p class="mvert0"><bean:message key="message.files.sources" bundle="MESSAGING_RESOURCES"/></p>
</div>

<logic:empty name="sources">
	<p><em><bean:message key="message.files.sources.empty" bundle="MESSAGING_RESOURCES"/></em></p>
</logic:empty>

<br/>

<logic:notEmpty name="sources">
	<fr:view name="sources">
	   <fr:layout name="tree">
	       <fr:property name="treeId" value="messsagingFilesSources"/>
	       <fr:property name="expandable" value="true"/>
	       
	       <fr:property name="eachLayout" value="values"/>
	
	       <fr:property name="schemaFor(PersonFileSourceGroupBean)" value="messaging.file.sourceGroup"/>
	       <fr:property name="childrenFor(PersonFileSourceGroupBean)" value="children"/>
	       <fr:property name="imageFor(PersonFileSourceGroupBean)" value="/images/functionalities/folder.gif"/>
	       <fr:property name="schemaFor(PersonFileSourceBean)" value="messaging.file.source"/>
	       <fr:property name="imageFor(PersonFileSourceBean)" value="/images/functionalities/folder.gif"/>
	   </fr:layout>
	   
	   <fr:destination name="files.view" path="<%= "/viewFiles.do?method=viewFiles&unitId=${unit.externalId}" %>"/>
	</fr:view>
</logic:notEmpty>

<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/drag-drop-folder-tree/js/ajax.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/drag-drop-folder-tree/js/drag-drop-folder-tree.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/drag-drop-folder-tree/js/tree-renderer.js"></script>
