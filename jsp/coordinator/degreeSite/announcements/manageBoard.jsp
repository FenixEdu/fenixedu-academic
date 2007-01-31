<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define name="degreeCurricularPlanId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define name="boardId" name="board" property="idInternal"/>

<em><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></em>
<h2>
    <bean:write name="board" property="name"/>
    <logic:equal name="board" propert="publicToRead" value="true">
        <span title="Really Simple Syndication" style="font-weight: normal; font-size: 0.7em;">
            (<html:link module="" path="<%= "/external/announcementsRSS.do?method=simple&amp;announcementBoardId=" + boardId %>">RSS</html:link>)
        </span>
    </logic:equal>
</h2>

<ul>
    <li>
        <html:link action="<%= String.format("announcementsManagement.do?method=addAnnouncement&amp;degreeCurricularPlanID=%s&amp;boardID=%s", degreeCurricularPlanID, boardId) %>">
            <bean:message key="label.createAnnouncement" bundle="MESSAGING_RESOURCES"/>
        </html:link>
    </li>               
</ul>

<bean:define id="anouncements" name="board" property="announcements"/>

<logic:notEmpty name="announcements">
    <% String sortCriteria = request.getParameter("sortBy"); %>
    
    <fr:view name="announcements" schema="announcement.view-with-creationDate-subject-online">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle2"/>
                <fr:property name="columnClasses" value=""/>
                <fr:property name="link(edit)" value="<%= String.format("/announcementsManagement.do&amp;method=editAnnouncement&amp;degreeCurricularPlanID=%s,&amp;boardID=%s", degreeCurricularPlanId, boardId) %>"/>
                <fr:property name="param(edit)" value="idInternal/announcementId"/>
                <fr:property name="key(edit)" value="messaging.edit.link"/>
                <fr:property name="bundle(edit)" value="MESSAGING_RESOURCES"/>
                <fr:property name="order(edit)" value="2"/>
                <fr:property name="link(view)" value="<%= String.format("/announcementsManagement.do&amp;method=viewAnnouncement&amp;degreeCurricularPlanID=%s&amp;boardID=%s", degreeCurricularPlanId, boardId) %>"/>
                <fr:property name="param(view)" value="idInternal/announcementId"/>
                <fr:property name="key(view)" value="messaging.view.link"/>
                <fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>                      
                <fr:property name="order(view)" value="1"/>
                <fr:property name="link(remove)" value="<%= String.format("/announcementsManagement.do&amp;method=deleteAnnouncement&amp;degreeCurricularPlanID=%s&amp;boardID=%s", degreeCurricularPlanId, boardId) %>"/>
                <fr:property name="param(remove)" value="idInternal/announcementId"/>
                <fr:property name="key(remove)" value="messaging.delete.label"/>
                <fr:property name="bundle(remove)" value="MESSAGING_RESOURCES"/>                
                <fr:property name="order(remove)" value="3"/>
                
                <fr:property name="sortUrl" value="<%= String.format("/announcementsManagement.do?method=manageBoard&amp;degreeCurricularPlanID=%s&amp;boardID=%s", degreeCurricularPlanId, boardId) %>"/>
                <fr:property name="sortParameter" value="sortBy"/>
                <fr:property name="sortBy" value="<%= sortCriteria == null ? "subject" : sortCriteria %>"/>
        </fr:layout>
    </fr:view>
    
    <logic:present name="archive">
    <logic:present name="announcementBoard">
        <bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
        <%
        final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context");
        final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
        final String module = org.apache.struts.util.ModuleUtils.getInstance().getModuleConfig(request).getPrefix();
        %>


        <div class="aarchives">
            <messaging:archive name="archive" targetUrl="<%=request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort() + context + module + contextPrefix + "method=viewArchive&amp;announcementBoardId=" + board.getIdInternal() + "&amp;" + extraParameters %>"/>    
        </div>

    </logic:present>
</logic:present>
    
</logic:notEmpty>

<logic:empty name="announcements">
    <p class="mtop2">
        <em><bean:message key="label.noAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
    </p>
</logic:empty>