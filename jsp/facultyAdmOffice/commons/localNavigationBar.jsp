<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="GRANT_OWNER_MANAGER">
	<%-- GRANT OWNER MANAGEMENT APPLICATIONS  --%>
	<ul>
		<li class="navheader"><bean:message key="link.group.grantsManagement.title"/></li>
		<li>
			<html:link page="/searchGrantOwner.do?method=searchForm">
		    	<bean:message key="link.search.grant.owner"/>
		    </html:link>
		</li>

		<li>
			<html:link page="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
		    	<bean:message key="link.create.person.grant.owner"/>
		    </html:link>
		</li>
		
		<li class="navheader"><bean:message key="ling.group.grantPropertiesManagement.title"/></li>
		<li>
			<html:link page="/manageGrantType.do?method=prepareManageGrantTypeForm">
		    	<bean:message key="link.manage.grant.type"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/manageGrantCostCenter.do?method=prepareManageGrantCostCenter">
		    	<bean:message key="link.manage.grant.costcenter"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/manageGrantProject.do?method=prepareManageGrantProject">
		    	<bean:message key="link.manage.grant.project"/>
		    </html:link>
		</li>

		<li class="navheader"><bean:message key="link.grant.list"/></li>
		<li>
			<html:link page="/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=1&amp;orderBy=orderByNumber">
		    	<bean:message key="link.grant.owner.list"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/listGrantContractByCriteria.do?method=actionStart">
		    	<bean:message key="link.grant.owner.list.byCriteria"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/listGrantContractAndInsuranceByCriteria.do?method=actionStart">
		    	<bean:message key="link.grant.owner.list.byInsurance"/>
		    </html:link>
		</li>

		<li class="navheader"><bean:message key="link.grant.stats"/></li>
		<li>
			<html:link page="/grantOwnerStats.do?method=actionStart">
		    	<bean:message key="link.grant.owner.stats"/>
		    </html:link>
		</li>

		<li><bean:message key="link.grant.correction"/></li>
		<li>
			<html:link page="/correctGrantOwner.do?method=prepareForm">
		    	<bean:message key="link.grant.owner.correction"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/correctGrantContract.do?method=prepareForm&amp;action=deleteContract">
				<bean:message key="link.grant.contract.delete.correction"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/correctGrantContract.do?method=prepareForm&amp;action=changeNumberContract">
		    	<bean:message key="link.grant.contract.change.number.correction"/>
		    </html:link>
		</li>
		<li>
			<html:link page="/correctGrantContract.do?method=prepareForm&amp;action=moveContract">
		    	<bean:message key="link.grant.contract.move.correction"/>
		    </html:link>
		</li>
	</ul>	
</logic:present>
