<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/messaging" prefix="messaging" %>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>

<html:xhtml/>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %></bean:define>

<div class="breadcumbs mvert0">
    <a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
    <bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
    &nbsp;&gt;&nbsp;
    <a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
    <logic:present name="degree">
        <bean:define id="degreeId" name="degree" property="externalId"/>
        
        &nbsp;&gt;&nbsp;
        <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + degreeId %>">
            <bean:write name="degree" property="sigla"/>
        </html:link>
    </logic:present>
    &nbsp;&gt;&nbsp;
    <bean:message key="public.degree.information.label.announcements"  bundle="PUBLIC_DEGREE_INFORMATION" />
</div>

<!-- COURSE NAME -->
<h1>
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
</h1>

<h2 class="greytxt">
    <bean:message key="public.degree.information.label.announcements" bundle="PUBLIC_DEGREE_INFORMATION"/>
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
						<fr:property name="confirmationKey(remove)" value="message.remove.announcement.confirmation" />
						<fr:property name="confirmationBundle(remove)" value="MESSAGING_RESOURCES" />
						
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
			<em><bean:message key="label.noAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
		</p>
	</logic:empty>
</logic:present>

<logic:present name="archive">
	<logic:present name="announcementBoard">
		<bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
		<%
		final String appContext = FenixConfigurationManager.getConfiguration().appContext();
		final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
	    final String module = org.apache.struts.util.ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
		%>

		<div class="aarchives">
			<messaging:archive name="archive" targetUrl="<%=request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort() + context + module + contextPrefix + "method=viewArchive&amp;announcementBoardId=" + board.getExternalId() + "&amp;" + extraParameters + "&amp;" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=" + request.getParameter(net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME) + "&amp;" %>"/>	
		</div>

	</logic:present>
</logic:present>
