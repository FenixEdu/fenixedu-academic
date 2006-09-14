<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.manage"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error"><bean:write name="messages" /></span>
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
		</fr:layout>
	</fr:edit>
</fr:form>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>

<logic:equal name="bean" property="showInformationByVigilant" value="false">
<ul>
	<li>
		<html:link page="<%= "/vigilancy/convokeManagement.do?method=showConvokesByVigilants&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "") %>">
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
	<html:link page="<%= "/vigilancy/convokeManagement.do?method=showConvokesByEvaluation&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "") %>">
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
		<fr:property name="classes" value="tstyle1a" />
		<fr:property name="sortBy" value="person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="vigilantSchema" value="<%= bean.getWhatSchemaToUseInVigilants() %>"/>

		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForFuture"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>

		<fr:property name="columnClasses" value=",width250px,,,,,,,,,,,,,,,,,,,,,,"/>	
		<fr:property name="convokesToShow" value="future"/>
	</fr:layout>
	
	<fr:destination name="activate" path="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=true&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
	<fr:destination name="deactivate" path="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=false&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
</fr:view>
</logic:equal>


<logic:equal name="bean" property="temporalInformation" value="PAST">
<fr:view name="vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1a" />
		<fr:property name="sortBy" value="person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="vigilantSchema" value="<%= bean.getWhatSchemaToUseInVigilants() %>"/>

		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForPast"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>
		
		<fr:property name="columnClasses" value=",width250px,,,,,,,,,,,,,,,,,,,,,,"/>
		<fr:property name="convokesToShow" value="past"/>
	</fr:layout>
	
	<fr:destination name="confirm" path="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=true&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
	<fr:destination name="unconfirm" path="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=false&oid=${idInternal}&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
</fr:view>
</logic:equal>


<logic:equal name="bean" property="temporalInformation" value="ALL">
<fr:view name="vigilants" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1a" />	
		<fr:property name="sortBy" value="person.username"/>
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="vigilantSchema" value="<%= bean.getWhatSchemaToUseInVigilants() %>"/>

		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForCoordinator"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>

		<fr:property name="columnClasses" value=",width250px,,,,,,,,,,,,,,,,,,,,,,"/>
		<fr:property name="convokesToShow" value="all"/>
	</fr:layout>

</fr:view>
</logic:equal>
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

<fr:view name="evaluation" property="convokes" schema="<%= bean.getWhatSchemaToUse() %>">
<fr:layout name="tabular">
	<fr:property name="sortBy" value="vigilant.person.username"/>
	<fr:property name="classes" value="tstyle1a tdtop thleft mtop05 mbottom0"/>
</fr:layout>
</fr:view>

</logic:equal>

<logic:equal name="bean" property="temporalInformation" value="PAST">

<fr:view name="evaluation" property="convokes" schema="<%= bean.getWhatSchemaToUse() %>">
<fr:layout name="tabular">
	<fr:property name="classes" value="tstyle1a tdtop thleft mtop05 mbottom0"/>
	<fr:property name="sortBy" value="vigilant.person.username"/>
	
		<fr:property name="link(confirmar)" value="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=true&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>" />
		<fr:property name="param(confirmar)" value="idInternal/oid" />
		<fr:property name="visibleIf(confirmar)" value="notAttended" />
		<fr:property name="link(Desconfirmar)" value="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=false&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>" />
		<fr:property name="param(Desconfirmar)" value="idInternal/oid" />
		<fr:property name="visibleIf(Desconfirmar)" value="attended" />

</fr:layout>
</fr:view>
</logic:equal>

<logic:equal name="bean" property="temporalInformation" value="FUTURE">

<fr:view name="evaluation" property="convokes" schema="<%= bean.getWhatSchemaToUse() %>">
<fr:layout name="tabular">
	<fr:property name="sortBy" value="vigilant.person.username"/>
		<fr:property name="classes" value="tstyle1a tdtop thleft mtop05 mbottom0"/>
		<fr:property name="link(Activar)" value="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=true&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>" />
		<fr:property name="param(Activar)" value="idInternal/oid" />
		<fr:property name="visibleIf(Activar)" value="notActive" />
		<fr:property name="link(Desactivar)" value="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=false&temporalInformation=" + bean.getTemporalInformation() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getIdInternal().toString() : "")%>"/>
		<fr:property name="param(Desactivar)" value="idInternal/oid" />
		<fr:property name="visibleIf(Desactivar)" value="active" />
</fr:layout>
</fr:view>
</logic:equal>

<p class="mtop0"><a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?writtenEvaluationId=" + evaluation.getIdInternal() + "&method=prepareAddMoreVigilants" %>">Adicionar Vigilantes</a></p>

</logic:iterate>


</logic:notEmpty>
</logic:present>

</logic:equal>