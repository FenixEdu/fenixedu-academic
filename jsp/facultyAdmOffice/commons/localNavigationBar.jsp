<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present role="grantOwnerManager">
	<%-- GRANT OWNER MANAGEMENT APPLICATIONS  --%>
	
	<strong>&raquo; <bean:message key="link.group.grantsManagement.title"/></strong>
	<ul>
		<li>
			<html:link page="/searchGrantOwner.do?method=searchForm">
		    	<bean:message key="link.search.grant.owner"/>
		    </html:link>
		</li>
	</ul>
	<ul>
		<li>
			<html:link page="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
		    	<bean:message key="link.create.person.grant.owner"/>
		    </html:link>
		</li>
	</ul>
	
	<br/>
	<strong>&raquo; <bean:message key="ling.group.grantPropertiesManagement.title"/></strong>
	<ul>
		<li>
			<html:link page="/manageGrantType.do?method=prepareManageGrantTypeForm">
		    	<bean:message key="link.manage.grant.type"/>
		    </html:link>
		</li>
	</ul>
	
	<br/>
	
</logic:present>

<logic:present role="creditsManager">
	<%-- TEACHER CREDITS MANAGEMENT APPLICATIONS --%>
	<strong>&raquo; <bean:message key="link.group.creditsManagement.title"/></strong>
	<ul>
			<li>
				<html:link action="/teacherSearch?method=searchForm&amp;page=0" >
					<bean:message key="link.teacher.credits.sheet.management"/>
				</html:link>
			</li>
	</ul>
</logic:present>

