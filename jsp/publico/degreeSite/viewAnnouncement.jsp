<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messaging.tld" prefix="messaging" %>

<html:xhtml/>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>

<div class="breadcumbs mvert0">
    <a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
    <bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
    &nbsp;&gt;&nbsp;
    <a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
    <logic:present name="degree">
        <bean:define id="degreeId" name="degree" property="idInternal"/>
        
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
    <logic:equal name="degree" property="bolonhaDegree" value="true">
        <bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
    </logic:equal>
    <logic:equal name="degree" property="bolonhaDegree" value="false">
        <bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
    </logic:equal>
    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
    <logic:present name="inEnglish">
        <logic:equal name="inEnglish" value="false">
            <bean:write name="degree" property="nome"/>
        </logic:equal>
        <logic:equal name="inEnglish" value="true">
            <bean:write name="degree" property="nameEn"/>
        </logic:equal>
    </logic:present>
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
        <span id="10367">
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
        <b><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></b>
    </h3>


<%-- Body --%>

    <fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html" />

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
    <html:link linkName="<%=announcement.getIdInternal().toString()%>"/>
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