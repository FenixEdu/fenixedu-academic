<%@ page language="java" %>
<%@page import="org.joda.time.YearMonthDay"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messaging.tld" prefix="messaging" %>

<%
net.sourceforge.fenixedu.domain.Person person = (net.sourceforge.fenixedu.domain.Person) request.getAttribute("person");
String contextPrefix = (String) request.getAttribute("contextPrefix");
String extraParameters = (String) request.getAttribute("extraParameters");
String year =  request.getParameter("selectedYear");
String month = request.getParameter("selectedMonth");

if (month != null && year!=null)
{
%>

<p><em style="background: #fff8cc;"><%= new net.sourceforge.fenixedu.util.Mes(Integer.valueOf(month)).toString()%> de <%=year%></em></p>
<%
} else if(request.getRequestURI().contains("teacher")) {
    YearMonthDay currentDate = new YearMonthDay();
%>
	<p><em><%= new net.sourceforge.fenixedu.util.Mes(Integer.valueOf(currentDate.getMonthOfYear())).toString()%> de <%= currentDate.getYear() %></em></p>
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
				<span id="10367">
					<img src="/images/dotist_post.gif" alt="Publicar">
					<logic:notEmpty name="announcement" property="publicationBegin">
						Publicado em 
							<fr:view name="announcement" property="publicationBegin" layout="no-time"/>
						<%
						if (announcement.getAnnouncementBoard().hasWriter(person)) {
						%>
							<logic:notEmpty name="announcement" property="publicationEnd">
							 	até 
								<fr:view name="announcement" property="publicationEnd" layout="no-time"/>
							</logic:notEmpty>
						<%
						}
						%>
					</logic:notEmpty>
						
					<logic:empty name="announcement" property="publicationBegin">
						Publicado em 
						<fr:view name="announcement" property="creationDate" layout="no-time"/>
					</logic:empty>
				</span>
			</p>

		<%-- Título --%>
			<logic:equal name="announcement" property="visible" value="true">
				<h3 class="mvert025">
				<html:link action="<%=contextPrefix +extraParameters +"&method=viewAnnouncement&announcementId=" + announcement.getIdInternal()%>">
					<span><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></span>
				</html:link> 	 	
				</h3>
			</logic:equal>
			
			<logic:equal name="announcement" property="visible" value="false">
				<p class="mvert025">
				<h3 class="mvert0 dinline">
				<html:link action="<%=contextPrefix +extraParameters +"&method=viewAnnouncement&announcementId=" + announcement.getIdInternal()%>">
					<span><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></span>
				</html:link> 	 	
				</h3>
				<em class="warning1">Invisível</em>
				</p>
			</logic:equal>

		<%-- Corpo --%>
			 <logic:notPresent name="announcementBoard">
				 <p class="ann_body mvert025">
				 <% if (!announcement.isExcerptEmpty())
				 	{
				 %>				 
				 	<fr:view name="announcement" property="excerpt"/>
				 	 <html:link action="<%=contextPrefix + "method=viewAnnouncement&announcementId=" + announcement.getIdInternal()%>">
						 Continuar a ler...
					 </html:link> 
				 <%				 		
				 	}
				 	else
				 	{
				 %>
				 	<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html"/>				 	
				 <% 
				 	}
				 %>
				 </p>
			 </logic:notPresent>
				
			<logic:present name="announcementBoard">
				<p class="ann_body mvert025">
					<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html"/>
				</p>
			</logic:present>

		<p class="mvert025">
			<em class="smalltxt greytxt2" >

		<%-- Board e RSS --%>
				Canal: 
				<html:link action="<%=contextPrefix + extraParameters +"&method=viewAnnouncements&announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal() + "#" + announcement.getIdInternal()%>">
					<fr:view name="announcement" property="announcementBoard.name" type="java.lang.String"/>
				</html:link>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  

		<%-- Gerir --%>
			<%
				if (!request.getRequestURI().contains("public") && announcement.getAnnouncementBoard().hasManager(person)) {
			%>
				Permissões:
				<html:link action="<%= "/announcements/manage" + announcement.getAnnouncementBoard().getClass().getSimpleName() + ".do?method=prepareEditAnnouncementBoard" + "&announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal() + "&tabularVersion=true" %>">
				  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.manage.link"/>
				</html:link>
				 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
			<%	
				} else if (!request.getRequestURI().contains("public") && announcement.getAnnouncementBoard().hasWriter(person)) {
			%>
				
				Permissões:
				<html:link action="<%= "/announcements/manage" + announcement.getAnnouncementBoard().getClass().getSimpleName() + ".do?method=viewAnnouncements" + "&announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal() %>">
				  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.write.link"/>
				</html:link>
				 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  
			<%	
				}
			%>		

		<%-- ReferedSubject Date --%>
		
			<logic:notEmpty name="announcement" property="referedSubjectBegin">
				<logic:notEmpty name="announcement" property="referedSubjectEnd">
			 	 	Evento ocorre de 
			 		<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="dataDependent"/>
			 	 	a 
			 	 	<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="dataDependent"/>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  
				</logic:notEmpty>
						 
				<logic:empty name="announcement" property="referedSubjectEnd">
					Evento ocorre a <fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="dataDependent"/>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  
				</logic:empty>
			</logic:notEmpty>
		
		<%-- Autor --%>
				<logic:notEmpty name="announcement" property="author">
				 	<logic:notEmpty name="announcement" property="authorEmail">
				 	Autor: 
				 		<html:link href="<%="mailto:"+announcement.getAuthorEmail()%>">
						 	<fr:view name="announcement" property="author"/>
						</html:link>
					  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				 	</logic:notEmpty>
				</logic:notEmpty>
				 			 
				 <logic:notEmpty name="announcement" property="author">
				 	<logic:empty name="announcement" property="authorEmail">
					 	Autor: <fr:view name="announcement" property="author"/>
					  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				 	</logic:empty>
				 </logic:notEmpty>

		<%-- Local --%> 
				 <logic:notEmpty name="announcement" property="place">
				 	Local: <fr:view name="announcement" property="place"/> - 
					  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				 </logic:notEmpty>
				 
		<%-- Modificado em --%> 
				<%
				if (announcement.wasModifiedSinceCreation())
				{
				%>
					Modificado em:
					<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
					<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
				<%
				}
				%>
		<%-- Data de Criação --%>
				<%
				if (announcement.getAnnouncementBoard().hasWriter(person)) {
				
				%>
					Data de criação:
					<span id="<%=announcement.getIdInternal().toString()%>">
						<fr:view name="announcement" property="creationDate" layout="no-time"/>
					</span>
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
			<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal" />
			<fr:view name="announcements" schema="announcement.view-with-creationDate-subject-online">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle2"/>
			     		<fr:property name="columnClasses" value=""/>
						<fr:property name="link(edit)" value="<%= contextPrefix + "method=editAnnouncement" + "&"+extraParameters + "&tabularVersion=true"%>"/>
						<fr:property name="param(edit)" value="idInternal/announcementId,announcementBoard.idInternal/announcementBoardId"/>
						<fr:property name="key(edit)" value="messaging.edit.link"/>
						<fr:property name="bundle(edit)" value="MESSAGING_RESOURCES"/>
						<fr:property name="order(edit)" value="2"/>
						<fr:property name="link(view)" value="<%= contextPrefix + "method=viewAnnouncement"%>"/>
						<fr:property name="param(view)" value="idInternal/announcementId"/>
						<fr:property name="key(view)" value="messaging.view.link"/>
						<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>						
						<fr:property name="order(view)" value="1"/>
						<fr:property name="link(remove)" value="<%= contextPrefix + "method=deleteAnnouncement" + "&" + extraParameters + "&tabularVersion=true"%>"/>
						<fr:property name="param(remove)" value="idInternal/announcementId,announcementBoard.idInternal/announcementBoardId"/>
						<fr:property name="key(remove)" value="messaging.delete.label"/>
						<fr:property name="bundle(remove)" value="MESSAGING_RESOURCES"/>				
						<fr:property name="order(remove)" value="3"/>
						
						<fr:property name="sortUrl" value="<%= "/announcementsManagement.do?method=listAnnouncements&announcementBoardId=" + announcementBoardId + "&tabularVersion=true" %>"/>
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
			<em>Não existem anúncios para a selecção actual.</em>
		</p>
	</logic:empty>
</logic:present>

<logic:present name="archive">
	<logic:present name="announcementBoard">
		<bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
		<%
		final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context");
		final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
	    final String module = org.apache.struts.util.ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
		%>


<style type="text/css">
.asd { margin-top: 2em;}
.asd p { margin: 0.5em 0; padding: 0; }
.asd span { font-weight: bold; padding-right: 0.5em; }
</style>

		<div class="asd">
			<messaging:archive name="archive" targetUrl="<%=request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort() + context + module + contextPrefix + "method=viewArchive&announcementBoardId=" + board.getIdInternal() + "&" + extraParameters + "&" %>"/>	
		</div>

	</logic:present>
</logic:present>