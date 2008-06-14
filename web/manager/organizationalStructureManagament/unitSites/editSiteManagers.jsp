<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="backLink" toScope="request">
	<ul>
		<li>
			<html:link page="/unitSiteManagement.do?method=prepare">
				<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</bean:define>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteActionName" value="/unitSiteManagement.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" value="<%= site.getIdInternal().toString() %>" toScope="request"/>

<jsp:include page="/webSiteManager/commons/chooseManagers.jsp"/>
