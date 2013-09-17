<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

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
<bean:define id="siteContextParamValue" value="<%= site.getExternalId().toString() %>" toScope="request"/>

<jsp:include page="/webSiteManager/commons/chooseManagers.jsp"/>
