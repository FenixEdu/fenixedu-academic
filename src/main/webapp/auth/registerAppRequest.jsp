<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page
	import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<html:xhtml />

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="label.auth" bundle="APPLICATION_RESOURCES" />
</h2>

<logic:present name="user">
		<fr:create id="create"
			type="net.sourceforge.fenixedu.domain.ExternalApplication"
			schema="my.schema.create.app" action="/externalAuth?method=listApps">
			<fr:hidden slot="author" name="user"/>
		</fr:create>
</logic:present>