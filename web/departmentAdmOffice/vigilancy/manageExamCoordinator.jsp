<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.manageExamCoordinator"/></h2>


<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/vigilancy/examCoordinatorManagement.do?method=selectUnitForCoordinator">
<fr:edit id="selectUnit" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
name="bean" schema="selectUnitInVigilantGroup"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>
<!--  selectUnitInVigilantGroup -->

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>


<logic:present name="bean" property="selectedUnit">		
<logic:empty name="bean" property="selectedUnit.examCoordinators">
	<p><em class="warning0"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noExamCoordinatoresForUnit"/></em></p>
</logic:empty>

<logic:notEmpty name="bean" property="selectedUnit.examCoordinators">
	<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.coordinatorsForUnit"/></strong>:</p>
<fr:view name="bean" property="selectedUnit.examCoordinators" schema="showExamCoordinators">
<fr:layout name="tabular">
	<fr:property name="classes" value="tstyle1 mvert05" />
	<fr:property name="sortBy" value="executionYear=desc, person.name=asc" />
	<fr:property name="key(apagar)" value="label.delete"/>
	<fr:property name="bundle(apagar)" value="VIGILANCY_RESOURCES"/>
	<fr:property name="link(apagar)" value="<%= "/vigilancy/examCoordinatorManagement.do?method=deleteExamCoordinator&unitId=" + bean.getSelectedUnit().getIdInternal() + "&deparmentId=" + bean.getSelectedDepartment().getIdInternal() %>"/>
	<fr:property name="param(apagar)" value="idInternal/oid"/>
	<fr:property name="visibleIf(apagar)" value="executionYear.current"/>
</fr:layout>
</fr:view>
<p class="mtop05 mbottom2">
	<html:link page="<%="/vigilancy/examCoordinatorManagement.do?method=editExamCoordinators&unitId=" + bean.getSelectedUnit().getIdInternal() + "&deparmentId=" + bean.getSelectedDepartment().getIdInternal() %>">
	<bean:message key="label.vigilancy.editPreviledges" bundle="VIGILANCY_RESOURCES"/>
	</html:link>
</p>
</logic:notEmpty>



<p class="mbottom05"><strong><bean:message key="label.vigilancy.selectPersonToCoordinate" bundle="VIGILANCY_RESOURCES"/></strong>:</p>

<fr:form action="/vigilancy/examCoordinatorManagement.do?method=addExamCoordinator">
<fr:edit name="bean" id="preserveState" schema="examCoordinator.create" 
action="/vigilancy/examCoordinatorManagement.do?method=addExamCoordinator"/>
<p><html:submit><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit></p>
</fr:form>




</logic:present>


<script type="text/javascript" language="javascript">
switchGlobal();
</script>
