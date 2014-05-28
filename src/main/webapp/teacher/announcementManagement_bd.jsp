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

<h2><bean:message key="label.announcements" /></h2>

<span class="error"><!-- Error messages go here --><html:errors />
	<logic:present name="errors">
		<bean:write name="errors" filter="true" />
	</logic:present	>
</span>
<table width="100%">
<tr>
<td class="infoop"><bean:message key="label.announcement.explanation" /></td>
</tr>
</table>
<br />
<logic:present name="siteView"> 
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="announcementsList" name="bodyComponent" property="announcements"/>
<table width="100%">
    <html:form action="/announcementManagementAction">
            <tr>
                <td>
					<div class="gen-button">
						<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
						<html:link page="<%= "/announcementManagementAction.do?method=prepareCreateAnnouncement&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
							<bean:message key="label.insertAnnouncement" />
						</html:link></div>
					<br />
					<br />
                </td>
            </tr>
            
            <logic:iterate id="announcement" name="announcementsList" >
                <tr>
                    <td>
                        <strong><bean:write name="announcement" property="title"/></strong>&nbsp;&nbsp;<span class="greytxt">(<dt:format pattern="dd-MM-yyyy HH:mm:ss"><bean:write name="announcement" property="lastModifiedDate.time"/></dt:format>)</span>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information" filter="false"/>
                        <br />
                        <br />
                    </td>
                </tr>
                <tr>
                    <td>
						<div class="gen-button">
							<bean:define id="announcementCode" name="announcement" property="externalId" />
							<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
							<html:link page="<%= "/announcementManagementAction.do?method=prepareEditAnnouncement&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;announcementCode=" + announcementCode %>">
								<bean:message key="button.edit" /> 
							</html:link>&nbsp;<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
							<html:link page="<%= "/announcementManagementAction.do?method=deleteAnnouncement&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;announcementCode=" + announcementCode %>" onclick="return confirm('Tem a certeza que deseja apagar este anúncio?')">
								<bean:message key="button.delete" />
							</html:link></div>
    	                <br />
    	                <br />
                    </td>
                </tr>
            </logic:iterate>
    </html:form>
</table>
</logic:present> 