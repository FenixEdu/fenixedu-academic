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
