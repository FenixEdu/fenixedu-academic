<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.manageVigilantGroups"/></h2>

<logic:present name="bean" property="executionYear">
<logic:equal name="bean" property="executionYear.current" value="true">
<ul>
	<li><html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupCreation"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.newVigilantGroup"/></html:link></li>
</ul>
</logic:equal>
</logic:present>

<logic:present name="bean" property="executionYear">

<div>
	<fr:form action="/vigilancy/vigilantGroupManagement.do?method=changeDisplaySettings">
	<fr:edit id="options" name="bean" schema="selectColumnsToDisplay">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		</fr:layout>
		</fr:edit>
	<html:submit styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
	</fr:form>
</div>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
	<p>
		<span class="error0"><bean:write name="messages" /></span>
	</p>
	</html:messages>
</logic:messagesPresent>

<logic:equal name="bean" property="executionYear.current" value="true">
<logic:notEmpty name="bean" property="vigilantGroups">
<logic:iterate id="vigilantGroup" name="bean" property="vigilantGroups">
<bean:define id="group" name="vigilantGroup" type="net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup"/>

<table class="tstyle1 tdtop thleft">
<tr>
	<logic:equal name="bean" property="showVigilantColumn" value="true">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="3">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="2">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="1">
		</logic:equal>
	</logic:equal>
	</logic:equal>

	<logic:equal name="bean" property="showVigilantColumn" value="false">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="1">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<th colspan="1">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<th colspan="0">
		</logic:equal>
	</logic:equal>
	</logic:equal>
	
	<%= group.getName() %><span style="font-weight: normal;"> (<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareEdition&forwardTo=attributes&oid=" + group.getIdInternal() %>"><bean:message key="label.edit" bundle="VIGILANCY_RESOURCES"/></a>, <a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=deleteVigilantGroup&oid=" + group.getIdInternal() %>"><bean:message key="label.vigilancy.delete" bundle="VIGILANCY_RESOURCES"/></a>)</span></th>
</tr>
<tr>
		<logic:equal name="bean" property="showVigilantColumn" value="true">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="3">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="2">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="1">
		</logic:equal>
	</logic:equal>
	</logic:equal>

	<logic:equal name="bean" property="showVigilantColumn" value="false">
	<logic:equal name="bean" property="showCourseColumn" value="true">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="2">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="1">
		</logic:equal>
	</logic:equal>
	<logic:equal name="bean" property="showCourseColumn" value="false">
		<logic:equal name="bean" property="showCoordinators" value="true">
		<td colspan="1">
		</logic:equal>
		<logic:equal name="bean" property="showCoordinators" value="false">
		<td colspan="0">
		</logic:equal>
	</logic:equal>
	</logic:equal>
	
	<bean:message key="label.manage" bundle="VIGILANCY_RESOURCES"/>: 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareEdition&forwardTo=vigilants&oid=" + group.getIdInternal() %>"> <bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/></a>,  
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareBoundPropertyEdition&oid=" +  group.getIdInternal() %>"><bean:message key="label.vigilancy.editPermissions" bundle="VIGILANCY_RESOURCES"/></a>, 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareStartPointsPropertyEdition&oid=" + group.getIdInternal() %>"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></a>,
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilancyCourseGroupManagement.do?method=prepareEdition&gid=" + group.getIdInternal() %>"> <bean:message key="label.vigilancy.courses" bundle="VIGILANCY_RESOURCES"/></a>, 
	<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/vigilantGroupManagement.do?method=prepareEdition&forwardTo=editCoordinators&oid=" + group.getIdInternal() %>"><bean:message key="label.vigilancy.examCoordinators" bundle="VIGILANCY_RESOURCES"/></a>  
	</td>
</tr>
<tr>
<logic:equal name="bean" property="showVigilantColumn" value="true">
	<td>
		<logic:notEmpty name="group" property="vigilantsThatCanBeConvoked">
		<p class="mvert05"><strong><bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
		<fr:view name="group" property="vigilantsThatCanBeConvoked">
		<fr:layout>
		<fr:property name="eachSchema" value="presentVigilantName"/>
		<fr:property name="sortBy" value="person.name"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="list2 mtop05"/>
		</fr:layout>
		</fr:view>
		</logic:notEmpty>
		<logic:notEmpty name="group" property="vigilantsThatCantBeConvoked">
		<p class="mvert05"><strong><bean:message key="label.vigilancy.vigilantsThatCantBeConvoked" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
		<fr:view name="group" property="vigilantsThatCantBeConvoked">
		<fr:layout>
		<fr:property name="eachSchema" value="presentVigilantName"/>
		<fr:property name="sortBy" value="person.name"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="list2 mtop05"/>
		</fr:layout>
		</fr:view>
		</logic:notEmpty>
	</td>
</logic:equal>
<logic:equal name="bean" property="showCourseColumn" value="true">
<td>
		<p class="mvert05"><strong><bean:message key="label.vigilancy.courses" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:view name="group" property="executionCourses">
	<fr:layout>
		<fr:property name="eachSchema" value="presentExecutionCourse"/>
		<fr:property name="sortBy" value="nome"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="classes" value="list2 mtop05"/>
	</fr:layout>
	</fr:view>
</td>
</logic:equal>	
<logic:equal name="bean" property="showCoordinators" value="true">
<td>
		<p class="mvert05"><strong><bean:message key="label.vigilancy.examCoordinators" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<fr:view name="group" property="examCoordinators"> 
	<fr:layout>
		<fr:property name="eachSchema" value="presentCoordinatorName"/>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="sortBy" value="person.name"/>
		<fr:property name="classes" value="list2 mtop05"/>
	</fr:layout>
	</fr:view>
		</td>
</logic:equal>
</tr>
</table>
</logic:iterate>

</logic:notEmpty>


<logic:empty name="bean" property="vigilantGroups">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noVigilantGroupsToDisplay"/> 
</logic:empty>
</logic:equal>

</logic:present>
<script type="text/javascript" language="javascript">
switchGlobal();
</script>