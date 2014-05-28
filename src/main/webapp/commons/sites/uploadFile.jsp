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

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="container" name="fileItemCreator" property="content"/>
<bean:define id="containerId" name="container" property="externalId"/>

<bean:define id="topContainerString" value="<%= "sectionID=" %>"/>
<bean:define id="selectContainerString" value="<%= "itemID=" + containerId %>"/>

<logic:equal name="container" property="class.simpleName" value="Section">
	<bean:define id="selectContainerString" value="<%= "sectionID=" + containerId %>"/>
	<bean:define id="topContainerString" value="<%= "sectionID=" + containerId %>"/>
</logic:equal>
 <logic:equal name="container" property="class.simpleName" value="Item">
	<bean:define id="sectionID" name="container" property="section.externalId"/>
	<bean:define id="topContainerString" value="<%= "sectionID=" + sectionID%>"/> 
 </logic:equal>

<h2>
	<fr:view name="container" property="name" />
</h2>

<h3>
    <bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%= actionName + "?method=section&amp;" + topContainerString + "&amp;" + context %>">
	<bean:message key="link.goBack" bundle="SITE_RESOURCES"/>
</html:link>
</li>
</ul>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<p class="mvert1">
    <span class="error0">
        <html:errors property="unableToStoreFile"/>
        <html:errors property="fileMaxSizeExceeded"/>
        <html:errors property="section" bundle="APPLICATION_RESOURCES"/>
    </span>
</p>

<div class="dinline forminline">
<fr:form action="<%= actionName + "?method=fileUpload&amp;" + context + "&amp;" + selectContainerString  %>" encoding="multipart/form-data" >
    <fr:edit id="creator" name="fileItemCreator" visible="false">
        	<fr:destination name="cancel" path="<%= String.format("%s?method=section&amp;%s&amp;%s", actionName, context, topContainerString) %>"/>
    </fr:edit>

<table class="tstyle5 thright thlight mtop025">
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName" bundle="SITE_RESOURCES"/>:</th>
		<td>
	        <fr:edit id="displayName" name="fileItemCreator" slot="displayName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.AuthorsName" bundle="SITE_RESOURCES"/>:</th>
		<td>
	        <fr:edit id="authorsName" name="fileItemCreator" slot="authorsName">
	            <fr:layout>
	                <fr:property name="size" value="40"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
    <logic:notPresent name="skipFileClassification">
        	<tr>
        		<th><bean:message key="label.teacher.siteAdministration.uploadFile.ResourceType" bundle="SITE_RESOURCES"/>:</th>
        		<td>
        	        <fr:edit id="educationalLearningResourceType" name="fileItemCreator" slot="educationalLearningResourceType">
        			<fr:layout>
        				<fr:property name="excludedValues" value="PROJECT_SUBMISSION,SITE_CONTENT"/>
        			</fr:layout>
        	        </fr:edit>
        		</td>
        	</tr>
    </logic:notPresent>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.file" bundle="SITE_RESOURCES"/>:</th>
		<td>
	    <fr:edit id="file" name="fileItemCreator" slot="file">
	        <fr:layout>
	            <fr:property name="size" value="40"/>
	            <fr:property name="fileNameSlot" value="fileName"/>
	            <fr:property name="fileSizeSlot" value="fileSize"/>
	        </fr:layout>
	    </fr:edit>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.teacher.siteAdministration.uploadFile.permissions" bundle="SITE_RESOURCES"/>:</th>
		<td>
	        <fr:edit id="permissions" name="fileItemCreator" schema="item.file.create" layout="flow">
	            <fr:layout>
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
	        </fr:edit>
		</td>
	</tr>
</table>

	<p class="mtop0 mbottom1 smalltxt"><em><bean:message key="label.allFieldsRequired" bundle="SITE_RESOURCES"/></em></p>
    
        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton dinline">
            <bean:message key="button.save"/>
        </html:submit>
	</fr:form>
</div>

<div class="dinline forminline">
	<fr:form action="<%=String.format("%s?method=section&amp;%s&amp;%s", actionName, context, topContainerString) %>" encoding="multipart/form-data">
		<html:cancel styleClass="inputbutton dinline">
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</div>
