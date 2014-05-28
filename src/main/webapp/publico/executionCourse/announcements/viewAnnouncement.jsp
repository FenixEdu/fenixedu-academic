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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="announcement">

	<bean:define id="announcement" name="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement"/>
		
	<%-- <em><bean:message key="label.communicationPortal.header" bundle="MESSAGING_RESOURCES"/></em> --%>
	<h2><bean:message key="messaging.viewAnnouncement.title" bundle="MESSAGING_RESOURCES"/></h2>

<div class="mvert2 announcement">


	<%-- Publication Date --%>
	<p class="mvert025 smalltxt greytxt1">
	<span>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="Publicar"/>
			<logic:notEmpty name="announcement" property="publicationBegin">
				Publicado em 
					<fr:view name="announcement" property="publicationBegin" layout="no-time"/>
				<logic:equal name="announcement" property="announcementBoard.currentUserWriter" value="true">
					<logic:notEmpty name="announcement" property="publicationEnd">
					 	até
						<fr:view name="announcement" property="publicationEnd" layout="no-time"/>
					</logic:notEmpty>
				</logic:equal>
			</logic:notEmpty>
				
			<logic:empty name="announcement" property="publicationBegin">
				Publicado em 
				<fr:view name="announcement" property="creationDate" layout="no-time"/>
			</logic:empty>
		</span>
	</p>
			
<%-- Título --%>
	<h3 class="mvert025">
		<b><fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/></b>
	</h3>


<%-- Body --%>
	<div class="ann_body">
		<fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html" />
	</div>

<p class="mvert025">
	<em class="smalltxt" style="color: #888;">

<%-- Canal --%>

Canal: <bean:write name="announcement" property="announcementBoard.name"/> -

<%-- Autor --%>
	
	<logic:notEmpty name="announcement" property="author">
		<logic:notEmpty name="announcement" property="authorEmail">
			Autor: 
			<html:link href="<%="mailto:"+announcement.getAuthorEmail()%>">
				<fr:view name="announcement" property="author"/>
			</html:link>
			 - 
		</logic:notEmpty>
	</logic:notEmpty>	
	

<%-- Data do Evento --%>
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<logic:notEmpty name="announcement" property="referedSubjectEnd">
				De
			</logic:notEmpty>
		</logic:notEmpty>
		
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time"/>
		</logic:notEmpty>
		
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<logic:notEmpty name="announcement" property="referedSubjectEnd">
				a
			</logic:notEmpty>
		</logic:notEmpty>				
		 
		<logic:notEmpty name="announcement" property="referedSubjectEnd">
			<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time"/>
			 - 
		</logic:notEmpty>
		
<%-- Autor --%>
	<logic:notEmpty name="announcement" property="author">
		<logic:empty name="announcement" property="authorEmail">
			Autor: <fr:view name="announcement" property="author"/>
			 - 
		</logic:empty>
	</logic:notEmpty>

<%-- Local --%>
	<logic:notEmpty name="announcement" property="place">
		Local: <fr:view name="announcement" property="place"/>
		 - 
	</logic:notEmpty>
	
<%-- Modificado em --%>
	
	<logic:equal name="announcement" property="originalVersion" value="false"> 
		Modificado em:
		<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
		 - 
	</logic:equal>
	
<%-- Data de Criação --%>
	<html:link linkName="<%= "ID_" + announcement.getExternalId().toString()%>"/>
		<bean:message key="label.creationDate" bundle="MESSAGING_RESOURCES"/>: 
		<fr:view name="announcement" property="creationDate" type="org.joda.time.DateTime" layout="no-time"/>
	</em>
</p>
</div>

</logic:present>


<logic:notPresent name="announcement">
	<bean:message key="error.cannot.display.announcement" bundle="MESSAGING_RESOURCES"/><br/>
	<bean:message key="error.not.allowed.to.view.announcement.possible.causes" bundle="MESSAGING_RESOURCES"/>
	<ul>
		<li>
			<bean:message key="error.not.allowed.to.view.announcement" bundle="MESSAGING_RESOURCES"/>
		</li>
		<li>
			<bean:message key="error.invisible.view.announcement" bundle="MESSAGING_RESOURCES"/>
		</li>
	</ul>
</logic:notPresent>