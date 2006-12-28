<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayUnavailableInformation"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<fr:form action="/vigilancy/unavailablePeriodManagement.do?method=manageUnavailablePeriodsOfVigilants">
<fr:edit id="selectVigilantGroup" name="bean" schema="vigilantGroup.selectToEdit">
</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<logic:present name="bean" property="selectedVigilantGroup">
<bean:define id="vigilantGroup" name="bean" property="selectedVigilantGroup" type="net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup"/>

<ul>
	<li>
	<html:link page="<%= "/vigilancy/unavailablePeriodManagement.do?method=prepareAddPeriodToVigilant&gid=" + vigilantGroup.getIdInternal() %>">
		<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addAnUnavailablePeriodOfVigilant"/>
	</html:link>
	</li>
</ul>

<logic:empty name="unavailablePeriods"> 
	<p>
		<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noUnavailablePeriodsToManage"/>.</em>
	</p>
</logic:empty>

<logic:notEmpty name="unavailablePeriods">
	<fr:view 
		name="unavailablePeriods"
		schema="unavailableShowForCoordinator"
    >
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />

		<fr:property name="key(edit)" value="label.edit"/>
		<fr:property name="bundle(edit)" value="VIGILANCY_RESOURCES"/>
		<fr:property name="link(edit)" value="<%= "/vigilancy/unavailablePeriodManagement.do?method=editUnavailablePeriodOfVigilant&gid=" + vigilantGroup.getIdInternal() %>"/>
		<fr:property name="param(edit)" value="idInternal/oid" />
		<fr:property name="key(delete)" value="label.delete"/>
		<fr:property name="bundle(delete)" value="VIGILANCY_RESOURCES"/>
		<fr:property name="link(delete)" value="<%= "/vigilancy/unavailablePeriodManagement.do?method=deleteUnavailablePeriodOfVigilant&gid=" + vigilantGroup.getIdInternal() %>"/>
		<fr:property name="param(delete)" value="idInternal/oid" />

	</fr:layout>
	</fr:view>
   
</logic:notEmpty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>