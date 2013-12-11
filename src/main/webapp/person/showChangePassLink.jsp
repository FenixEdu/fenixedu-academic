<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key ="title.person.changepass" /></h2>


<div class="infoop2 mvert15">
<bean:message key="message.change.password" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="APPLICATION_RESOURCES"/>
</div>

<p><html:link href='<%= "https://id.ist.utl.pt/password/index.php?url=https://fenix.ist.utl.pt/privado&istid=" + AccessControl.getPerson().getIstUsername() %>' ><bean:message key="link.change.password" bundle="APPLICATION_RESOURCES"/></html:link></p>	

