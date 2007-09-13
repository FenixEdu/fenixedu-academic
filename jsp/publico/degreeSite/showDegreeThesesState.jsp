<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="degree" property="sigla"/></html:link>
	</logic:present>
	&nbsp;&gt;&nbsp;
	<bean:message key="public.degree.information.label.theses"  bundle="PUBLIC_DEGREE_INFORMATION" />
</div>

<!-- COURSE NAME -->
<h1>
	<logic:equal name="degree" property="bolonhaDegree" value="true">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
	</logic:equal>
	<logic:equal name="degree" property="bolonhaDegree" value="false">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
	<logic:present name="inEnglish">
		<logic:equal name="inEnglish" value="false">
			<bean:write name="degree" property="nome"/>
		</logic:equal>
		<logic:equal name="inEnglish" value="true">
			<bean:write name="degree" property="nameEn"/>
		</logic:equal>
	</logic:present>
</h1>

<h2 class="greytxt"><bean:message key="public.degree.information.label.theses"  bundle="PUBLIC_DEGREE_INFORMATION" /></h2>

<div class="mtop1">
	<html:link page="<%= "/showDegreeTheses.do?method=showTheses&amp;degreeID=" + request.getParameter("degreeID") %>">
		<bean:message key="public.degree.information.link.thesis.showPublication" bundle="PUBLIC_DEGREE_INFORMATION"/>
	</html:link> |
	<bean:message key="public.degree.information.link.thesis.showState" bundle="PUBLIC_DEGREE_INFORMATION"/>
</div>

<logic:empty name="theses">
	<em>
		<bean:message key="public.degree.information.label.theses.empty.message" bundle="PUBLIC_DEGREE_INFORMATION"/>
	</em>
</logic:empty>

<logic:notEmpty name="theses">
	<ul>
		<logic:iterate id="thesis" name="theses" type="net.sourceforge.fenixedu.domain.thesis.Thesis">
			<li>
				<div>
					<p class="mvert0">
						<fr:view name="thesis" property="student.number"/>,
						<fr:view name="thesis" property="student.person.name" layout="person-name"/>,
						<em><fr:view name="thesis" property="finalFullTitle"/></em>
					</p>
					<p class="greytxt mvert0">
						<bean:define id="state" name="thesis" property="state"/>
						<bean:message key="<%= "label.theses.state." + state %>" bundle="PUBLIC_DEGREE_INFORMATION"/>.
					</p>
				</div>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

