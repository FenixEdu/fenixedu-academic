<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.selectPreviousPointsSchema"/></h2>

<ul>
	<li><a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareGroupPointsPropertyEdition&oid=" + request.getParameter("oid") %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></a></li>
</ul>

<fr:form action="<%= "/vigilancy/vigilantGroupManagement.do?method=changeYearForPoints&oid=" + request.getParameter("oid") %>">
	<fr:edit id="selectVigilantGroup" name="bean" schema="selectVigilantGroupForPoints">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<logic:present name="group">
	<bean:define id="groupID" name="group" property="externalId"/>
	<fr:form action="<%= "/vigilancy/vigilantGroupManagement.do?method=copySchemaPoints&show=groups&oid=" + request.getParameter("oid") + "&selectedGroupID=" + groupID %>">
		<fr:view name="group" schema="previousPointsForGroup">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:view>
		
		<html:submit><bean:message bundle="RENDERER_RESOURCES" key="renderers.form.submit.name"/></html:submit>
	</fr:form>
</logic:present>