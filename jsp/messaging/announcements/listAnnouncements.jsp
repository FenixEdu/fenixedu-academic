<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/messaging.tld" prefix="messaging"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:xhtml />


<bean:define id="hasYear" value="<%= Boolean.valueOf(request.getParameter("selectedYear") != null).toString() %>" type="java.lang.String"/>
<bean:define id="hasMonth" value="<%= Boolean.valueOf(request.getParameter("selectedMonth") != null).toString()%>" type="java.lang.String"/>


<logic:equal name="hasYear" value="true">
	<logic:equal name="hasMonth" value="true">
		<p><em style="background: #fff8dd;"><%=new net.sourceforge.fenixedu.util.Mes(Integer.valueOf(request.getParameter("selectedMonth"))).toString()%>
		de <%=request.getParameter("selectedYear")%></em></p>
	</logic:equal>
</logic:equal>

<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
<bean:define id="extraParameters" name="extraParameters" type="java.lang.String"/>

<logic:present name="announcements">

	<logic:notEmpty name="announcements">

		<bean:define id="tabularVersion" value="<%= request.getParameter("tabularVersion") != null ? Boolean.valueOf(request.getParameter("tabularVersion")).toString() : Boolean.FALSE.toString() %>"/>
	
		<logic:equal name="tabularVersion" value="false">
			<logic:iterate id="announcement" name="announcements"
				type="net.sourceforge.fenixedu.domain.messaging.Announcement">
	
	
				<bean:define id="announcementId" name="announcement" property="idInternal"/>
				<bean:define id="announcementBoardId" name="announcement" property="announcementBoard.idInternal"/>
				<bean:define id="announcementBoardClass" name="announcement" property="announcementBoard.class.simpleName"/>
	
				<div class="announcement mtop15 mbottom25">
					<%-- Event Date OR Publication Date --%>
					<p class="mvert025 smalltxt greytxt2">
						<span>
							<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="Publicar" /> 
							<logic:notEmpty name="announcement" property="publicationBegin">
								<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.published.in" />
								<fr:view name="announcement" property="publicationBegin" layout="no-time" />
			
								<logic:equal name="announcement"
									property="announcementBoard.currentUserWriter" value="true">
									<logic:notEmpty name="announcement" property="publicationEnd">
										<bean:message bundle="MESSAGING_RESOURCES"
											key="label.messaging.until" />
										<fr:view name="announcement" property="publicationEnd"
											layout="no-time" />
									</logic:notEmpty>
								</logic:equal>
							</logic:notEmpty> 
							<logic:empty name="announcement" property="publicationBegin">
								<bean:message bundle="MESSAGING_RESOURCES"
									key="label.listAnnouncements.published.in" />
								<fr:view name="announcement" property="creationDate"
									layout="no-time" />
							</logic:empty>
						</span>
					</p>
	
					<bean:define id="urlSubject" type="java.lang.String"><%= contextPrefix %><%= extraParameters %>&amp;method=viewAnnouncement&amp;announcementId=<%= announcementId %></bean:define>
					<%-- Title --%>
					<logic:equal name="announcement" property="visible" value="true">
						<h3 class="mvert025">
							<html:link action="<%= urlSubject %>">
								<span><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString" /></span>
							</html:link>
						</h3>
					</logic:equal>
					<logic:equal name="announcement" property="visible" value="false">
						<p class="mvert025">
							<h3 class="mvert0 dinline">
								<html:link action="<%= urlSubject %>">
									<span><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString" /></span>
								</html:link>
							</h3>
							<em class="warning1"><bean:message key="label.invisible" bundle="MESSAGING_RESOURCES" /></em>
						</p>
					</logic:equal>
	
					 <%-- Body --%>
					<logic:notPresent name="announcementBoard">
						<div class="ann_body mvert025">
							<logic:equal name="announcement" property="excerptEmpty" value="false">
								<fr:view name="announcement" property="excerpt" />
								<html:link action="<%= urlSubject %>">
									<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.more.information" />
								</html:link>
							</logic:equal>
							<logic:equal name="announcement" property="excerptEmpty" value="true">
								<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html" />
							</logic:equal>
						</div>
					</logic:notPresent>
					<logic:present name="announcementBoard">
						<div class="ann_body mvert025">
							<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html" />
						</div>
					</logic:present>
	
					<p class="mtop05 mbottom025">
						<em class="smalltxt greytxt2">
							<%-- Board e RSS --%>
							<bean:define id="urlRSS" type="java.lang.String"><%= contextPrefix %><%= extraParameters %>&amp;method=viewAnnouncements&amp;announcementBoardId=<%= announcementBoardId %>#<%= announcementId %></bean:define>
							<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.board" />:
							<html:link action="<%= urlRSS %>">
								<fr:view name="announcement" property="announcementBoard.name" />
							</html:link>
							<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
			
							<%-- Manage --%> 
							
							<bean:define id="contentContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY%>"/>
							<bean:define id="showWritePermission" value="true"/>
									
							<logic:equal name="contentContext" property="selectedContainer.publicAvailable" value="false">
								<logic:equal name="announcement" property="announcementBoard.currentUserManager" value="true">
									<bean:define id="urlManage" type="java.lang.String">/announcements/manage<%= announcementBoardClass %>.do?method=prepareEditAnnouncementBoard&amp;announcementBoardId=<%= announcementBoardId %>&amp;tabularVersion=true&amp;<%= extraParameters %></bean:define>
									<bean:message key="label.permissions" bundle="MESSAGING_RESOURCES" />:
								    <html:link action="<%= urlManage  %>">
										<bean:message bundle="MESSAGING_RESOURCES" key="messaging.manage.link" />
								    </html:link> 
								    <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" /> 
									<bean:define id="showWritePermission" value="false"/>
							    </logic:equal>
						    </logic:equal>
						   
							<logic:equal name="showWritePermission" value="true">
			                	<logic:equal name="contentContext" property="selectedContainer.publicAvailable" value="false">
				                	<logic:equal name="announcement" property="announcementBoard.currentUserWriter" value="true">
					                	<bean:define id="urlManageView" type="java.lang.String">/announcements/manage<%= announcementBoardClass %>.do?method=viewAnnouncements&amp;announcementBoardId=<%= announcementBoardId %>&amp;tabularVersion=true&amp;<%= extraParameters %></bean:define>
				                		<bean:message key="label.permissions" bundle="MESSAGING_RESOURCES" />:
							   			<html:link action="<%= urlManageView %>">
											<bean:message bundle="MESSAGING_RESOURCES" key="messaging.write.link" />
										</html:link> 
							 			<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
							 		</logic:equal> 
						 		</logic:equal>
						 	</logic:equal>
			 
			 				<%-- ReferedSubject Date --%>
			 				<logic:notEmpty name="announcement" property="referedSubjectBegin">
								<logic:notEmpty name="announcement" property="referedSubjectEnd">
									<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.from" />
									<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="dataDependent" />
									<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.to" />
									<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="dataDependent" />
									<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
								</logic:notEmpty>
			
								<logic:empty name="announcement" property="referedSubjectEnd">
									<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.in" />
									<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="dataDependent" />
									<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
								</logic:empty>
							</logic:notEmpty>
							
							<%-- Author --%>
							<logic:notEmpty name="announcement" property="author">
								<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.author" />:
								<fr:view name="announcement" property="author" />
								<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
							</logic:notEmpty>
			
							<%-- Local --%>
							<logic:notEmpty name="announcement" property="place">
								<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.place" />:
								<fr:view name="announcement" property="place" />
								- <bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
							</logic:notEmpty>
							
							<%-- Modified em --%>
							<logic:equal name="announcement" property="originalVersion" value="false">
								<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.modified.in" />
								<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time" />
								<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.symbol.less" />
							</logic:equal>
			
							<%-- CreationDate --%>
							<logic:equal name="announcement" property="announcementBoard.currentUserWriter" value="true">
								<bean:message key="label.creationDate" bundle="MESSAGING_RESOURCES" />:
								<span id="<%= "ID_" + announcementId %>">
									<fr:view name="announcement" property="creationDate" layout="no-time" />
								</span>
							</logic:equal>
						</em>
					</p>
				</div>
			</logic:iterate>
		</logic:equal>
		
		<logic:equal name="tabularVersion" value="true">
			<bean:define id="defaultValue" value="prepareEditAnnouncementBoard"
				toScope="request" />
		
			<bean:define id="methodWithReturn" name="defaultValue" />
			<logic:present name="returnMethod">
				<bean:define id="methodWithReturn" name="returnMethod" />
			</logic:present>

			<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal" />
			<bean:define id="urlLinkEdit" type="java.lang.String"><%= contextPrefix %>method=editAnnouncement&amp;<%= extraParameters %>&amp;tabularVersion=true</bean:define>
			<bean:define id="urlLinkView" type="java.lang.String"><%= contextPrefix %>method=viewAnnouncement&amp;<%= extraParameters %></bean:define>
			<bean:define id="urlLinkRemove" type="java.lang.String"><%= contextPrefix %>method=deleteAnnouncement&amp;<%= extraParameters %>&amp;tabularVersion=true</bean:define>
			<bean:define id="urlLinkSort" type="java.lang.String"><%= contextPrefix %>method=<%= methodWithReturn %>&amp;announcementBoardId=<%= announcementBoardId %>&amp;tabularVersion=true&amp;<%= extraParameters %></bean:define>
			<fr:view name="announcements"
				schema="announcement.view-with-creationDate-subject-online">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle2" />
					<fr:property name="columnClasses"
						value=",nowrap,acenter,acenter,nowrap" />
					<fr:property name="link(edit)" value="<%= urlLinkEdit %>" />
					<fr:property name="param(edit)"
						value="idInternal/announcementId,announcementBoard.idInternal/announcementBoardId" />
					<fr:property name="key(edit)" value="messaging.edit.link" />
					<fr:property name="bundle(edit)" value="MESSAGING_RESOURCES" />
					<fr:property name="order(edit)" value="2" />
					<fr:property name="link(view)" value="<%= urlLinkView %>" />
					<fr:property name="param(view)" value="idInternal/announcementId" />
					<fr:property name="key(view)" value="messaging.view.link" />
					<fr:property name="bundle(view)" value="MESSAGING_RESOURCES" />
					<fr:property name="order(view)" value="1" />
					<fr:property name="link(remove)" value="<%= urlLinkRemove %>" />
					<fr:property name="param(remove)"
						value="idInternal/announcementId,announcementBoard.idInternal/announcementBoardId" />
					<fr:property name="key(remove)" value="messaging.delete.label" />
					<fr:property name="bundle(remove)" value="MESSAGING_RESOURCES" />
					<fr:property name="order(remove)" value="3" />

					<fr:property name="link(approve)" value="<%= contextPrefix + "method=aproveAction&amp;announcementId=${idInternal}&amp;action=true&amp;"+extraParameters+"&amp;tabularVersion=true" %>" />
					<fr:property name="param(approve)" value="idInternal/announcementId,announcementBoard.idInternal/announcementBoardId" />
					<fr:property name="key(approve)" value="messaging.approve.link" />
					<fr:property name="bundle(approve)" value="MESSAGING_RESOURCES" />
					<fr:property name="order(approve)" value="4" />
					<fr:property name="visibleIf(approve)" value="canApproveUser" />

					<fr:property name="link(notApprove)" value="<%= contextPrefix + "method=aproveAction&amp;announcementId=${idInternal}&amp;action=false&amp;"+extraParameters+"&amp;tabularVersion=true" %>" />
					<fr:property name="param(notApprove)" value="idInternal/announcementId,announcementBoard.idInternal/announcementBoardId" />
					<fr:property name="key(notApprove)" value="messaging.not.approve.link" />
					<fr:property name="bundle(notApprove)" value="MESSAGING_RESOURCES" />
					<fr:property name="order(notApprove)" value="5" />
					<fr:property name="visibleIf(notApprove)" value="canNotApproveUser" />
		
					<fr:property name="sortUrl" value="<%= urlLinkSort  %>" />
					<fr:property name="sortParameter" value="sortBy" />
					<fr:property name="sortBy"
						value="<%= request.getParameter("sortBy") != null ?  request.getParameter("sortBy") : "creationDate=desc"%>" />
				</fr:layout>
			</fr:view>
		</logic:equal>
	</logic:notEmpty>

	<logic:empty name="announcements">
		<p class="mtop2"><em><bean:message
			key="label.noAnnouncements" bundle="MESSAGING_RESOURCES" /></em></p>
	</logic:empty>
</logic:present>

<logic:present name="archive">
	<logic:present name="announcementBoard">
		<bean:define id="board" name="announcementBoard"
			type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard" />
		<%
				    final String module = org.apache.struts.util.ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
		%>


		<div class="aarchives">
			<bean:define id="urlTarget" type="java.lang.String"><%= request.getContextPath() %><%= module %><%= contextPrefix %>method=viewArchive&amp;announcementBoardId=<%= board.getIdInternal().toString() %>&amp;<%= extraParameters %>&amp;</bean:define>
			<messaging:archive name="archive" targetUrl="<%= urlTarget %>" />
		</div>

	</logic:present>
</logic:present>
