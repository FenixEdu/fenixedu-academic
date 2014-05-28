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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="announcements">
		<p><em><bean:message key="message.announcements.not.available" /></em></p>
	</logic:empty>
		
	<logic:notEmpty name="component" property="announcements">
		<h2><bean:message key="label.announcements"/></h2>
		<logic:iterate id="announcement" name="component" property="announcements" >	
			<bean:define id="announcementId" name ="announcement" property="externalId" />
			<div class="info-lst" id="a<%= announcementId.toString() %>">
				<h3>
					<a class="info-title">
						<bean:write name="announcement" property="title"/>
					</a>
					<br />
					<span class="greytxt">
						<dt:format pattern="dd/MM/yyyy HH:mm">
							<bean:write name="announcement" property="lastModifiedDate.time"/>
						</dt:format>
					</span>
				</h3>
                 
				<p><bean:write name="announcement" property="information" filter="false"/></p>
            </div>       
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="siteView" property="component">
	<p><em><bean:message key="message.announcements.not.available" /></em></p>
</logic:notPresent>
