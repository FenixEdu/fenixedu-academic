<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<ul class="treemenu">
	
<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
	<fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" 
		layout="side-menu">
            <fr:layout>
                <fr:property name="sectionUrl" value="/viewHomepage.do?method=section"/>
            </fr:layout>
	</fr:view>
</logic:present>

<logic:notPresent name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
		<li>
			<html:link page="/viewHomepage.do?method=listTeachers">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.teachers"/>
			</html:link>
	    </li>
		<li>
			<html:link page="/viewHomepage.do?method=listEmployees">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.employees"/>
			</html:link>
	    </li>
		<li>
			<html:link page="/viewHomepage.do?method=listStudents">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.students"/>
			</html:link>
	    </li>
		<li>
			<html:link page="/viewHomepage.do?method=listAlumni">
				<bean:message bundle="HOMEPAGE_RESOURCES" key="link.homepage.listings.alumni"/>
			</html:link>
	    </li>
    </logic:notPresent>
</ul>

