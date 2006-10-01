<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.manage"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<ul>
	<li>
		<html:link page="/vigilancy/convokeManagement.do?method=prepareConvoke">
		<bean:message key="label.create" bundle="VIGILANCY_RESOURCES"/> <bean:message key="label.vigilancy.convokes" bundle="VIGILANCY_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:form action="/vigilancy/convokeManagement.do?method=changeTemporalInformation">
	<fr:edit id="temporalInfo" name="bean" schema="selectTime">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>

<logic:equal name="bean" property="showInformationByVigilant" value="false">
<ul>
	<li>
		<html:link page="<%= "/vigilancy/convokeManagement.do?method=showConvokesByVigilants&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showBoundsJustification=" + bean.isShowBoundsJustification() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "") %>">
		<bean:message key="label.vigilancy.showConvokesByVigilant" bundle="VIGILANCY_RESOURCES"/>
		</html:link>, 
		<span class="highlight1"><bean:message key="label.vigilancy.showConvokesByEvaluation" bundle="VIGILANCY_RESOURCES"/></span>
	</li>
</ul>
</logic:equal>

<logic:equal name="bean" property="showInformationByVigilant" value="true">
<ul>
	<li>
	<span class="highlight1"><bean:message key="label.vigilancy.showConvokesByVigilant" bundle="VIGILANCY_RESOURCES"/></span>, 
	<html:link page="<%= "/vigilancy/convokeManagement.do?method=showConvokesByEvaluation&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showBoundsJustification=" + bean.isShowBoundsJustification() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "") %>">
	<bean:message key="label.vigilancy.showConvokesByEvaluation" bundle="VIGILANCY_RESOURCES"/>
	</html:link>
	</li>
</ul>
</logic:equal>



<logic:equal name="bean" property="showInformationByVigilant" value="true"> 



<logic:notPresent name="writtenEvaluations">
<logic:empty name="vigilants">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noConvokes"/>
</logic:empty>

<logic:notEmpty name="vigilants">
<logic:equal name="bean" property="temporalInformation" value="FUTURE">

<fr:view name="vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="sortBy" value="teacherCategoryCode, person.username"/>
	
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="showIncompatibilities" value="<%= String.valueOf(bean.isShowIncompatibilities()) %>"/>
		<fr:property name="showUnavailables" value="<%= String.valueOf(bean.isShowUnavailables()) %>"/>
		<fr:property name="showBoundsJustification" value="<%= String.valueOf(bean.isShowBoundsJustification()) %>"/>
		<fr:property name="showStartPoints" value="<%= String.valueOf(bean.isShowStartPoints()) %>"/>
		<fr:property name="showNotActiveConvokes" value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>"/>
		
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForFuture"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>

		<fr:property name="columnClasses" value=",,width250px,,,,,,,,,,,,,,,,,,,,,,"/>	
		<fr:property name="convokesToShow" value="future"/>
	</fr:layout>
	
	<fr:destination name="activate" path="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=true&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() +  "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
	<fr:destination name="deactivate" path="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=false&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() +  "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
</fr:view>
</logic:equal>


<logic:equal name="bean" property="temporalInformation" value="PAST">
<fr:view name="vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1" />
 	<fr:property name="sortBy" value="teacherCategoryCode, person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="showIncompatibilities" value="<%= String.valueOf(bean.isShowIncompatibilities()) %>"/>
		<fr:property name="showUnavailables" value="<%= String.valueOf(bean.isShowUnavailables()) %>"/>
		<fr:property name="showBoundsJustification" value="<%= String.valueOf(bean.isShowBoundsJustification()) %>"/>
		<fr:property name="showStartPoints" value="<%= String.valueOf(bean.isShowStartPoints())%>"/>
		<fr:property name="showNotActiveConvokes" value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>"/>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForPast"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>
		
		<fr:property name="columnClasses" value=",,width250px,,,,,,,,,,,,,,,,,,,,,,"/>
		<fr:property name="convokesToShow" value="past"/>
	</fr:layout>
	
	<fr:destination name="confirm" path="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=true&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
	<fr:destination name="unconfirm" path="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=false&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() +  "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
</fr:view>
</logic:equal>


<logic:equal name="bean" property="temporalInformation" value="ALL">
<fr:view name="vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1" />	
		<fr:property name="sortBy" value="teacherCategoryCode, person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="showIncompatibilities" value="<%= String.valueOf(bean.isShowIncompatibilities()) %>"/>
		<fr:property name="showUnavailables" value="<%= String.valueOf(bean.isShowUnavailables()) %>"/>
		<fr:property name="showBoundsJustification" value="<%= String.valueOf(bean.isShowBoundsJustification()) %>"/>
		<fr:property name="showStartPoints" value="<%= String.valueOf(bean.isShowStartPoints())%>"/>
		<fr:property name="showNotActiveConvokes" value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>"/>
		
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForCoordinator"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>

		<fr:property name="columnClasses" value=",,width250px,,,,,,,,,,,,,,,,,,,,,,"/>
		<fr:property name="convokesToShow" value="all"/>
	</fr:layout>

</fr:view>
</logic:equal>

<ul class="list2">
<li>
<em><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></em>
</li>


</ul>
</logic:notEmpty>
</logic:notPresent>


</logic:equal>


<logic:equal name="bean" property="showInformationByVigilant" value="false"> 
<logic:present name="writtenEvaluations">

<logic:empty name="writtenEvaluations">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noConvokes"/>
</logic:empty>

<logic:notEmpty name="writtenEvaluations">

<logic:iterate id="writtenEvaluation" name="writtenEvaluations">

<bean:define id="evaluation" name="writtenEvaluation" type="net.sourceforge.fenixedu.domain.WrittenEvaluation"/>
<bean:define id="executionCourse" name="writtenEvaluation" property="associatedExecutionCourses[0]" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>
<bean:define id="beginDate" name="evaluation" property="beginningDateTime" type="org.joda.time.DateTime"/>
<p class="mtop2 mbottom05"><%= "<strong>" +  executionCourse.getNome() + " (" + executionCourse.getSigla() + ")</strong> - " + evaluation.getEvaluationType().toString() + " " + beginDate.getDayOfMonth() + "/" + beginDate.getMonthOfYear() + "/" + beginDate.getYear() %> </p>


<logic:equal name="bean" property="temporalInformation" value="ALL">
	
<table class="tstyle1 tdtop thleft mtop05 mbottom0">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showUnavailables" value="true">
			<th><bean:message key="label.vigilancy.unavailablePeriodsShortLabel" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showIncompatibilities" value="true">
			<th><bean:message key="label.vigilancy.displayIncompatibleInformation" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showBoundsJustification" value="true">
			<th><bean:message key="label.vigilancy.boundsJustification" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showStartPoints" value="true">
			<th><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th>
			<bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<th><bean:message key="label.vigilancy.date" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
	</tr>
	<logic:iterate id="vigilancyWithCredits" name="evaluation" property="<%= bean.isShowNotActiveConvokes() ? "vigilancysWithCredits" :  "activeVigilancysWithCredits"%>" type="net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits">
		<tr>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.teacherCategoryCode"/>
			</td>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.person.username"/>
			</td>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.person.name"/>
			</td>
			<logic:equal name="bean" property="showUnavailables" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.unavailablePeriodsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showIncompatibilities" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.incompatiblePersonName" type="java.lang.String"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showBoundsJustification" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.boundsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showStartPoints" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.startPoints"/>
				</td>
			</logic:equal>
			<td>
			<fr:view name="vigilancyWithCredits" property="vigilant.points"/>
			</td>
			<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
				<td><fr:view name="vigilancyWithCredits" property="writtenEvaluation.dayDateYearMonthDay"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="active"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="confirmed"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="attendedToConvoke"/>
				</td>
			</logic:equal>
			<td><fr:view name="vigilancyWithCredits" property="points"/>
			</td>
		</tr>
	</logic:iterate>
</table>



</logic:equal>

<logic:equal name="bean" property="temporalInformation" value="PAST">


<table class="tstyle1 tdtop thleft mtop05 mbottom0">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showUnavailables" value="true">
			<th><bean:message key="label.vigilancy.unavailablePeriodsShortLabel" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showIncompatibilities" value="true">
			<th><bean:message key="label.vigilancy.displayIncompatibleInformation" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showBoundsJustification" value="true">
			<th><bean:message key="label.vigilancy.boundsJustification" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showStartPoints" value="true">
			<th><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th>
			<bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<th><bean:message key="label.vigilancy.date" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th></th>
		<th></th>
	</tr>
	<logic:iterate id="vigilancyWithCredits" name="evaluation" property="<%= bean.isShowNotActiveConvokes() ? "vigilancysWithCredits" :  "activeVigilancysWithCredits" %>" type="net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits">
		<tr>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.teacherCategoryCode"/>
			</td>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.person.username"/>
			</td>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.person.name"/>
			</td>
			<logic:equal name="bean" property="showUnavailables" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.unavailablePeriodsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showIncompatibilities" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.incompatiblePersonName" type="java.lang.String"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showBoundsJustification" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.boundsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showStartPoints" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.startPoints"/>
				</td>
			</logic:equal>
			<td>
			<fr:view name="vigilancyWithCredits" property="vigilant.points"/>
			</td>
			<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
				<td><fr:view name="vigilancyWithCredits" property="writtenEvaluation.dayDateYearMonthDay"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="active"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="confirmed"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="attendedToConvoke"/>
				</td>
			</logic:equal>
			<td><fr:view name="vigilancyWithCredits" property="points"/>
			</td>
			<td>
				<logic:equal name="vigilancyWithCredits" property="attended" value="false">
				<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?oid=" + vigilancyWithCredits.getIdInternal() + "&method=convokeAttended&bool=true&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "" ) %>"><bean:message key="label.unconfirm" bundle="VIGILANCY_RESOURCES"/></a>
				</logic:equal>
			</td>
			<td>
				<logic:equal name="vigilancyWithCredits" property="attended" value="true">
				<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?oid=" + vigilancyWithCredits.getIdInternal() + "&method=convokeAttended&bool=false&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "" ) %>"><bean:message key="label.confirm" bundle="VIGILANCY_RESOURCES"/></a>
				</logic:equal>
			</td>
		</tr>
		
	</logic:iterate>
</table>

</logic:equal>

<logic:equal name="bean" property="temporalInformation" value="FUTURE">

<table class="tstyle1 tdtop thleft mtop05 mbottom0">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showUnavailables" value="true">
			<th><bean:message key="label.vigilancy.unavailablePeriodsShortLabel" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showIncompatibilities" value="true">
			<th><bean:message key="label.vigilancy.displayIncompatibleInformation" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showBoundsJustification" value="true">
			<th><bean:message key="label.vigilancy.boundsJustification" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showStartPoints" value="true">
			<th><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th>
			<bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<th><bean:message key="label.vigilancy.date" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th></th>
		<th></th>
	</tr>
	<logic:iterate id="vigilancyWithCredits" name="evaluation" property="<%= bean.isShowNotActiveConvokes() ? "vigilancysWithCredits" :  "activeVigilancysWithCredits" %>" type="net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits">
		<tr>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.teacherCategoryCode"/>
			</td>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.person.username"/>
			</td>
			<td><fr:view name="vigilancyWithCredits" property="vigilant.person.name"/>
			</td>
			<logic:equal name="bean" property="showUnavailables" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.unavailablePeriodsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showIncompatibilities" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.incompatiblePersonName" type="java.lang.String"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showBoundsJustification" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.boundsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showStartPoints" value="true">
				<td><fr:view name="vigilancyWithCredits" property="vigilant.startPoints"/>
				</td>
			</logic:equal>
			<td>
			<fr:view name="vigilancyWithCredits" property="vigilant.points"/>
			</td>
			<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
				<td><fr:view name="vigilancyWithCredits" property="writtenEvaluation.dayDateYearMonthDay"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="active"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="confirmed"/>
				</td>
				<td><fr:view name="vigilancyWithCredits" property="attendedToConvoke"/>
				</td>
			</logic:equal>
			<td><fr:view name="vigilancyWithCredits" property="points"/>
			</td>
			<td>
				<logic:equal name="vigilancyWithCredits" property="active" value="false">
				<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?oid=" + vigilancyWithCredits.getIdInternal() + "&method=convokeActive&bool=true&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"><bean:message key="label.activate" bundle="VIGILANCY_RESOURCES"/></a>
				</logic:equal>
			</td>
			<td>
				<logic:equal name="vigilancyWithCredits" property="active" value="true">
				<a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?oid="  + vigilancyWithCredits.getIdInternal() + "&method=convokeActive&bool=false&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"><bean:message key="label.deactivate" bundle="VIGILANCY_RESOURCES"/></a>
				</logic:equal>
			</td>
		</tr>
		
	</logic:iterate>
</table>

</logic:equal>

<p class="mtop0"><a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?writtenEvaluationId=" + evaluation.getIdInternal() + "&method=prepareAddMoreVigilants" %>">Adicionar Vigilantes</a></p>

</logic:iterate>

<ul class="list2">
<li>
<em><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></em>
</li>
<li>
<em><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/>: <bean:message key="label.vigilancy.startPoints" bundle="VIGILANCY_RESOURCES"/></em>
</li>
</ul>
</logic:notEmpty>
</logic:present>
</logic:equal>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>