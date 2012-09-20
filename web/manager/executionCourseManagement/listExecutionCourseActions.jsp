<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES"
	key="label.manager.executionCourseManagement.edit.executionCourse" /></h2>


<bean:define id="executionPeriodId" name="sessionBean"
	property="executionPeriod.idInternal" />
<bean:define id="executionPeriodQName" name="sessionBean"
	property="executionPeriod.qualifiedName" />

	
<bean:define id="linkGetRequestBigMessage" value=""/>
<bean:define id="linkGetRequestLilMessage" value=""/>


<logic:equal name="sessionBean" property="chooseNotLinked" value="false">

	<bean:define id="executionDegreeType" name="sessionBean"
		property="executionDegree.degreeType" />
	<bean:define id="executionDegreeName" name="sessionBean"
		property="executionDegree.degreeName" />
	<bean:define id="executionDegreeId" name="sessionBean"
		property="executionDegree.idInternal" />

	<bean:define id="curricularYear" name="sessionBean"
		property="curricularYear.year" />
	<bean:define id="curricularYearId" name="sessionBean"
		property="curricularYear.idInternal" />

	<bean:define id="notLinkedHardCoded"
		value="&executionCoursesNotLinked=null" />

	<bean:define id="yearTag"
		value="<%= curricularYear.toString() + "º ano" %>" />

	<fr:view name="sessionBean" property="executionDegree.degreeType.localizedName" />
	<fr:view name="sessionBean" property="executionDegree.degreeName" />
(<b><fr:view name="sessionBean"
		property="executionDegree.degree.sigla" /></b>) &nbsp;&gt;&nbsp;
<fr:view name="yearTag" />
	<fr:view name="sessionBean" property="executionPeriod.qualifiedName" />


	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + executionPeriodId.toString() +
	"&executionDegree=" + executionDegreeType.toString() + " em " + executionDegreeName.toString() + "~" + executionDegreeId.toString() +
	"&curYear=" + curricularYear.toString() + notLinkedHardCoded.toString() %>" />

	<bean:define id="linkGetRequestLilMessage"
		value="<%= "&executionPeriodId=" + executionPeriodId.toString() +
	"&executionDegreeId=" + executionDegreeId + "&curYearId=" + curricularYearId.toString() + notLinkedHardCoded.toString()%>" />

</logic:equal>



<logic:equal name="sessionBean" property="chooseNotLinked" value="true">

	<bean:define id="notLinked" name="sessionBean" property="chooseNotLinked"/>

	<p><fr:view name="sessionBean"
		property="executionPeriod.qualifiedName" />&nbsp;&gt;&nbsp; <bean:message
		bundle="MANAGER_RESOURCES" key="label.manager.chooseNotLinked" /></p>


	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + executionPeriodId.toString() +
	"&executionDegree=null~null" + "&curYear=null" + "&executionCoursesNotLinked=" + notLinked %>" />

	<bean:define id="linkGetRequestLilMessage"
		value="<%= "&executionPeriodId=" + executionPeriodId.toString() +
	"&executionDegreeId=null" + "&curYearId=null" + "&executionCoursesNotLinked=" + notLinked %>" />

</logic:equal>


<fr:view name="sessionBean" property="executionCourses">
	<fr:layout name="tabular">

		<fr:property name="linkGroupSeparator" value="&nbsp&nbsp|&nbsp&nbsp" />

		<fr:property name="linkFormat(edit)"
			value="<%="/editExecutionCourse.do?method=editExecutionCourse&executionCourseId=${idInternal}" + linkGetRequestBigMessage.toString() %>" />
		<fr:property name="order(edit)" value="1" />
		<fr:property name="key(edit)"
			value="label.manager.executionCourseManagement.edit" />
		<fr:property name="bundle(edit)" value="MANAGER_RESOURCES" />

		<fr:property name="linkFormat(swapAnnoun)"
			value="<%= "/announcementSwap.do?method=prepareSwap&executionCourseId=${idInternal}" + linkGetRequestLilMessage.toString() %>" />
		<fr:property name="order(swapAnnoun)" value="2" />
		<fr:property name="key(swapAnnoun)"
			value="label.manager.executionCourse.announcementMove" />
		<fr:property name="bundle(swapAnnoun)" value="MANAGER_RESOURCES" />

		<fr:property name="linkFormat(splitCourses)"
			value="/seperateExecutionCourse.do?method=prepareTransfer&executionCourseId=${idInternal}" />
		<fr:property name="order(splitCourses)" value="3" />
		<fr:property name="key(splitCourses)"
			value="link.manager.seperate.execution_course" />
		<fr:property name="bundle(splitCourses)" value="MANAGER_RESOURCES" />

		<fr:property name="linkFormat(delete)"
			value="<%="/editExecutionCourse.do?method=deleteExecutionCourse&executionCourseId=${idInternal}" + linkGetRequestBigMessage.toString() %>" />
		<fr:property name="order(delete)" value="4" />
		<fr:property name="key(delete)"
			value="label.manager.executionCourseManagement.delete" />
		<fr:property name="visibleIf(delete)" value="deletable" />
		<fr:property name="bundle(delete)" value="MANAGER_RESOURCES" />
		<fr:property name="confirmationKey(delete)" value="label.manager.delete.selected.executionCourses.certainty"/>
		<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES"/>
		<fr:property name="confirmationArgs(delete)" value="${idInternal},${nome},${sigla}"/>


		<fr:property name="linkFormat(sentEmails)"
			value="/emails.do?method=viewSentEmails&senderId=${sender.idInternal}" />
		<fr:property name="order(sentEmails)" value="5" />
		<fr:property name="key(sentEmails)"
			value="link.manager.email.sender" />
		<fr:property name="bundle(sentEmails)" value="MANAGER_RESOURCES" />
		<fr:property name="visibleIf(sentEmails)" value="hasSender" />
		<fr:property name="module(sentEmails)" value="/messaging" />
		
	
		<fr:property name="classes" value="tstyle1 thleft" />
		<fr:property name="columnClasses" value=",,,tdclear tderror1" />

	</fr:layout>

	<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionCourse"
		bundle="MANAGER_RESOURCES">
		<fr:slot name="nome"
			key="label.manager.teachersManagement.executionCourseName" />
		<fr:slot name="sigla" key="label.manager.curricularCourse.code" />
	</fr:schema>
</fr:view>


<!--<h2>Apagar Anúncios</h2>

<p><strong>Opção Livre 1</strong></p>

<div class="warning1">
	Deseja apagar todos os anúncios da disciplina Opção Livre 1 - 2009/2010 - 1º Semestre?
</div>

<p>
	<input type="button" value="Apagar Anúncios"/>
	<input type="button" value="Cancelar"/>
</p>-->