<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<h2><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></h2>

<bean:define id="unitID" name="unit" property="idInternal"/>

<logic:equal name="unit" property="currentUserAllowedToUploadFiles" value="true">
<ul>
	<li>
		<html:link page="<%= "/researchUnitFunctionalities.do?method=prepareFileUpload&unitId=" + unitID %>"><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></html:link>
	</li>
</ul>
</logic:equal>

<logic:notEmpty name="files">

<fr:view name="unit" property="unitFileTags">
	<fr:layout name="tag-cloud">
		<fr:property name="linkFormat" value="/researchUnitFunctionalities.do?method=viewFilesByTag&tagName=${name}&unitId=${unit.idInternal}"/>
		<fr:property name="classes" value="tcloud"/>
		<fr:property name="popularCount" value="10"/>
		<fr:property name="minimumLevel" value="0.4"/>
		<fr:property name="sortBy" value="name"/>
	</fr:layout>
</fr:view>

<bean:define id="cpURL" value="<%= "/researcher/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID%>"/>
<logic:present name="tagName">
	<bean:define id="tagName" name="tagName" type="java.lang.String"/>
	<bean:define id="cpURL" value="<%= "/researcher/researchUnitFunctionalities.do?method=viewFilesByTag&tagName=" + tagName + "&unitId=" + unitID%>"/>
</logic:present>

<p>
	<html:link page="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID%>">
		<bean:message key="renderers.show.all" bundle="RENDERER_RESOURCES"/>
	</html:link>
</p>

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: 
<cp:collectionPages url="<%= cpURL %>" pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>

		<fr:view name="files" schema="show.unit.files">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thnowrap"/>
				<fr:property name="columnClasses" value=",smalltxt,smalltxt width100px acenter,,,smalltxt color888,width100px"/>
				<fr:property name="visibleIf(delete)" value="editableByCurrentUser"/>
				<fr:property name="order(delete)" value="2"/>
				<fr:property name="key(delete)" value="label.delete" />
				<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
				<fr:property name="link(delete)" value="<%= "/researchUnitFunctionalities.do?method=deleteFile&unitId=" + unitID %>"/>
				<fr:property name="param(delete)" value="idInternal/fid" />
				<fr:property name="order(edit)" value="1"/>
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
				<fr:property name="link(edit)" value="<%= "/researchUnitFunctionalities.do?method=prepareEditFile&unitId=" + unitID %>"/>
				<fr:property name="param(edit)" value="idInternal/fid" />
				<fr:property name="visibleIf(edit)" value="editableByCurrentUser"/>
			</fr:layout>
		</fr:view> 

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: <cp:collectionPages url="<%= cpURL %>"
pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>

<p>
	<bean:message key="label.unitFileTags" bundle="RESEARCHER_RESOURCES"/>: 
	<fr:view name="unit" property="unitFileTags">
		<fr:layout name="tag-count">
			<fr:property name="linkFormat" value="/researchUnitFunctionalities.do?method=viewFilesByTag&tagName=${name}&unitId=${unit.idInternal}"/>
			<fr:property name="showAllUrl" value="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID%>"/>
			<fr:property name="sortBy" value="name"/>
		</fr:layout>
	</fr:view>
</p>

</logic:notEmpty>

<logic:empty name="files">
	<bean:message key="label.noFilesAvailable" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>