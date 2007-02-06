<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="announcement">

	<bean:define id="announcement" name="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement"/>
		
	<em><bean:message key="label.messaging.portal" bundle="MESSAGING_RESOURCES"/></em>
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
                <bean:message key="label.messaging.announcement.publicationDate" bundle="MESSAGING_RESOURCES"/>
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
                <bean:message key="label.messaging.announcement.publicationDate" bundle="MESSAGING_RESOURCES"/>
				<fr:view name="announcement" property="creationDate" layout="no-time"/>
			</logic:empty>
		</span>
	</p>
				
<%-- Título --%>
	<h3 class="mvert025">
		<b><fr:view name="announcement" property="subject" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></b>
	</h3>


<%-- Body --%>

	<fr:view name="announcement" property="body" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html" />

<p class="mvert025">
	<em class="smalltxt" style="color: #888; text-wrap: nowrap;">


<%-- Autor --%>		 		
	<logic:notEmpty name="announcement" property="author">
		<logic:notEmpty name="announcement" property="authorEmail">
            <bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>:
			<html:link href="<%="mailto:"+announcement.getAuthorEmail()%>">
				<fr:view name="announcement" property="author"/>
			</html:link>
			 - 
		</logic:notEmpty>
	</logic:notEmpty>	
	

<%-- Data do Evento --%>
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<logic:notEmpty name="announcement" property="referedSubjectEnd">
				<bean:message key="label.messaging.period.from" bundle="MESSAGING_RESOURCES"/>
			</logic:notEmpty>
		</logic:notEmpty>
		
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time"/>
		</logic:notEmpty>
		
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<logic:notEmpty name="announcement" property="referedSubjectEnd">
				<bean:message key="label.messaging.period.to" bundle="MESSAGING_RESOURCES"/>
			</logic:notEmpty>
		</logic:notEmpty>				
		 
		<logic:notEmpty name="announcement" property="referedSubjectEnd">
			<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time"/>
			 - 
		</logic:notEmpty>
		
<%-- Autor --%>
	<logic:notEmpty name="announcement" property="author">
		<logic:empty name="announcement" property="authorEmail">
			<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
            <fr:view name="announcement" property="author"/>
			 - 
		</logic:empty>
	</logic:notEmpty>

<%-- Local --%>
	<logic:notEmpty name="announcement" property="place">
		<bean:message key="label.messaging.location" bundle="MESSAGING_RESOURCES"/>:
        <fr:view name="announcement" property="place"/>
		 - 
	</logic:notEmpty>
	
<%-- Modificado em --%>
	<%	if (announcement.wasModifiedSinceCreation())
		{
	%>
        <bean:message key="label.messaging.modified.in" bundle="MESSAGING_RESOURCES"/>:
		<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
		 - 
	<%
	}
	%>

<%-- Data de Criação --%>
	<html:link linkName="<%=announcement.getIdInternal().toString()%>"/>
        <bean:message key="label.messaging.announcement.creationDate" bundle="MESSAGING_RESOURCES"/>:
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