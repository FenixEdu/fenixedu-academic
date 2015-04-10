<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>


${portal.angularToolkit()}

<script
	src="${pageContext.request.contextPath}/bennu-portal/js/angular-route.min.js"></script>
<script
	src="${pageContext.request.contextPath}/manager/school/schoolManagementApp.js"></script>
<script
	src="${pageContext.request.contextPath}/manager/school/departmentsCtlr.js"></script>
<script
	src="${pageContext.request.contextPath}/manager/school/scientificAreasCtlr.js"></script>
<script
	src="${pageContext.request.contextPath}/manager/school/officesCtlr.js"></script>


<div ng-app="SchoolManagementApp">
	<div class="row">
		<div class="col-sm-12">
			<div ng-controller="HeaderCtrl">
				<ul class="nav nav-tabs nav-justified">
					<li role="presentation"
						ng-class="{ active: isActive('/department')}"><a
						href="#/departments">${fr:message('resources.ApplicationResources','label.manager.departments')}</a></li>
					<li role="presentation" ng-class="{ active: isActive('/office')}"><a
						href="#/offices">${fr:message('resources.ApplicationResources','label.manager.administrativeOffices')}</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div ng-view autoscroll="true"></div>
</div>

  
<script>
	var strings = {
		authorizationsSuccess : "${fr:message('resources.ApplicationResources', 'message.manager.authorizations.success')}",
		emptyDegreeSuccess : "${fr:message('resources.ApplicationResources', 'message.manager.emptyDegree.success')}",
	}


</script>