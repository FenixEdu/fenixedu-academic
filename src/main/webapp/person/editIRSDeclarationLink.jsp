<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:html xhtml="true"/>

<h2>
	<bean:message key="label.person.edit.irs.declaration.link"  />
</h2>

<fr:edit id="declarationBean" name="declarationBean" action="/irsDeclaration.do?method=editBean">
	<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.person.IRSDeclarationAction$IRSDeclarationBean" bundle="APPLICATION_RESOURCES">
		<fr:slot name="title" key="label.person.title" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="irsLink" key="label.person.link" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="available" key="label.person.available" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>

    <%
    	net.sourceforge.fenixedu.domain.RootDomainObject rootDomainObject = net.sourceforge.fenixedu.domain.RootDomainObject.getInstance();
    	if (rootDomainObject.getIrsDeclarationLink() != null) {
    	    %>
    				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= rootDomainObject.getIrsDeclarationLink().getIrsLink() %>">
    					<%= rootDomainObject.getIrsDeclarationLink().getTitle().getContent() %>
    				</a>
<%
    	}
%>