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

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>

<div class="breadcumbs mvert0">
    <a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
    <bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
    &nbsp;&gt;&nbsp;
    <a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
    <logic:present name="degree">
        <bean:define id="degreeId" name="degree" property="externalId"/>
        
        &nbsp;&gt;&nbsp;
        <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + degreeId %>">
            <bean:write name="degree" property="sigla"/>
        </html:link>

        &nbsp;&gt;&nbsp;
        <html:link page="<%= "/showDegreeAnnouncements.do?method=viewAnnouncements&amp;degreeID=" + degreeId %>">
            <bean:message key="public.degree.information.label.announcements"  bundle="PUBLIC_DEGREE_INFORMATION" />
        </html:link>
        &nbsp;&gt;&nbsp;
        
        <logic:present name="announcement">
            <fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
        </logic:present>
    </logic:present>
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

<!-- CAMPUS -->
<logic:iterate id="campusIter" name="campus">
    <h2 class="greytxt">
        <bean:write name="campusIter" property="name"/>
    </h2>
</logic:iterate>

<logic:present name="announcement">

    <bean:define id="announcement" name="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement"/>

    <h2><bean:write name="announcement" property="announcementBoard.name"/></h2>

    <%
    String contextPrefix = (String) request.getAttribute("contextPrefix");
    String extraParameters = (String) request.getAttribute("extraParameters");
    net.sourceforge.fenixedu.domain.Person person = (net.sourceforge.fenixedu.domain.Person) request.getAttribute("person");
    %>

<div class="mvert2" style="width: 550px;">

    <%-- Publication Date --%>
    <p class="mvert025 smalltxt greytxt1">
        <span>
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="Publicar"/>
            <logic:notEmpty name="announcement" property="publicationBegin">
                Publicado em 
                    <fr:view name="announcement" property="publicationBegin" layout="no-time"/>
                <%
                if (announcement.getAnnouncementBoard().hasWriter(person)) {
                %>
                    <logic:notEmpty name="announcement" property="publicationEnd">
                        atï¿? 
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
                
<%-- Tï¿?tulo --%>
    <h3 class="mvert025">
        <b><fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/></b>
    </h3>


<%-- Body --%>

    <fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html" />

<p class="mvert025">
    <em class="smalltxt" style="color: #888;">


<%-- Autor --%>             
    <logic:notEmpty name="announcement" property="author">
        <logic:notEmpty name="announcement" property="authorEmail">
            Autor: 
            <html:link href="<%="mailto:"+announcement.getAuthorEmail()%>">
                <fr:view name="announcement" property="author"/>
            </html:link>
             - 
        </logic:notEmpty>
    </logic:notEmpty>   
    

<%-- Data do Evento --%>
        <logic:notEmpty name="announcement" property="referedSubjectBegin">
            <logic:notEmpty name="announcement" property="referedSubjectEnd">
                De
            </logic:notEmpty>
        </logic:notEmpty>
        
        <logic:notEmpty name="announcement" property="referedSubjectBegin">
            <fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time"/>
        </logic:notEmpty>
        
        <logic:notEmpty name="announcement" property="referedSubjectBegin">
            <logic:notEmpty name="announcement" property="referedSubjectEnd">
                a
            </logic:notEmpty>
        </logic:notEmpty>               
         
        <logic:notEmpty name="announcement" property="referedSubjectEnd">
            <fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time"/>
             - 
        </logic:notEmpty>
        
<%-- Autor --%>
    <logic:notEmpty name="announcement" property="author">
        <logic:empty name="announcement" property="authorEmail">
            Autor: <fr:view name="announcement" property="author"/>
             - 
        </logic:empty>
    </logic:notEmpty>

<%-- Local --%>
    <logic:notEmpty name="announcement" property="place">
        Local: <fr:view name="announcement" property="place"/>
         - 
    </logic:notEmpty>
    
<%-- Modificado em --%>
    <%  if (announcement.wasModifiedSinceCreation())
        {
    %>
        Modificado em:
        <fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
         - 
    <%
    }
    %>

<%-- Data de Criação --%>
    <html:link linkName="<%=announcement.getExternalId().toString()%>"/>
        Data de criação: 
        <fr:view name="announcement" property="creationDate" type="org.joda.time.DateTime" layout="no-time"/>
    </em>
</p>
</div>

</logic:present>


<logic:notPresent name="announcement">
    <bean:message key="error.cannot.display.announcement" bundle="MESSAGING_RESOURCES"/><br/>
    <bean:message key="error.not.allowed.to.view.announcement.possible.causes" bundle="MESSAGING_RESOURCES"/>
    <ul>
        <li>
            <bean:message key="error.not.allowed.to.view.announcement" bundle="MESSAGING_RESOURCES"/>
        </li>
        <li>
            <bean:message key="error.invisible.view.announcement" bundle="MESSAGING_RESOURCES"/>
        </li>
    </ul>
</logic:notPresent>