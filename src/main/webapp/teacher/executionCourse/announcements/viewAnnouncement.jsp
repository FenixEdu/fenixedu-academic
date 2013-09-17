<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="announcement">

		
	<em><bean:message key="label.messaging.portal" bundle="MESSAGING_RESOURCES"/></em>
	<h2><bean:write name="announcement" property="announcementBoard.name"/></h2>
	
	<%
	String contextPrefix = (String) request.getAttribute("contextPrefix");
	String extraParameters = (String) request.getAttribute("extraParameters");
	net.sourceforge.fenixedu.domain.Person person = (net.sourceforge.fenixedu.domain.Person) request.getAttribute("person");
	%>
	

<div class="mvert2 announcement">

<%-- Publication Date --%>
	<p class="mvert025 smalltxt greytxt1">
		<span>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="Publicar">
			<logic:notEmpty name="announcement" property="publicationBegin">
					<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.published.in" />
					<fr:view name="announcement" property="publicationBegin" layout="no-time"/>

					<logic:equal name="announcementBoard" property="currentUserWritter" value="true">
						<logic:notEmpty name="announcement" property="publicationEnd">
						 	<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.until" />
							<fr:view name="announcement" property="publicationEnd" layout="no-time"/>
						</logic:notEmpty>
					</logic:equal>
			</logic:notEmpty>
				
			<logic:empty name="announcement" property="publicationBegin">
				<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.published.in" />
				<fr:view name="announcement" property="creationDate" layout="no-time"/>
			</logic:empty>
		</span>
	</p>
					
<%-- Tï¿½tulo --%>
	<h3 class="mvert025">
		<b><fr:view name="announcement" property="subject" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/></b>
	</h3>


<%-- Body --%>
	<div class="ann_body">
		<fr:view name="announcement" property="body" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString" layout="html" />
	</div>

<p class="mvert025">
	<em class="smalltxt" style="color: #888;" >


<%-- Autor --%>		 		
	<logic:notEmpty name="announcement" property="author">
		<logic:notEmpty name="announcement" property="authorEmail">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.author" />:
			<bean:define id="email" name="announcement" property="authorEmail"/>
			<html:link href="<%="mailto:"+ email %>">
				<fr:view name="announcement" property="author"/>
			</html:link>
			 - 
		</logic:notEmpty>
	</logic:notEmpty>	
	

<%-- Data do Evento --%>
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<logic:notEmpty name="announcement" property="referedSubjectEnd">
				<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.from" />
			</logic:notEmpty>
		</logic:notEmpty>
		
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time"/>
		</logic:notEmpty>
		
		<logic:notEmpty name="announcement" property="referedSubjectBegin">
			<logic:notEmpty name="announcement" property="referedSubjectEnd">
				<bean:message bundle="MESSAGING_RESOURCES" key="label.listAnnouncements.event.occurs.to" />
			</logic:notEmpty>
		</logic:notEmpty>				
		 
		<logic:notEmpty name="announcement" property="referedSubjectEnd">
			<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time"/>
			 - 
		</logic:notEmpty>
		
<%-- Autor --%>
	<logic:notEmpty name="announcement" property="author">
		<logic:empty name="announcement" property="authorEmail">
			<p class="mvert025 graytxt1"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.author" />: <fr:view name="announcement" property="author"/></p>
			 - 
		</logic:empty>
	</logic:notEmpty>

<%-- Local --%>
	<logic:notEmpty name="announcement" property="place">
		<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.place" />: <fr:view name="announcement" property="place"/>
		 - 
	</logic:notEmpty>
	
<%-- Modificado em --%>
		<logic:equal name="announcement" property="originalVersion" value="false">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.modified.in" />:
			<fr:view name="announcement" property="lastModification" type="org.joda.time.DateTime" layout="no-time"/>
			 - 
		 </logic:equal>

<%-- Data de Criação --%>
	<bean:define id="externalId" name="announcement" property="externalId" type="java.lang.String"/> 
	<html:link linkName="<%= externalId.toString() %>"/>
		<bean:message key="label.creationDate" bundle="MESSAGING_RESOURCES" />: 
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