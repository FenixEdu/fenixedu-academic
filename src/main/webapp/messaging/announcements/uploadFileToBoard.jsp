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


<jsp:include flush="true" page="/messaging/context.jsp"/>

	<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoard" name="bean" property="fileHolder" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>		

<h2>
	<fr:view name="announcementBoard" property="name" />
</h2>

<h3>
	<bean:message key="label.files.management" bundle="MESSAGING_RESOURCES"/>
</h3>

<ul>
<li>
<html:link page="<%=  contextPrefix + "method=viewAnnouncements&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>">
	<bean:message key="link.goBack" bundle="SITE_RESOURCES"/>
</html:link>
</li>
<li>
	<a data-toggle="collapse" data-target="#insertFile" href="#">
		<bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/>
	</a>
</li>
</ul>


<div id="insertFile" class="collapse">
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
		        <html:errors property="section" bundle="APPLICATION_RESOURCES"/>
		    </span>
		</p>

		<div class="dinline forminline">
		<strong><bean:message key="title.item.file.upload" bundle="SITE_RESOURCES"/></strong>:

		<fr:form action="<%= contextPrefix + "method=fileUpload&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>" encoding="multipart/form-data" >
		    <fr:edit id="creator" name="bean" visible="false">
		        	<fr:destination name="cancel" path="<%= String.format("%s?method=viewAnnouncements&amp;announcementBoardId=%s&amp;%s", contextPrefix, announcementBoardId, extraParameters) %>"/>
		    </fr:edit>
		
		<table class="tstyle5 thright thlight mtop025">
			<tr>
				<th><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName" bundle="SITE_RESOURCES"/>:</th>
				<td>
			        <fr:edit id="displayName" name="bean" slot="displayName">
			            <fr:layout>
			                <fr:property name="size" value="40"/>
			            </fr:layout>
			        </fr:edit>
			        <p class="smalltxt color888 mvert0"><bean:message key="label.small.file.description" bundle="SITE_RESOURCES"/>.</p>
				</td>
			</tr>
			<tr>
				<th><bean:message key="label.teacher.siteAdministration.uploadFile.file" bundle="SITE_RESOURCES"/>:</th>
				<td>
			    <fr:edit id="file" name="bean" slot="file">
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
			        <fr:edit id="permissions" name="bean" schema="fileContent.permissions" layout="flow">
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
</div>

<p>
<fr:view name="announcementBoard" property="fileContentSet" schema="site.item.file.advanced" layout="tabular">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="order(edit)" value="1"/>
		<fr:property name="link(edit)" value="<%= contextPrefix + "method=editFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>"/>
		<fr:property name="param(edit)" value="externalId/fileId"/>
		<fr:property name="key(edit)" value="link.edit"/>
		<fr:property name="bundle(edit)" value="SITE_RESOURCES"/>
		<fr:property name="order(remove)" value="2"/>
		<fr:property name="link(remove)" value="<%= contextPrefix + "method=deleteFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>"/>
		<fr:property name="param(remove)" value="externalId/fileId"/>
		<fr:property name="key(remove)" value="link.delete"/>
		<fr:property name="bundle(remove)" value="SITE_RESOURCES"/> 
	</fr:layout>
</fr:view>	
</p>




	
