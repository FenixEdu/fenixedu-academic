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