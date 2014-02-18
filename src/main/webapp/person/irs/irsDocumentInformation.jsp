<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key ="link.title.irsDeclaration" /></h2>

<p><em><bean:message key="message.irs.declaration.not.available" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" arg1="<%= Instalation.getInstance().getInstituitionalEmailAddress("da") %>" bundle="APPLICATION_RESOURCES" /></em></p>
