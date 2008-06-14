<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
	   
	   <fr:destination name="files.view" path="<%= "/viewFiles.do?method=viewFiles&unitId=${unit.idInternal}" %>"/>
	</fr:view>
</logic:notEmpty>