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
	<ul>
		<li>
			<html:link page="/manageGrantCostCenter.do?method=prepareManageGrantCostCenter">
		    	<bean:message key="link.manage.grant.costcenter"/>
		    </html:link>
		</li>
	</ul>
	<ul>
		<li>
			<html:link page="/manageGrantProject.do?method=prepareManageGrantProject">
		    	<bean:message key="link.manage.grant.project"/>
		    </html:link>
		</li>
	</ul>

	<br/>
	<strong>&raquo; <bean:message key="link.grant.list"/></strong>	
	<ul>
		<li>
			<html:link page="/listGrantOwner.do?method=prepareFirstTimeListGrantOwner&amp;spanNumber=1&amp;orderBy=orderByNumber&amp;numberOfElementsInSpan=100">
		    	<bean:message key="link.grant.owner.list"/>
		    </html:link>
		</li>
	</ul>
	<ul>
	<li>
		<html:link page="/listGrantContractByCriteria.do?method=actionStart">
	    	<bean:message key="link.grant.owner.list.byCriteria"/>
	    </html:link>
	</li>
	</ul>
	
	<br/>
	<strong>&raquo; <bean:message key="link.grant.correction"/></strong>	
	<ul>
		<li>
			<html:link page="/correctGrantOwner.do?method=prepareForm">
		    	<bean:message key="link.grant.owner.correction"/>
		    </html:link>
		</li>
	</ul>
		<ul>
		<li>
			<html:link page="/correctGrantContract.do?method=prepareForm&amp;action=deleteContract">
				<bean:message key="link.grant.contract.delete.correction"/>
		    </html:link>
		</li>
	</ul>
		<ul>
		<li>
			<html:link page="/correctGrantContract.do?method=prepareForm&amp;action=changeNumberContract">
		    	<bean:message key="link.grant.contract.change.number.correction"/>
		    </html:link>
		</li>
	</ul>
		<ul>
		<li>
			<html:link page="/correctGrantContract.do?method=prepareForm&amp;action=moveContract">
		    	<bean:message key="link.grant.contract.move.correction"/>
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
			<html:link page="/prepareManagementPositionsManagement.do?method=searchForm&amp;page=0" >
				<bean:message key="link.managementPositions.management"/>
			</html:link>
		</li>
		<li>
			<html:link page="/prepareServiceExemptionsManagement.do?method=searchForm&amp;page=0" >
				<bean:message key="link.serviceExemptions.management"/>
			</html:link>
		</li>
		<li>
			<html:link page="/teacherSearchForTeacherCreditsSheet.do?method=searchForm&amp;page=0" > 
				<bean:message key="link.creditsSheet.view"/>  
			</html:link> 
		</li>
	</ul>
	<br />
	<br />
	&raquo;	As funcionalidades abaixo <br />estarão brevemente disponíveis.
	<br />
	<b>Relatórios</b>
	<ul>
		<li>
			Cargos de gestão
		</li>
		<li>
			Situações de não exercício
		</li>
	</ul>
</logic:present>

