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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.messaging.Announcement"%>

<%-- <em><bean:message bundle="MESSAGING_RESOURCES" key="label.manageChannels"/></em> --%>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="messaging.annoucenment.edit.label"/></h2>

<script language="javascript">
	function set_image_size(imagetag, image) {
		var image_width = image.width;
		var image_height = image.height;
		
		if(image_width > 400 || image_height > 300) {
			var width_ratio = 400 / image_width;
			var height_ratio = 300 / image_height;

			imagetag.width = image_width * Math.min(width_ratio, height_ratio);
			imagetag.height = image_height * Math.min(width_ratio, height_ratio);
		} else {
			imagetag.width = image_width;
			imagetag.height = image_height;
		}
	}
</script>


<bean:define id="contextPrefix" name="contextPrefix" />
<bean:define id="extraParameters" name="extraParameters" />
<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>

<logic:notPresent name="returnMethod">
	<bean:define id="formAction" toScope="request"><%= "method=prepareEditAnnouncementBoard&amp;announcementBoardId=" + announcementBoardId + "&amp;" + extraParameters %></bean:define>
</logic:notPresent>
<logic:present name="returnMethod">
	<bean:define id="returnMethod" name="returnMethod"/>
	<bean:define id="formAction" toScope="request"><%= "method=" + returnMethod + "&amp;announcementBoardId=" + announcementBoardId + "&amp;" + extraParameters %></bean:define>
</logic:present>

<bean:define id="action" name="formAction" type="java.lang.String"/>

<fr:form action="<%=  contextPrefix + action %>">

<fr:edit id="announcement" name="announcement" visible="false"/>

<p class="mtop2 mbottom025"><strong><bean:message key="label.messaging.requiredFields" bundle="MESSAGING_RESOURCES"/>:</strong></p>

<table class="tstyle5 thlight thtop thright mtop025">

<%-- Título --%>
	<tr>
		<th style="width: 125px;">
			<span class="required">*</span> <bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.title.label"/>:
		</th>
		<td>
			<fr:edit id="announcement-subject-validated" name="announcement" slot="subject" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredMultiLanguageStringValidator">
				<fr:layout>
					<fr:property name="size" value="50"/>
				</fr:layout>
			</fr:edit>
			<fr:message for="announcement-subject-validated"/>
		</td>
	</tr>

<%-- Corpo --%>
	<tr>
		<th>
			<span class="required">*</span> <bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.body.label"/>:
		</th>
		<td>
			<fr:edit id="announcement-body-validated" name="announcement" slot="body" layout="area" 
					validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredMultiLanguageStringValidator">
				<fr:layout name="rich-text">
					<fr:property name="safe" value="true" />
			   		<fr:property name="columns" value="70"/>
			   		<fr:property name="rows" value="15"/>
			   		<fr:property name="config" value="advanced" />
				</fr:layout>				
			</fr:edit>
			<fr:message for="announcement-body-validated"/>
		</td>
	</tr>
	<logic:notEmpty name="announcementBoard" property="fileContentSet">
		<tr>
		<th>
			<bean:message key="label.insertFiles" bundle="MESSAGING_RESOURCES"/>
		</th>
		<td>
				<div style="height: 80px; overflow: auto; border: 1px solid #aaa; padding: 0.25em; background: #fafafa;">
				<logic:iterate id="file" name="announcementBoard" property="filesSortedByDate">
				
				
				<p><fr:view name="file" property="displayName"/>
				   <span class="color888">(<fr:view name="file" property="filename"/>)
				   <fr:view name="file" property="permittedGroup" layout="null-as-label" type="org.fenixedu.bennu.core.groups.Group">
	                                        <fr:layout>
	                                            <fr:property name="label" value="label.public"/>
	                                            <fr:property name="key" value="true"/>
	                                            <fr:property name="bundle" value="SITE_RESOURCES"/>
	                                            <fr:property name="subLayout" value="values"/>
	                                            <fr:property name="subSchema" value="permittedGroup.name"/>
	                                        </fr:layout>
	                                    </fr:view>
	                </span> - <bean:define id="downloadUrl" name="file" property="downloadUrl"/>
					<bean:define id="displayName" name="file" property="displayName"/>
                    <%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#" onclick="<%= "insertLink('" + downloadUrl + "', '"+ displayName + "');"%>"><bean:message key="link.insert.file.in.editor" bundle="SITE_RESOURCES"/></a>
                    </p>
			</logic:iterate>
			</div>
		</td>
		</tr>
		</logic:notEmpty>
</table>

<script type="text/javascript" src='<%= request.getContextPath() + "/javaScript/tinyMCEHook.js"%>'></script>
            
<p class="mtop1 mbottom025"><strong><bean:message bundle="MESSAGING_RESOURCES" key="label.optionalFields"/>:</strong></p>
<table class="tstyle5 thlight thtop thright mtop025">
<%-- Excerto --%>
	<tr>
		<th style="width: 125px;">
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.excerpt.label"/>:
		</th>
		<td>
			<fr:edit name="announcement" slot="excerpt" layout="area">
				<fr:layout>
					<fr:property name="rows" value="3"/>
					<fr:property name="columns" value="70"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>

<%-- Local --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.place.label"/>:
		</th>
		<td>
			<fr:edit name="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement" slot="place">
				<fr:layout>
					<fr:property name="size" value="30"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>

	
<%-- Palavras-chave --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.keywords.label"/>:
		</th>
		<td>
			<fr:edit name="announcement" slot="keywords">
				<fr:layout>
					<fr:property name="size" value="30"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>

<%-- Nome do autor --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.authorName.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="author">
				<fr:layout>
					<fr:property name="size" value="30"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>


<%-- E-mail do autor --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.authorEmail.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="authorEmail">
				<fr:layout>
					<fr:property name="size" value="30"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>

<%-- Início do evento --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.referedSubjectBegin.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="referedSubjectBegin"/>			
		</td>
	</tr>

<%-- Fim do evento --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.referedSubjectEnd.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="referedSubjectEnd"/>						
		</td>
	</tr>

<%-- Início de publicação --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.publicationBegin.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="publicationBegin"/>			
		</td>
	</tr>

<%-- Fim de publicação --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.publicationEnd.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="publicationEnd"/>			
		</td>
	</tr>

<%-- Visível --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.visible.label"/>:
		</th>
		<td>
			<fr:edit name="announcement" slot="visible"/>
		</td>
	</tr>
	
<%-- Categories --%>
 
 	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.categories.label"/>:
		</th>
		<td>
			<fr:edit id="categories-validated" name="announcement" slot="categories">
				<fr:layout name="option-select">
					<fr:property name="labelStyle" value="display:none"/>
				
                <fr:property name="label" value="" />
                <fr:property name="providerClass"
                        value="net.sourceforge.fenixedu.presentationTier.renderers.providers.AnnouncementCategoryProvider" />
                <fr:property name="eachSchema" value="announcement.category.name.content" />
                <fr:property name="eachLayout" value="values" />
				<fr:property name="classes" value="nobullet noindent" />
        
				</fr:layout>
			</fr:edit>
		<span class="error0"><fr:message for="categories-validated"/></span>
		</td>
	</tr>

<%-- Campus --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.campus.label"/>
		</th>
		<td>
			<fr:edit id="campus-validated" name="announcement" slot="campus">
 				<fr:layout name="menu-select">
					<fr:property name="labelStyle" value="display:none"/>
					<fr:property name="size" value="30"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager.CampusProvider"/>		
					<fr:property name="format"	value="${name}" />
				</fr:layout>
			</fr:edit>
		</td>
	</tr>

<%-- Press Release --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.pressRelease.label"/>:
		</th>
		<td>
			<fr:edit name="announcement" slot="pressRelease"/>
		</td>
	</tr>
	
<%-- Photo --%>	
 
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.photo.label"/>
		</th>
		<td>
			<span id="photoUrl">
				<fr:edit name="announcement" slot="photoUrl" visible="true">
					<fr:layout name="input-with-comment">
						<fr:property name="style" value="display: none"/>
					</fr:layout>
				</fr:edit>
			</span>
		
			<span id="photo">
			<logic:present name="announcement" property="photoUrl">
			<logic:notEmpty name="announcement" property="photoUrl">
				<bean:define id="announcementPhotoUrl" name="announcement" property="photoUrl"/>
				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><img src="<%= announcementPhotoUrl %>" />
			</logic:notEmpty>
			<logic:empty name="announcement" property="photoUrl">
				<img src="http://bogus/bogus.jpg"/>
			</logic:empty>

			<bean:define id="photoUrl" name="announcement" property="photoUrl"/>
			<script language="javascript">
				photo = new Image();
				photo.src = '<%= photoUrl %>';
				set_image_size(document.getElementById('photo').childNodes[1], photo);
			</script>
			</logic:present>

			<logic:notPresent name="announcement" property="photoUrl">
				<img src="http://bogus/bogus.jpg"/>
			</logic:notPresent>

			</span>
			
			<p id="remove-paragraph" class="mvert025" style="display: <%= ((Announcement) request.getAttribute("announcement")).getPhotoUrl() != null &&  ((Announcement) request.getAttribute("announcement")).getPhotoUrl().length() > 0 ? "block" : "none" %>">
				<a onclick="document.getElementById('remove-paragraph').setAttribute('style', 'display:none'); document.getElementById('photoUrl').childNodes[1].childNodes[1].setAttribute('value',''); document.getElementById('photo').childNodes[1].setAttribute('src', '');">Remover</a>
			</p>
		</td>	
	</tr>


	<logic:notEmpty name="announcementBoard" property="fileContentSet">
		<tr>
		<th>
			<bean:message key="label.define.image" bundle="MESSAGING_RESOURCES"/>:
		</th>
		<td>
				<div style="height: 80px; overflow: auto; padding: 0.25em;">
				<logic:iterate id="file" name="announcementBoard" property="filesSortedByDate">
                <bean:define id="downloadUrl" name="file" property="downloadUrl"/>
				<bean:define id="displayName" name="file" property="displayName"/>
				
				<% 
					final String REGEX = "^.*\\.(jpg|gif|png|jpeg)$";
					if(((String)downloadUrl).matches(REGEX)) {
				%>
				
				<div style="display: inline">
					<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %>
					
					<div class="announcement_gallery" onclick="<%= "document.getElementById('remove-paragraph').setAttribute('style', 'display:block'); document.getElementById('photoUrl').childNodes[1].childNodes[1].setAttribute('value','" + downloadUrl + "'); document.getElementById('photo').childNodes[1].setAttribute('src', '" + downloadUrl + "'); new_image = new Image(); new_image.src='" + downloadUrl + "'; set_image_size(document.getElementById('photo').childNodes[1], new_image); "%>" style="border-style:none;">
						<table>
							<tr>
							<td>
								<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><img src="<%= downloadUrl %>" style="width:40px; height:30px"/>
							</td>
							</tr>
						</table>
					</div>
                </div>
                <% } %>
                
			</logic:iterate>
			</div>
		</td>
		</tr>
	</logic:notEmpty>
	
	<%-- Editor notes --%>
	<logic:notEmpty name="announcement" property="editorNotes">
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.editorNotes.label"/>:
		</th>
		<td>
			<fr:view name="announcement" property="editorNotes"/>
		</td>
	</tr>
	</logic:notEmpty>
	
</table>


	<p>
		<fr:edit name="announcement"  slot="creator">
			<fr:hidden name="person"/>
		</fr:edit>		
	</p>
	<p>
		<fr:edit name="announcement"  slot="announcementBoard">
			<fr:hidden name="announcementBoard"/>
		</fr:edit>			
	</p>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
	</p>	
</fr:form>
