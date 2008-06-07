<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.assiduousnessExemptions" /></h2>

<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE);
            if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {

            %>
<ul class="list5 mtop15">
	<li>
		<html:link page="/assiduousnessParametrization.do?method=insertAssiduousnessExemption"><bean:message key="link.createAssiduousnessExemption" /></html:link>
	</li>
</ul>
<%}%>
<logic:present name="assiduousnessExemptions">
	<fr:view name="assiduousnessExemptions" schema="show.assiduousnessExemptions">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder tdleft" />
			<fr:property name="headerClasses" value="acenter" />
			<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {  %>
                <fr:property name="link(edit)" value="/assiduousnessParametrization.do?method=prepareEditAssiduousnessExemption" />
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="param(edit)" value="idInternal" />
				<fr:property name="bundle(edit)" value="ASSIDUOUSNESS_RESOURCES" />
				<fr:property name="link(delete)" value="/assiduousnessParametrization.do?method=deleteAssiduousnessExemption" />
				<fr:property name="key(delete)" value="label.delete" />
				<fr:property name="param(delete)" value="idInternal" />
				<fr:property name="bundle(delete)" value="ASSIDUOUSNESS_RESOURCES" />
            <%}%>
		</fr:layout>
	</fr:view>
</logic:present>
