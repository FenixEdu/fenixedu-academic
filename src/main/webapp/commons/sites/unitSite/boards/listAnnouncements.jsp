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

<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/messaging" prefix="messaging" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>

<h2 class="mtop15"><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable"/>
<bean:define id="context" name="extraParameters"/>

<logic:present name="archiveDate">
    <p>
        <span class="warning0">
            <bean:message key="label.messaging.archive.selected" bundle="MESSAGING_RESOURCES"/>
            <fr:view name="archiveDate">
                <fr:layout>
                    <fr:property name="format" value="MMMM yyyy"/>
                </fr:layout>
            </fr:view>
        </span>
    </p>
</logic:present>

<logic:empty name="announcements">
	<p><em><bean:message key="label.noAnnouncements" bundle="MESSAGING_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="announcements">
<logic:iterate id="announcement" name="announcements">
	<bean:define id="announcementID" name="announcement" property="externalId"/>
	
	<div class="announcement mtop15 mbottom25">
		<p class="mvert025 smalltxt greytxt2">
			<img src="<%= request.getContextPath() + "/images/dotist_post.gif"%>"/>
			<bean:message key="label.listAnnouncements.published.in" bundle="MESSAGING_RESOURCES"/>:
			<logic:present name="announcement" property="publicationBegin">
				<fr:view name="announcement" property="publicationBegin" layout="no-time"/>
			</logic:present>
			<logic:notPresent name="announcement" property="publicationBegin">
				<fr:view name="announcement" property="creationDate" layout="no-time"/>
			</logic:notPresent>
		</p>
		
		<h3 class="mvert025">
			<html:link page="<%= String.format("%s?method=viewAnnouncement&amp;%s&amp;announcementId=%s", action, context, announcementID) %>">
				<fr:view name="announcement" property="subject"/>
			</html:link>
		</h3>
		
		<div class="usitebody mvert025">
			<fr:view name="announcement" property="body" layout="html"/>
		</div>
		
		<p>
			<em class="color888 smalltxt">
				<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
				<logic:notEmpty name="announcement" property="authorEmail">
					<bean:define id="authorEmail" name="announcement" property="authorEmail"/>
					<a href="mailto:<%= authorEmail %>">
						<fr:view name="announcement" property="author"/>
					</a>
				</logic:notEmpty>
				<logic:empty name="announcement" property="authorEmail">
					<fr:view name="announcement" property="author"/>
				</logic:empty>
			</em>
		</p>
	</div>
</logic:iterate>
</logic:notEmpty>

<logic:present name="archive">
    <logic:present name="announcementBoard">
        <bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>

        <div class="aarchives">
            <messaging:archive name="archive" targetUrl="<%= request.getContextPath() + "/publico" + action + "?method=viewArchive&amp;announcementBoardId=" + board.getExternalId() + "&amp;" + context + "&amp;" %>"/>  
        </div>

    </logic:present>
</logic:present>
