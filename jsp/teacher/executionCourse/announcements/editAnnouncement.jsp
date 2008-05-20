<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em>Gest�o de Canais</em>
<h2><bean:message bundle="MESSAGING_RESOURCES" key="messaging.annoucenment.edit.label"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<bean:define id="contextPrefix" name="contextPrefix" />
<bean:define id="extraParameters" name="extraParameters" />
<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>


<bean:define id="action"><%= "method=viewAnnouncements&announcementBoardId=" + announcementBoardId + "&" + extraParameters %></bean:define>

<fr:form action="<%=  contextPrefix + action %>">


<p class="mtop2 mbottom025"><strong><bean:message key="label.messaging.requiredFields" bundle="MESSAGING_RESOURCES"/>:</strong></p>

<table class="tstyle5 thlight thtop thright mtop025">

<%-- T�tulo --%>
	<tr>
		<th style="width: 125px;">
			<span class="required">*</span> <bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.title.label"/>:
		</th>
		<td>
			<fr:edit id="announcement-subject-validated" name="announcement" slot="subject" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator">
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
					validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator">
				<fr:layout name="rich-text">
					<fr:property name="safe" value="true" />
			   		<fr:property name="columns" value="70"/>
			   		<fr:property name="rows" value="15"/>
			   		<fr:property name="config" value="advanced" />
				</fr:layout>				
			</fr:edit>
			<fr:message for="announcement-body-validated" />
		</td>
	</tr>
	<logic:notEmpty name="announcementBoard" property="files">
		<tr>
		<th>
			<bean:message key="link.insertFile" bundle="SITE_RESOURCES"/>
		</th>
		<td>
				<div style="height: 80px; overflow: auto; border: 1px solid #aaa; padding: 0.25em; background: #fafafa;">
				<logic:iterate id="file" name="announcementBoard" property="filesSortedByDate">
				
				
				<p><fr:view name="file" property="displayName"/>
				   <span class="color888">(<fr:view name="file" property="filename"/>)
				   <fr:view name="file" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
	                                        <fr:layout>
	                                            <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
	                                            <fr:property name="key" value="true"/>
	                                            <fr:property name="bundle" value="SITE_RESOURCES"/>
	                                            <fr:property name="subLayout" value="values"/>
	                                            <fr:property name="subSchema" value="permittedGroup.class.text"/>
	                                        </fr:layout>
	                                    </fr:view>
	                </span> - <bean:define id="downloadUrl" name="file" property="downloadUrl"/>
					<bean:define id="displayName" name="file" property="displayName"/>
                    <a href="#" onclick="<%= "insertLink('" + downloadUrl + "', '"+ displayName + "');"%>"><bean:message key="link.insert.file.in.editor" bundle="SITE_RESOURCES"/></a>
                    </p>
			</logic:iterate>
			</div>
		</td>
		</tr>
	</logic:notEmpty>
</table>

<script type="text/javascript">
            function hasTinyMCE() {
                return tinyMCE.configs.length > 0;
            }
            
            function insertLink(url, name) {
                if (hasTinyMCE()) {
                    tinyMCE.execCommand('mceInsertContent', false, '<a href="' + url + '">' + name + '</a> ');
                }
            }
    
            if (hasTinyMCE()) {
                switchGlobal();
            }
            </script>
            
<p class="mtop1 mbottom025"><strong>Campos opcionais:</strong></p>
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

<%-- Vis�vel --%>
	<tr>
		<th>
			<bean:message bundle="MESSAGING_RESOURCES" key="net.sourceforge.fenixedu.domain.messaging.Announcement.visible.label"/>:
		</th>
		<td>
			<fr:edit name="announcement" slot="visible"/>
		</td>
	</tr>
</table>


	<p>
		<fr:edit name="announcement"  slot="creator">
			<fr:hidden name="person"/>
		</fr:edit>		

	<p>
		<fr:edit name="announcement"  slot="announcementBoard">
			<fr:hidden name="announcementBoard"/>
		</fr:edit>			
	
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
	</p>	
</fr:form>