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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/messaging" prefix="messaging" %>

<html:xhtml/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link href="<%= institutionUrl + structureUrl %>">
        <bean:message key="structure" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="/department/showDepartments.faces">
        <bean:message key="academic.units" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="unitId" name="unit" property="externalId"/>
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="realName"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="label.announcements" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <fr:view name="department" property="realName"/>
</h1>

<h2 class="greytxt">
    <bean:message key="public.degree.information.label.events" bundle="PUBLIC_DEGREE_INFORMATION"/>
</h2>

<%
net.sourceforge.fenixedu.domain.Person person = (net.sourceforge.fenixedu.domain.Person) request.getAttribute("person");
String contextPrefix = (String) request.getAttribute("contextPrefix");
String extraParameters = (String) request.getAttribute("extraParameters");
String year =  request.getParameter("selectedYear");
String month = request.getParameter("selectedMonth");

if (month != null && year!=null)
{
%>
<p><em style="background: #fff8dd;"><%= new net.sourceforge.fenixedu.util.Mes(Integer.valueOf(month)).toString()%> de <%=year%></em></p>
<%
}
%>

<logic:present name="announcements">

	<logic:notEmpty name="announcements">

		<%
		if (request.getParameter("tabularVersion") == null)
		{
		%>
		
		<logic:iterate id="announcement" name="announcements" type="net.sourceforge.fenixedu.domain.messaging.Announcement">
		
		<div class="announcement mtop15 mbottom25">

		<%-- Event Date OR Publication Date --%>
			<p class="mvert025 smalltxt greytxt2">
				<span>
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="Publicar"/>
					<logic:notEmpty name="announcement" property="publicationBegin">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.published.in" /> 
							<fr:view name="announcement" property="publicationBegin" layout="no-time"/>
						<%
						if (announcement.getAnnouncementBoard().hasWriter(person)) {
						%>
							<logic:notEmpty name="announcement" property="publicationEnd">
								&nbsp;
								<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.until" />
								&nbsp;
								<fr:view name="announcement" property="publicationEnd" layout="no-time"/>
							</logic:notEmpty>
						<%
						}
						%>
					</logic:notEmpty>
						
					<logic:empty name="announcement" property="publicationBegin">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.published.in" /> 
						<fr:view name="announcement" property="creationDate" layout="no-time"/>
					</logic:empty>
				</span>
			</p>

		<%-- Title --%>
			<logic:equal name="announcement" property="visible" value="true">
				<h3 class="mvert025">
				<html:link action="<%=contextPrefix +extraParameters +"&amp;method=viewAnnouncement&amp;announcementId=" + announcement.getExternalId()%>">
					<span id="<%="ID_" + announcement.getExternalId().toString()%>">
						<fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
					</span>
				</html:link> 	 	
				</h3>
			</logic:equal>
			
			<logic:equal name="announcement" property="visible" value="false">
				<p class="mvert025">
				<h3 class="mvert0 dinline">
				<html:link action="<%=contextPrefix +extraParameters +"&amp;method=viewAnnouncement&amp;announcementId=" + announcement.getExternalId()%>">
					<span id="<%="ID_" + announcement.getExternalId().toString()%>">
						<fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
					</span>
				</html:link> 	 	
				</h3>
				<em class="warning1"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.invisible" /></em>
				</p>
			</logic:equal>

		<%-- Corpo --%>
			 <logic:notPresent name="announcementBoard">
				 <div class="ann_body mvert025">
				 <% if (!announcement.isExcerptEmpty())
				 	{
				 %>				 
				 	<fr:view name="announcement" property="excerpt"/>
				 	 <html:link action="<%=contextPrefix + "method=viewAnnouncement&amp;announcementId=" + announcement.getExternalId()%>">
				 	 	 <bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.more.information" /> 
					 </html:link> 
				 <%				 		
				 	}
				 	else
				 	{
				 %>
				 	<fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html"/>				 	
				 <% 
				 	}
				 %>
				 </div>
			 </logic:notPresent>
				
			<logic:present name="announcementBoard">
				<div class="ann_body mvert025">
					<fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html"/>
				</div>
			</logic:present>

		<p class="mtop05 mbottom025">
			<em class="smalltxt greytxt2" >

		<%-- Board e RSS --%>
				<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board" />: 
				<html:link action="<%=contextPrefix + extraParameters +"&amp;method=viewAnnouncements&amp;announcementBoardId=" + announcement.getAnnouncementBoard().getExternalId() + "#" + announcement.getExternalId()%>">
					<fr:view name="announcement" property="announcementBoard.name" type="java.lang.String"/>
				</html:link>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  

		<%-- ReferedSubject Date --%>
		
			<logic:notEmpty name="announcement" property="referedSubjectBegin">
				<logic:notEmpty name="announcement" property="referedSubjectEnd">
					<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.from" />
			 		<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="dataDependent"/>
			 		 <bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.to" /> 
			 	 	<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="dataDependent"/>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  
				</logic:notEmpty>
						 
				<logic:empty name="announcement" property="referedSubjectEnd">
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.in" /> <fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="dataDependent"/>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				</logic:empty>
			</logic:notEmpty>
		
		<%-- Autor --%>
				<logic:notEmpty name="announcement" property="author">
					  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.author" />: <fr:view name="announcement" property="author"/>
					  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				</logic:notEmpty>

		<%-- Local --%> 
				 <logic:notEmpty name="announcement" property="place">
				 	<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.place" />: <fr:view name="announcement" property="place"/> <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
					  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				 </logic:notEmpty>
				 
		<%-- Modificado em --%> 
				<%
				if (announcement.wasModifiedSinceCreation())
				{
				%>
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.modified.in" /> 
					<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				<%
				}
				%>
		<%-- CreationDate --%>
				<%
				if (announcement.getAnnouncementBoard().hasWriter(person)) {
				
				%>
					<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.creationDate" />: 
					<fr:view name="announcement" property="creationDate" layout="no-time"/>					
				<%
				}
				%>
				</em>
			</p>
			</div>
			</logic:iterate>
		<%	
		}
		else
		{
		%>
			<bean:define id="sortCriteria" name="sortBy" type="java.lang.String" />
			<bean:define id="announcementBoardId" name="announcementBoard" property="externalId" />
			<fr:view name="announcements" schema="announcement.view-with-creationDate-subject-online">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle2"/>
			     		<fr:property name="columnClasses" value=""/>
						<fr:property name="link(edit)" value="<%= contextPrefix + "method=editAnnouncement" + "&amp;"+extraParameters + "&amp;tabularVersion=true"%>"/>
						<fr:property name="param(edit)" value="externalId/announcementId,announcementBoard.externalId/announcementBoardId"/>
						<fr:property name="key(edit)" value="messaging.edit.link"/>
						<fr:property name="bundle(edit)" value="MESSAGING_RESOURCES"/>
						<fr:property name="order(edit)" value="2"/>
						<fr:property name="link(view)" value="<%= contextPrefix + "method=viewAnnouncement"%>"/>
						<fr:property name="param(view)" value="externalId/announcementId"/>
						<fr:property name="key(view)" value="messaging.view.link"/>
						<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>						
						<fr:property name="order(view)" value="1"/>
						<fr:property name="link(remove)" value="<%= contextPrefix + "method=deleteAnnouncement" + "&amp;" + extraParameters + "&amp;tabularVersion=true"%>"/>
						<fr:property name="param(remove)" value="externalId/announcementId,announcementBoard.externalId/announcementBoardId"/>
						<fr:property name="key(remove)" value="messaging.delete.label"/>
						<fr:property name="bundle(remove)" value="MESSAGING_RESOURCES"/>				
						<fr:property name="order(remove)" value="3"/>
						
						<fr:property name="sortUrl" value="<%= "/announcements/manageUnitAnnouncementBoard.do?method=prepareEditAnnouncementBoard&amp;announcementBoardId=" + announcementBoardId + "&amp;tabularVersion=true" %>"/>
						<fr:property name="sortParameter" value="sortBy"/>
						<fr:property name="sortBy" value="<%= sortCriteria %>"/>
				</fr:layout>
			</fr:view>
		<%
		}
		%>
	</logic:notEmpty>
	
	<logic:empty name="announcements">
		<p class="mtop2">
			<em><bean:message key="public.degree.information.label.noEvents" bundle="PUBLIC_DEGREE_INFORMATION"/></em>
		</p>
	</logic:empty>
</logic:present>

<logic:present name="archive">
	<logic:present name="announcementBoard">
		<bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
		<%
	    final String module = org.apache.struts.util.ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
		%>

		<div class="aarchives">
			<messaging:archive name="archive" targetUrl="<%=request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort() + request.getContextPath() + module + contextPrefix + "method=viewArchive&amp;announcementBoardId=" + board.getExternalId() + "&amp;" + extraParameters + "&amp;" %>"/>	
		</div>

	</logic:present>
</logic:present>
