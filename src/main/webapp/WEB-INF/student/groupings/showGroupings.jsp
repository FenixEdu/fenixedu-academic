<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<spring:url var="phase" value="/student/groups/phase" />
<div ng-app="studentGroupingsApp">
<div ng-view>
</div>
</div>
    ${portal.bennuPortal()}
    
<script>
	var strings = {
		groupingShiftChange : "${fr:message('resources.ApplicationResources', 'message.student.studentGroup.shiftChange')}",
		groupCreated : "${fr:message('resources.ApplicationResources', 'message.student.studentGroup.created')}",
		studentEnrolledGroup : "${fr:message('resources.ApplicationResources', 'message.student.studentGroup.enrolled')}",
		studentUnenrolledGroup : "${fr:message('resources.ApplicationResources', 'message.student.studentGroup.unrolled')}"
	}
</script>
<script
	src="${pageContext.request.contextPath}/bennu-core/js/angular.min.js"></script>
<script
	src="${pageContext.request.contextPath}/bennu-portal/js/angular-route.min.js"></script>
<script src="${pageContext.request.contextPath}/student/groupings/studentGroupingsApp.js"></script>
