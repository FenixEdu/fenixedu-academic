<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.justifications" /></h2>
<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW);
            if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user
                    .getPerson())) {

            %>
<ul class="list5 mtop15">
	<li>
		<html:link page="/assiduousnessParametrization.do?method=prepareInsertJustificationMotive"><bean:message key="link.createJustification" /></html:link>
	</li>
</ul>
<%}%>

<logic:present name="justificationMotives">
	<fr:view name="justificationMotives" schema="show.justificationMotives">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder tdleft" />
			<fr:property name="headerClasses" value="acenter" />
			<%if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user
                    .getPerson())) {
                %>
                <fr:property name="link(edit)" value="/assiduousnessParametrization.do?method=prepareEditJustificationMotive" />
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="param(edit)" value="idInternal" />
				<fr:property name="bundle(edit)" value="ASSIDUOUSNESS_RESOURCES" />
            <%}%>
		</fr:layout>
	</fr:view>
</logic:present>
