<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<h2><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></h2>

<bean:define id="unitID" name="unit" property="idInternal"/>

<ul>
	<li>
		<html:link page="<%= "/researchUnitFunctionalities.do?method=prepareFileUpload&unitId=" + unitID %>"><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></html:link>
	</li>
</ul>

<logic:notEmpty name="files">


<fr:view name="unit" property="unitFileTags" layout="tag-count"/>

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: 
<cp:collectionPages url="<%= "/researcher/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>" pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>

		<fr:view name="files" schema="show.unit.files">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
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

<bean:message key="label.numberPages" bundle="RESEARCHER_RESOURCES"/>: <cp:collectionPages url="<%= "/researcher/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>"
pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>

</logic:notEmpty>

<logic:empty name="files">
	<bean:message key="label.noFilesAvailable" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>