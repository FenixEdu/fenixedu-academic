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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/messaging" prefix="messaging" %>

<html:xhtml/>

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
				<html:link page="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=viewAnnouncement&announcementId=${announcement.externalId}">
					<span><fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/></span>
				</html:link> 	 	
				</h3>
			</logic:equal>
			
			<logic:equal name="announcement" property="visible" value="false">
				<p class="mvert025">
				<h3 class="mvert0 dinline">
				<html:link page="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=viewAnnouncement&announcementId=${announcement.externalId}">
					<span><fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/></span>
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
				 	 <html:link action="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=viewAnnouncement&announcementId=${announcement.externalId}">
						 Continuar a ler...
					 </html:link> 
				</logic:equal>
				 <logic:equal name="announcement" property="excerptEmpty" value="true">
				 	<fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html"/>				 	
				</logic:equal>
				 </div>
			 </logic:notPresent>
				
			<logic:present name="announcementBoard">
				<div class="ann_body mvert025">
				<fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html"/>
				</div>
			</logic:present>

		<p class="mtop05 mbottom025">
			<em class="smalltxt greytxt2">

		<%-- Editar, Apagar --%>
			<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
				<bean:message key="label.permissions" bundle="MESSAGING_RESOURCES"/>:
				<bean:define id="announcementId" name="announcement" property="externalId" />
				<html:link action="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=editAnnouncement&announcementId=${announcement.externalId}">
				  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.edit.link"/>
				</html:link>
				
				<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.comma" />
				
				<html:link action="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=deleteAnnouncement&announcementId=${announcement.externalId}">
				  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.delete.link"/>
				</html:link>
				
				<logic:equal name="announcementBoard" property="currentUserApprover" value="true">
					<logic:equal name="announcement" property="approved" value="false">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.comma" />
							
						<html:link action="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=aproveAction&announcementId=${announcement.externalId}&action=true">
					  	<bean:message bundle="MESSAGING_RESOURCES" key="messaging.approve.link"/>
						</html:link>				 
					</logic:equal>
						
	
					<logic:equal name="announcement" property="approved" value="true">
						<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.comma" />
							
						<html:link action="/announcementManagement.do?executionCourseID=${executionCourse.externalId}&method=aproveAction&announcementId=${announcement.externalId}&action=false">
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
					<span id="<%="ID_" + announcement.getExternalId().toString()%>">
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
		<div class="aarchives">
			<messaging:archive name="archive" targetUrl="${pageContext.request.contextPath}/teacher/announcementManagement.do?method=viewArchive&announcementBoardId=${board.externalId}&executionCourseID=${executionCourse.externalId}&"/>	
		</div>

	</logic:present>
</logic:present>