<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.domain.messaging.Announcement"%>

<em>Gest�o de Canais</em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="messaging.annoucenment.edit.label"/></h2>

<bean:define id="contextPrefix" name="contextPrefix" />
<bean:define id="extraParameters" name="extraParameters" />
<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>


<bean:define id="action"><%= "method=listAnnouncements&announcementBoardId=" + announcementBoardId + "&" + extraParameters %></bean:define>

<fr:form action="<%=  contextPrefix + action %>">


<table class="tstyle8 thlight thtop thright">

<%-- T�tulo --%>
	<tr>
		<th>
			<span class="required">*</span> <bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.title.label"/>:
		</th>
		<td>
			<fr:edit id="announcement-subject-validated" name="announcement" slot="subject" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator">
				<fr:layout>
					<fr:property name="size" value="30"/>
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
					validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator">
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


<%-- Excerto --%>
	<tr>
		<th>
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

<%-- In�cio do evento --%>
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

<%-- In�cio de publica��o --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.publicationBegin.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="publicationBegin"/>			
		</td>
	</tr>

<%-- Fim de publica��o --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.publicationEnd.label"/>:
		</th>
		<td>
			<fr:edit name="announcement"  slot="publicationEnd"/>			
		</td>
	</tr>
	
		<%-- Sticky --%>
	<tr>
		<th><bean:message bundle="MESSAGING_RESOURCES"
			key="net.sourceforge.fenixedu.domain.messaging.Announcement.sticky.label" />:
		</th>
		<td><fr:edit name="announcement" slot="sticky" /></td>
	</tr>



<%-- Vis�vel --%>
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
					<fr:property name="format"	value="${spaceInformation.presentationName}" />
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
				<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><img src="<%= announcementPhotoUrl %>" />
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


	<logic:notEmpty name="announcementBoard" property="files">
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
					<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %>
					
					<div class="announcement_gallery" onclick="<%= "document.getElementById('remove-paragraph').setAttribute('style', 'display:block'); document.getElementById('photoUrl').childNodes[1].childNodes[1].setAttribute('value','" + downloadUrl + "'); document.getElementById('photo').childNodes[1].setAttribute('src', '" + downloadUrl + "'); new_image = new Image(); new_image.src='" + downloadUrl + "'; set_image_size(document.getElementById('photo').childNodes[1], new_image); "%>" style="border-style:none;">
						<table>
							<tr>
							<td>
								<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><img src="<%= downloadUrl %>" style="width:40px; height:30px"/>
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


<script type="text/javascript">

	var form = document.forms[0];
	
	
	
	var myDate = new Date();
	var dateString =myDate.getUTCDate()+"/"+myDate.getUTCMonth()+1+"/"+myDate.getUTCFullYear()
	
	for (var i in form.elements){
		var name = form.elements[i].name
		if (name.match(/publicationBegin_date/)){
			var pubDate = name
		}
		if (name.match(/publicationBegin_hours/)){
			var hours = name
		}
		if (name.match(/publicationBegin_minutes/)){
			var minutes = name
		}
		if (name.match(/sticky/)){
			var stickyId = name
		}
			
	}
	var beginDate = form.elements[pubDate]
	var hours = form.elements[hours]
	var minutes = form.elements[minutes]
	var sticky = form.elements[stickyId]
	
	sticky.onchange = function(){if (sticky.checked == true){ beginDate.value=dateString} else {beginDate.value = "";hours.value="";minutes.value=""}};
</script>
