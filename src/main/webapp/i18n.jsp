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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration" %>

<div id="version">
<table>
	<tr>
		<c:forEach var="lang" items="<%= CoreConfiguration.supportedLocales() %>">
			<td>
			<!-- NO_CHECKSUM --><img src="${pageContext.request.contextPath}/images/flags/${lang.language}.gif" 
									 alt="${lang}" lang="${lang.toLanguageTag()}"
									 title="${lang}" class="locale-change-flag" style="cursor: pointer" />
			</td>
		</c:forEach>
	</tr>
</table>
</div>

<script>

$(".locale-change-flag").on("click", function (event) {
	$.ajax({
		type: 'POST',
		url: '${pageContext.request.contextPath}/api/bennu-core/profile/locale/' + $(event.currentTarget).attr('lang'),
		success: function () {
			location.reload();
		}
	});
});

</script>
