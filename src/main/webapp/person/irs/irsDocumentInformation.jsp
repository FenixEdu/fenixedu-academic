<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<html:xhtml/>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key ="link.title.irsDeclaration" /></h2>

<p><em><bean:message key="message.irs.declaration.not.available" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" arg1="<%= Instalation.getInstance().getAcademicDirectionEmailAddress() %>" bundle="APPLICATION_RESOURCES" /></em></p>
