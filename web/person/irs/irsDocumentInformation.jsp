<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<html:xhtml/>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key ="link.title.irsDeclaration" /></h2>

<bean:define id="loggedPerson" name="loggedPerson" type="net.sourceforge.fenixedu.domain.Person" />

<logic:notEmpty name="loggedPerson" property="student">

	<% if(loggedPerson.hasRole(RoleType.STUDENT)) { %>
		<p>
		<bean:message key="label.students" bundle="APPLICATION_RESOURCES" />:
		<html:link page="/generatedDocuments.do?method=showAnnualIRSDocuments&contentContextPath_PATH=/estudante/estudante" titleKey="label.documents.anualIRS" bundle="STUDENT_RESOURCES" module="/student">
			<bean:message key="link.students.irs.declaration" bundle="APPLICATION_RESOURCES" />
		</html:link>
		</p>
	<% } %>

</logic:notEmpty>

<%
  	net.sourceforge.fenixedu.domain.RootDomainObject rootDomainObject = net.sourceforge.fenixedu.domain.RootDomainObject.getInstance();
  	if (rootDomainObject.getIrsDeclarationLink() != null
  			&& rootDomainObject.getIrsDeclarationLink().getAvailable() != null
  			&& rootDomainObject.getIrsDeclarationLink().getAvailable().booleanValue()) {
%>
 	    		<p class="mtop2"><bean:message key="label.employees" bundle="APPLICATION_RESOURCES" />: 
 	    			<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= rootDomainObject.getIrsDeclarationLink().getIrsLink() %>">
 	    			<bean:message key="link.employee.irs.declaration.download" bundle="APPLICATION_RESOURCES" /></a> (.pdf)
 	    		<p>
<%
  	}
%>

<bean:define id="notStudentAndDeclarationNotAvailable" >
	<%= String.valueOf((rootDomainObject.getIrsDeclarationLink() == null
  			|| rootDomainObject.getIrsDeclarationLink().getAvailable() == null
  			|| !rootDomainObject.getIrsDeclarationLink().getAvailable().booleanValue()) 
  			&& (!loggedPerson.hasRole(RoleType.STUDENT)
  			|| !loggedPerson.hasStudent())) %>
</bean:define>

<logic:equal name="notStudentAndDeclarationNotAvailable" value="true">
	<p><em><bean:message key="message.irs.declaration.not.available" bundle="APPLICATION_RESOURCES" /></em></p>
</logic:equal>
