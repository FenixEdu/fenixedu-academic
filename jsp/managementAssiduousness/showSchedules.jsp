<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.schedules" /></h2>
<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW);
            if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user
                    .getPerson())) {

                %>
<ul class="list5 mtop15">
	<li>
		<html:link
			page="/assiduousnessParametrization.do?method=prepareInsertSchedule">
			<bean:message key="link.insertSchedule" />
		</html:link>
	</li>
</ul>
<%}%>

<logic:present name="workScheduleList">
	<fr:view name="workScheduleList" schema="show.workScheduleType">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder tdleft" />
			<%if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user.getPerson())) {%>
			<fr:property name="link(view)"
				value="/assiduousnessParametrization.do?method=prepareEditSchedule" />
			<fr:property name="key(view)" value="label.edit" />
			<fr:property name="param(view)" value="idInternal" />
			<fr:property name="visibleIf(view)" value="isEditable" />
			<fr:property name="bundle(view)" value="ASSIDUOUSNESS_RESOURCES" />
			<% } %>
		</fr:layout>
	</fr:view>
</logic:present>

