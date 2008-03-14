<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messaging.tld" prefix="messaging" %>

<html:xhtml/>

<%
net.sourceforge.fenixedu.domain.Person person = (net.sourceforge.fenixedu.domain.Person) request.getAttribute("person");
String contextPrefix = (String) request.getAttribute("contextPrefix");
String extraParameters = (String) request.getAttribute("extraParameters");
%>

<bean:define id="hasYear" value="<%= Boolean.valueOf(request.getParameter("selectedYear") != null).toString() %>" type="java.lang.String"/>
<bean:define id="hasMonth" value="<%= Boolean.valueOf(request.getParameter("selectedMonth") != null).toString()%>" type="java.lang.String"/>

<logic:equal name="hasYear" value="true">
	<logic:equal name="hasMonth" value="true">
		<p><em style="background: #fff8dd;"><%=new net.sourceforge.fenixedu.util.Mes(Integer.valueOf(request.getParameter("selectedMonth"))).toString()%>
		de <%=request.getParameter("selectedYear")%></em></p>
	</logic:equal>
</logic:equal>


<logic:present name="announcements">

	<logic:notEmpty name="announcements">
		
		<logic:iterate id="announcement" name="announcements" type="net.sourceforge.fenixedu.domain.messaging.Announcement">
		
		<div class="announcement mtop15 mbottom25">

		<%-- Event Date OR Publication Date --%>
			<p class="mvert025 smalltxt greytxt2">
			<span>
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="Publicar"/>
					<logic:notEmpty name="announcement" property="publicationBegin">
						Publicado em 
							<fr:view name="announcement" property="publicationBegin" layout="no-time"/>
							<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
								<logic:notEmpty name="announcement" property="publicationEnd">
								 	<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.until" /> 
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

		<%-- Title --%>
			<logic:equal name="announcement" property="visible" value="true">
				<h3 class="mtop0 mbottom025">
				<html:link action="<%=contextPrefix +extraParameters +"&amp;method=viewAnnouncement&amp;announcementId=" + announcement.getIdInternal()%>">
					<span><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></span>
				</html:link> 	 	
				</h3>
			</logic:equal>
			
			<logic:equal name="announcement" property="visible" value="false">
				<p class="mvert025">
				<h3 class="mvert0 dinline">
				<html:link action="<%=contextPrefix +extraParameters +"&amp;method=viewAnnouncement&amp;announcementId=" + announcement.getIdInternal()%>">
					<span><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></span>
				</html:link> 	 	
				</h3>
				<em class="warning1"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.invisible" /></em>
				</p>
			</logic:equal>

		<%-- Corpo --%>
			 <logic:notPresent name="announcementBoard">
				 <div class="ann_body mvert025">
				 <logic:equal name="announcement" property="excerptEmpty" value="false">
				 	<fr:view name="announcement" property="excerpt" layout="html"/>
				 	 <html:link action="<%=contextPrefix + "method=viewAnnouncement&amp;announcementId=" + announcement.getIdInternal()%>">
						 Continuar a ler...
					 </html:link> 
				</logic:equal>
				 <logic:equal name="announcement" property="excerptEmpty" value="true">
				 	<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html"/>				 	
				</logic:equal>
				 </div>
			 </logic:notPresent>
				
			<logic:present name="announcementBoard">
				<div class="ann_body mvert025">
				<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html"/>
				</div>
			</logic:present>

		<p class="mtop05 mbottom025">
			<em class="smalltxt greytxt2">

		<%-- Board e RSS --%>
				Canal: 
				<html:link action="<%=contextPrefix + extraParameters +"&amp;method=viewAnnouncements&amp;announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal() + "#" + announcement.getIdInternal()%>">
					<fr:view name="announcement" property="announcementBoard.name" type="java.lang.String"/>
				</html:link>
				  <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />  

		<%-- Editar, Apagar --%>
			<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
				<bean:message key="label.permissions" bundle="MESSAGING_RESOURCES"/>:
				<bean:define id="announcementId" name="announcement" property="idInternal" />
				<html:link action="<%= contextPrefix + "method=editAnnouncement&amp;announcementId="+announcementId+"&amp;"+extraParameters%>">
				  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.edit.link"/>
				</html:link>
				
				<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.comma" />
				
				<html:link action="<%= contextPrefix + "method=deleteAnnouncement&amp;announcementId="+announcementId+"&amp;"+extraParameters%>">
				  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.delete.link"/>
				</html:link>
				
				<logic:equal name="announcementBoard" property="currentUserApprover" value="true">
					<logic:equal name="announcement" property="approved" value="false">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.comma" />
							
						<html:link action="<%= contextPrefix + "method=aproveAction&amp;announcementId="+announcementId+"&amp;action=true&amp;"+extraParameters%>">
					  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.approve.link"/>
						</html:link>				 
					</logic:equal>
						
	
					<logic:equal name="announcement" property="approved" value="true">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.comma" />
							
						<html:link action="<%= contextPrefix + "method=aproveAction&amp;announcementId="+announcementId+"&amp;action=false&amp;"+extraParameters%>">
					  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.not.approve.link"/>
						</html:link>				 
					</logic:equal>
				</logic:equal>
						
				 <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />

				</logic:equal>				 

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
					<logic:equal name="announcement" property="originalVersion" value="false">
						Modificado em:
						<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
						<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
					</logic:equal> 
		<%-- CreationDate --%>
					<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
					<bean:message key="label.creationDate" bundle="MESSAGING_RESOURCES"/>
					<span id="<%="ID_" + announcement.getIdInternal().toString()%>">
						<fr:view name="announcement" property="creationDate" layout="no-time"/>
					</span>
					</logic:equal>
				</em>
			</p>
			
			</div>
			</logic:iterate>

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
		final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context");
		final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
	    final String module = org.apache.struts.util.ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
		%>

		<div class="aarchives">
			<messaging:archive name="archive" targetUrl="<%=context + module + contextPrefix + "method=viewArchive&amp;announcementBoardId=" + board.getIdInternal() + "&amp;" + extraParameters + "&amp;" %>"/>	
		</div>

	</logic:present>
</logic:present>