<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<bean:define id="unitID" name="unit" property="externalId"/>

<em>
	<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.portal"/>
</em>

<h2>
	<bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/>
	<bean:message key="of" bundle="APPLICATION_RESOURCES"/>
	<fr:view name="unit" property="nameI18n"/>
</h2>


<ul>
	<logic:equal name="unit" property="currentUserAllowedToUploadFiles" value="true">
		<li>
			<html:link page="<%= "/viewFiles.do?method=prepareFileUpload&unitId=" + unitID %>"><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></html:link>
		</li>
	</logic:equal>
	<li>
		<a href="#" onclick="switchDisplay('instructions');">Instruções</a>
	</li>
</ul>

<div id="instructions" class="switchNone">
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
		<fr:property name="linkFormat" value="<%= "/viewFiles.do?method=viewFilesByTag&unitId=${unit.externalId}" + sortUrlParameter %>"/>
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
	<p class="mtop0 mbottom2"><html:link page="<%= "/viewFiles.do?method=viewFiles&unitId=" + unitID %>"><bean:message key="label.tagCloud.cleanTags" bundle="RESEARCHER_RESOURCES"/></html:link> <span class="color888">(<bean:message key="label.showAllFiles" bundle="RESEARCHER_RESOURCES"/>)</span></p>	
</logic:notEmpty>

<logic:notEmpty name="files">

<bean:define id="URL" value="<%=  "/viewFiles.do?method=viewFiles&unitId=" + unitID%>"/>

<logic:present name="tags">
	<bean:define id="URL" value="<%= "/viewFiles.do?method=viewFilesByTag&selectedTags=" + tags + "&unitId=" + unitID%>"/>
</logic:present>

<bean:define id="cpURL" value="<%= "/messaging" +  URL + sortUrlParameter%>"/>	

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: 
<cp:collectionPages url="<%= cpURL %>" pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>

		<fr:view name="files" schema="show.unit.files.noPermission">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle2 thlight"/>
				<fr:property name="columnClasses" value="bold nowrap,smalltxt,smalltxt width100px acenter nowrap,,smalltxt,smalltxt color888 nowrap,nowrap"/>
				<fr:property name="sortParameter" value="sort"/>
				<fr:property name="sortIgnored" value="true"/>
				<fr:property name="sortBy" value="<%= request.getParameter("sort") == null ? "displayName" : request.getParameter("sort")%>"/>
				<fr:property name="sortableSlots" value="displayName,uploadTime"/>
				<fr:property name="sortUrl" value="<%= URL + (request.getParameter("page") == null ? "" : "&page=" + request.getParameter("page"))%>"/>
			</fr:layout>
		</fr:view> 

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: <cp:collectionPages url="<%= cpURL %>"
pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>

<p>
	<bean:message key="label.unitFileTags" bundle="RESEARCHER_RESOURCES"/>: 
	<fr:view name="unit" property="unitFileTags">
		<fr:layout name="tag-count">
			<fr:property name="linkFormat" value="<%= "/viewFiles.do?method=viewFilesByTag&selectedTags=${name}&unitId=${unit.externalId}" %>"/>
			<fr:property name="showAllUrl" value="<%= "/viewFiles.do?method=viewFiles&unitId=" + unitID%>"/>
			<fr:property name="sortBy" value="name"/>
		</fr:layout>
	</fr:view>
</p>

</logic:notEmpty>

<logic:empty name="files">
	<bean:message key="label.noFilesAvailable" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
