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

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>

<h2 class="mtop15"><bean:message key="label.messaging.events.title" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable" toScope="request"/>

<logic:present name="announcement">
<bean:define id="announcementID" name="announcement" property="externalId"/>

<h3 class="mvert025">
	<fr:view name="announcement" property="subject"/>
</h3>

<p class="mvert025 smalltxt greytxt2">
	<logic:present name="announcement" property="referedSubjectBegin">
		<logic:present name="announcement" property="referedSubjectEnd">
			<bean:message key="label.listAnnouncements.event.occurs.from" bundle="MESSAGING_RESOURCES"/>
		</logic:present>
		<logic:notPresent name="announcement" property="referedSubjectEnd">
			<bean:message key="label.listAnnouncements.event.occurs.at" bundle="MESSAGING_RESOURCES"/>
		</logic:notPresent>
        <fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time" />
	</logic:present>
	<logic:present name="announcement" property="referedSubjectEnd">
		<bean:message key="label.listAnnouncements.event.occurs.to" bundle="MESSAGING_RESOURCES"/>
		<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time" />
	</logic:present>
	<logic:notEmpty name="announcement" property="place">
	/ <fr:view name="announcement" property="place"/>
	</logic:notEmpty>
</p>
	
<div class="ann_body mvert05">
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