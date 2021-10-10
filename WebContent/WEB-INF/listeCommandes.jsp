<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Liste des commandes existantes</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>
<body>
	<c:import url="/inc/menu.jsp" />
	<div id="corps">
		<c:choose>
			<c:when test="${ empty sessionScope.commandes }">
				<p class="erreur">Aucune commande enregistrée.</p>
			</c:when>
			<c:otherwise>
				<table>
					<tr>
						<th>Client</th>
						<th>Date</th>
						<th>Montant</th>
						<th>Mode de paiement</th>
						<th>Statut de paiement</th>
						<th>Mode de livraison</th>
						<th>Statut de livraison</th>
						<th class="action">Action</th>
					</tr>
					<c:forEach items="${ sessionScope.commandes }" var="mapCommandes"
						varStatus="boucle">
						<tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
							<td><c:out value="${ mapCommandes.value.client.prenom } ${ mapCommandes.value.client.nom }" /></td>
							<td><c:out value="${ mapCommandes.value.date }" /></td>
							<td><c:out value="${ mapCommandes.value.montant }" /></td>
							<td><c:out value="${ mapCommandes.value.modePaiement }" /></td>
							<td><c:out value="${ mapCommandes.value.statutPaiement }" /></td>
							<td><c:out value="${ mapCommandes.value.modeLivraison }" /></td>
							<td><c:out value="${ mapCommandes.value.statutLivraison }" /></td>
							<%-- Lien vers la servlet de suppression, avec passage de la date de la commande - c'est-à-dire la clé de la Map - en paramètre grâce à la balise <c:param/>. --%>
							<td class="action"><a
								href="<c:url value="/suppressionCommande"><c:param name="dateCommande" value="${ mapCommandes.key }" /></c:url>">
									<img src="<c:url value="/inc/supprimer.png"/>" alt="Supprimer" />
							</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>