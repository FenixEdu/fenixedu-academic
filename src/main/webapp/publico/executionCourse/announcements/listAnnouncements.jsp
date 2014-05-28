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
<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/messaging" prefix="messaging"%>

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

	<bean:define id="tabularVersion" value="<%= request.getParameter("tabularVersion") != null ? Boolean.valueOf(request.getParameter("tabularVersion")).toString()  : Boolean.FALSE.toString() %>"/>
	
		<logic:equal name="tabularVersion" value="false">
		<logic:iterate id="announcement" name="announcements"
			type="net.sourceforge.fenixedu.domain.messaging.Announcement">


			<bean:define id="announcementId" name="announcement" property="externalId"/>
			<bean:define id="announcementBoardId" name="announcement" property="announcementBoard.externalId"/>
			<bean:define id="announcementBoardClass" name="announcement" property="announcementBoard.class.simpleName"/>

			<div class="announcement mtop15 mbottom25"><%-- Event Date OR Publication Date --%>
			<p class="mvert025 smalltxt greytxt2"><span> <img
				src="<%= request.getContextPath() %>/images/dotist_post.gif"
				alt="Publicar" /> <logic:notEmpty name="announcement"
				property="publicationBegin">
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.listAnnouncements.published.in" />
				<fr:view name="announcement" property="publicationBegin"
					layout="no-time" />

				<logic:equal name="announcement"
					property="announcementBoard.currentUserWriter" value="true">
					<logic:notEmpty name="announcement" property="publicationEnd">
						<bean:message bundle="MESSAGING_RESOURCES"
							key="label.messaging.until" />
						<fr:view name="announcement" property="publicationEnd"
							layout="no-time" />
					</logic:notEmpty>
				</logic:equal>
			</logic:notEmpty> <logic:empty name="announcement" property="publicationBegin">
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.listAnnouncements.published.in" />
				<fr:view name="announcement" property="creationDate"
					layout="no-time" />
			</logic:empty> </span></p>

			<%-- Title --%> <logic:equal name="announcement" property="visible"
				value="true">
				<h3 class="mvert025"><html:link
					action="<%=contextPrefix +extraParameters +"&amp;method=viewAnnouncement&amp;announcementId=" + announcementId%>">
					<span><fr:view name="announcement" property="subject"
						type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" /></span>
				</html:link></h3>
			</logic:equal> <logic:equal name="announcement" property="visible" value="false">
				<p class="mvert025">
				<h3 class="mvert0 dinline"><html:link
					action="<%=contextPrefix +extraParameters +"&amp;method=viewAnnouncement&amp;announcementId=" + announcementId%>">
					<span><fr:view name="announcement" property="subject"
						type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" /></span>
				</html:link></h3>
				<em class="warning1"><bean:message key="label.invisible"
					bundle="MESSAGING_RESOURCES" /></em>
				</p>
			</logic:equal> <%-- Body --%> <logic:notPresent name="announcementBoard">
				<div class="ann_body mvert025"><logic:equal
					name="announcement" property="excerptEmpty" value="false">
					<fr:view name="announcement" property="excerpt" />
					<html:link
						action="<%=contextPrefix + "method=viewAnnouncement&amp;announcementId=" + announcementId%>">
						<bean:message bundle="MESSAGING_RESOURCES"
							key="label.listAnnouncements.more.information" />
					</html:link>
				</logic:equal> <logic:equal name="announcement" property="excerptEmpty"
					value="true">
					<fr:view name="announcement" property="body"
						type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"
						layout="html" />
				</logic:equal></div>
			</logic:notPresent> <logic:present name="announcementBoard">
				<div class="ann_body mvert025"><fr:view name="announcement"
					property="body"
					type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"
					layout="html" /></div>
			</logic:present>

			<p class="mtop05 mbottom025"><em class="smalltxt greytxt2">
			<%-- Board e RSS --%> <bean:message bundle="MESSAGING_RESOURCES"
				key="label.messaging.board" />: <html:link
				action="<%=contextPrefix + extraParameters +"&amp;method=viewAnnouncements&amp;announcementBoardId=" + announcementBoardId + "#" + announcementId%>">
				<fr:view name="announcement" property="announcementBoard.name" />
			</html:link> <bean:message bundle="MESSAGING_RESOURCES"
				key="label.messaging.symbol.less" /> 
 
 <%-- ReferedSubject Date --%> <logic:notEmpty name="announcement"
				property="referedSubjectBegin">
				<logic:notEmpty name="announcement" property="referedSubjectEnd">
					<bean:message bundle="MESSAGING_RESOURCES"
						key="label.listAnnouncements.event.occurs.from" />
					<fr:view name="announcement" property="referedSubjectBegin"
						type="org.joda.time.DateTime" layout="dataDependent" />
					<bean:message bundle="MESSAGING_RESOURCES"
						key="label.listAnnouncements.event.occurs.to" />
					<fr:view name="announcement" property="referedSubjectEnd"
						type="org.joda.time.DateTime" layout="dataDependent" />
					<bean:message bundle="MESSAGING_RESOURCES"
						key="label.messaging.symbol.less" />
				</logic:notEmpty>

				<logic:empty name="announcement" property="referedSubjectEnd">
					<bean:message bundle="MESSAGING_RESOURCES"
						key="label.listAnnouncements.event.occurs.in" />
					<fr:view name="announcement" property="referedSubjectBegin"
						type="org.joda.time.DateTime" layout="dataDependent" />
					<bean:message bundle="MESSAGING_RESOURCES"
						key="label.messaging.symbol.less" />
				</logic:empty>
			</logic:notEmpty> <%-- Author --%> <logic:notEmpty name="announcement"
				property="author">
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.messaging.author" />: <fr:view name="announcement"
					property="author" />
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.messaging.symbol.less" />
			</logic:notEmpty> <%-- Local --%> <logic:notEmpty name="announcement" property="place">
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.messaging.place" />: <fr:view name="announcement"
					property="place" /> - 
					  <bean:message bundle="MESSAGING_RESOURCES"
					key="label.messaging.symbol.less" />
			</logic:notEmpty> <%-- Modified em --%> <logic:equal name="announcement"
				property="originalVersion" value="false">
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.messaging.modified.in" />
				<fr:view name="announcement" property="lastModification"
					type="org.joda.time.DateTime" layout="no-time" />
				<bean:message bundle="MESSAGING_RESOURCES"
					key="label.messaging.symbol.less" />
			</logic:equal> <%-- CreationDate --%> <logic:equal name="announcement"
				property="announcementBoard.currentUserWriter" value="true">
				<bean:message key="label.creationDate" bundle="MESSAGING_RESOURCES" />:
						<span id="<%= "ID_" + announcementId %>">
				<fr:view name="announcement" property="creationDate"
					layout="no-time" /> </span>
			</logic:equal> </em></p>
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

		<bean:define id="announcementBoardId" name="announcementBoard"
			property="externalId" />
		<fr:view name="announcements"
			schema="announcement.view-with-creationDate-subject-online">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle2" />
				<fr:property name="columnClasses"
					value=",nowrap,acenter,acenter,nowrap" />
				<fr:property name="link(edit)"
					value="<%= contextPrefix + "method=editAnnouncement" + "&amp;"+extraParameters + "&amp;tabularVersion=true"%>" />
				<fr:property name="param(edit)"
					value="externalId/announcementId,announcementBoard.externalId/announcementBoardId" />
				<fr:property name="key(edit)" value="messaging.edit.link" />
				<fr:property name="bundle(edit)" value="MESSAGING_RESOURCES" />
				<fr:property name="order(edit)" value="2" />
				<fr:property name="link(view)"
					value="<%= contextPrefix + "method=viewAnnouncement&amp;" + extraParameters %>" />
				<fr:property name="param(view)" value="externalId/announcementId" />
				<fr:property name="key(view)" value="messaging.view.link" />
				<fr:property name="bundle(view)" value="MESSAGING_RESOURCES" />
				<fr:property name="order(view)" value="1" />

				<fr:property name="link(remove)"
					value="<%= contextPrefix + "method=deleteAnnouncement" + "&amp;" + extraParameters + "&amp;tabularVersion=true"%>" />
				<fr:property name="param(remove)"
					value="externalId/announcementId,announcementBoard.externalId/announcementBoardId" />
				<fr:property name="key(remove)" value="messaging.delete.label" />
				<fr:property name="bundle(remove)" value="MESSAGING_RESOURCES" />
				<fr:property name="order(remove)" value="3" />
				<fr:property name="confirmationKey(remove)" value="message.remove.announcement.confirmation" />
				<fr:property name="confirmationBundle(remove)" value="MESSAGING_RESOURCES" />

				<fr:property name="sortUrl"
					value="<%= contextPrefix + "method=" + methodWithReturn + "&amp;announcementBoardId=" + announcementBoardId + "&amp;tabularVersion=true&amp;" + extraParameters %>" />
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


		<div class="aarchives"><messaging:archive name="archive"
			targetUrl="<%= request.getContextPath() + module + contextPrefix + "method=viewArchive&amp;announcementBoardId=" + board.getExternalId() + "&amp;" + extraParameters + "&amp;" %>" />
		</div>

	</logic:present>
</logic:present>
