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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

<h2><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/> <span class="small">${unit.name}</span></h2>

<bean:define id="unitID" name="unit" property="externalId"/>
<bean:define id="actionName" name="functionalityAction"/>
<bean:define id="module" name="module"/>

<ul>
	<logic:equal name="unit" property="currentUserAllowedToUploadFiles" value="true">
		<li>
			<html:link page="<%= "/" + actionName + ".do?method=prepareFileUpload&unitId=" + unitID %>"><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></html:link>
		</li>
	</logic:equal>
	<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
		<li>
			<html:link page="<%= "/" + actionName + ".do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.manageAccessGroups" bundle="RESEARCHER_RESOURCES"/></html:link>
		</li>
	</logic:equal>
	<li>
		<a href="#" data-toggle="collapse" data-target="#instructions"><bean:message key="label.instructions" bundle="RESEARCHER_RESOURCES"/></a>
	</li>
</ul>

<div id="instructions" class="collapse">
<div class="infoop2 mbottom1 mtop05">
<p class="mtop0"><strong><bean:message key="label.tagCloud" bundle="RESEARCHER_RESOURCES"/>:</strong> <bean:message key="label.tagCloud.explanation" bundle="RESEARCHER_RESOURCES"/></p>
<table>
<tr><td><img src="<%= request.getContextPath() + "/images/tag_selected.gif"%>" style="border: 1px solid #eee; padding: 4px; background: #fff;"/></td><td><bean:message key="label.tagCloud.selected.explanation" bundle="RESEARCHER_RESOURCES"/></td></tr>
<tr><td><img src="<%= request.getContextPath() + "/images/tag_nearby.gif" %>" style="border: 1px solid #eee; padding: 4px; background: #fff;"/></td><td><bean:message key="label.tagCloud.neighbour.explanation" bundle="RESEARCHER_RESOURCES"/></td></tr>
<tr><td><img src="<%= request.getContextPath() + "/images/tag_available.gif"%>" style="border: 1px solid #eee; padding: 4px; background: #fff;"/></td><td><bean:message key="label.tagCloud.available.explanation" bundle="RESEARCHER_RESOURCES"/></td></tr>
</table>
</div>
</div>

<bean:define id="tags" value="<%= request.getParameter("selectedTags") != null ? request.getParameter("selectedTags") : "" %>" type="java.lang.String"/>
<bean:define id="sortUrlParameter" value="<%= (request.getParameter("sort") == null ? "" : "&sort=" + request.getParameter("sort")) %>"/>

<logic:notEmpty name="unit" property="unitFileTags">
<fr:view name="unit" property="unitFileTags">
	<fr:layout name="tag-search">
		<fr:property name="classes" value="tcloud"/>
		<fr:property name="linkFormat" value="<%= "/" + actionName + ".do?method=viewFilesByTag&unitId=${unit.externalId}" + sortUrlParameter %>"/>
		<fr:property name="popularCount" value="10"/>
		<fr:property name="minimumLevel" value="0.4"/>
		<fr:property name="sortBy" value="name"/>
		<fr:property name="parameter" value="selectedTags"/>
		<fr:property name="selectedTags" value="<%= tags %>"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:notEmpty name="tags">
	<bean:define id="separatedTags" value="<%= tags.replace(" ", " + ") %>"/>
	<p class="mbottom05"><bean:message key="label.tagCloud.selectedTags" bundle="RESEARCHER_RESOURCES"/>: 
		<span class="color888"><fr:view name="separatedTags"/></span>
	</p>
	<p class="mtop0 mbottom2"><html:link page="<%= "/" + actionName + ".do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.tagCloud.cleanTags" bundle="RESEARCHER_RESOURCES"/></html:link> <span class="color888">(<bean:message key="label.showAllFiles" bundle="RESEARCHER_RESOURCES"/>)</span></p>	
</logic:notEmpty>

<logic:notEmpty name="files">

<bean:define id="URL" value="<%=  "/" + actionName + ".do?method=manageFiles&unitId=" + unitID%>"/>

<logic:present name="tags">
	<bean:define id="URL" value="<%= "/" + actionName + ".do?method=viewFilesByTag&selectedTags=" + tags + "&unitId=" + unitID%>"/>
</logic:present>

<bean:define id="cpURL" value="<%= "/" + module +  URL + sortUrlParameter%>"/>	

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: 
<cp:collectionPages url="<%= cpURL %>" pageNumberAttributeName="filePage" numberOfPagesAttributeName="numberOfPages"/>

		<fr:view name="files" schema="show.unit.files">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle2 thlight thnowrap"/>
				<fr:property name="columnClasses" value="bold,smalltxt,smalltxt width100px acenter nowrap,,smalltxt,smalltxt color888 nowrap,nowrap"/>				
				<fr:property name="visibleIf(delete)" value="editableByCurrentUser"/>
				<fr:property name="order(delete)" value="2"/>
				<fr:property name="key(delete)" value="label.delete" />
				<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
				<fr:property name="link(delete)" value="<%= "/" + actionName + ".do?method=deleteFile&unitId=" + unitID %>"/>
				<fr:property name="param(delete)" value="externalId/fid" />
				<fr:property name="order(edit)" value="1"/>
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
				<fr:property name="link(edit)" value="<%= "/" + actionName + ".do?method=prepareEditFile&unitId=" + unitID %>"/>
				<fr:property name="param(edit)" value="externalId/fid" />
				<fr:property name="visibleIf(edit)" value="editableByCurrentUser"/>		
				<fr:property name="sortParameter" value="sort"/>
				<fr:property name="sortIgnored" value="true"/>
				<fr:property name="sortBy" value="<%= request.getParameter("sort") == null ? "displayName" : request.getParameter("sort")%>"/>
				<fr:property name="sortableSlots" value="displayName,uploadTime"/>
				<fr:property name="sortUrl" value="<%= URL + (request.getParameter("filePage") == null ? "" : "&filePage=" + request.getParameter("filePage"))%>"/>
			</fr:layout>
		</fr:view> 

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: <cp:collectionPages url="<%= cpURL %>"
pageNumberAttributeName="filePage" numberOfPagesAttributeName="numberOfPages"/>

<p>
	<bean:message key="label.unitFileTags" bundle="RESEARCHER_RESOURCES"/>: 
	<fr:view name="unit" property="unitFileTags">
		<fr:layout name="tag-count">
			<fr:property name="linkFormat" value="<%= "/" + actionName + ".do?method=viewFilesByTag&selectedTags=${name}&unitId=${unit.externalId}" %>"/>
			<fr:property name="showAllUrl" value="<%= "/" + actionName + ".do?method=manageFiles&unitId=" + unitID%>"/>
			<fr:property name="sortBy" value="name"/>
		</fr:layout>
	</fr:view>
</p>

</logic:notEmpty>

<logic:empty name="files">

	<p>
		<em><bean:message key="label.noFilesAvailable" bundle="RESEARCHER_RESOURCES"/></em>
	</p>
</logic:empty>

