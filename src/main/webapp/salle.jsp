<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% if (request.getAttribute("salles") == null) { response.sendRedirect("index"); } %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <meta charset="UTF-8">
    <title>Gestion des Salles</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --success-color: #28a745;
            --danger-color: #dc3545;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            background-color: #f8f9fa;
        }

        .header {
            background-color: var(--primary-color);
            padding: 1.5rem 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .header-title {
            color: white;
            margin: 0;
            font-size: 1.8rem;
        }

        .nav-buttons {
            display: flex;
            gap: 1rem;
        }

        .button {
            padding: 0.6rem 1.2rem;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.9rem;
        }

        .button.add { background-color: var(--success-color); }
        .button.edit { background-color: #ffc107; }
        .button.delete { background-color: var(--danger-color); }
        .button.back { background-color: #6c757d; }
        .button { color: white; }

        .button:hover {
            transform: translateY(-2px);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1.5rem;
        }

        .table-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .table-wrapper {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 1rem 1.2rem;
            text-align: left;
            border-bottom: 1px solid #f0f0f0;
        }

        th {
            background-color: var(--secondary-color);
            color: white;
            font-weight: 600;
            font-size: 1rem;
        }

        tr:hover {
            background-color: #fafafa;
        }

        .actions-cell {
            display: flex;
            gap: 0.6rem;
        }

        .icon-btn {
            background: none;
            border: none;
            cursor: pointer;
            color: var(--primary-color);
            transition: all 0.3s;
            padding: 0.4rem;
            font-size: 1.1rem;
        }

        .icon-btn:hover {
            color: var(--accent-color);
            transform: scale(1.1);
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
        }

        .modal-content {
            background-color: white;
            width: 380px;
            padding: 1.5rem;
            border-radius: 8px;
            position: relative;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .modal-title {
            margin-top: 0;
            color: var(--primary-color);
            font-size: 1.3rem;
            margin-bottom: 1.5rem;
        }

        .form-group {
            margin-bottom: 1rem;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.4rem;
            color: #666;
            font-size: 0.9rem;
        }

        .form-group input {
            width: 100%;
            padding: 0.5rem 0.8rem;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 0.9rem;
            box-sizing: border-box;
        }

        .close-btn {
            position: absolute;
            top: 1rem;
            right: 1rem;
            cursor: pointer;
            font-size: 1.5rem;
            color: #666;
            line-height: 1;
        }

        .empty-state {
            text-align: center;
            padding: 3rem 0;
            color: #666;
        }

        .empty-state i {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: #b0b0b0;
        }
        
		        /* Notifications */
		.notification {
		    position: fixed;
		    bottom: 20px;
		    right: -300px;
		    padding: 15px 25px;
		    border-radius: 6px;
		    color: white;
		    display: flex;
		    align-items: center;
		    gap: 12px;
		    opacity: 0.95;
		    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
		    z-index: 1000;
		    transition: right 0.5s ease-in-out;
		}
		
		.notification.show {
		    right: 20px;
		}
		
		.notification.success {
		    background-color: var(--success-color);
		}
		
		.notification.error {
		    background-color: var(--danger-color);
		}
		
		.notification i {
		    font-size: 1.2rem;
		}
		
		@media (max-width: 768px) {
		    .notification {
		        width: calc(100% - 40px);
		        left: 20px;
		        right: auto;
		    }
		    
		    .notification.show {
		        left: 20px;
		    }
		}
    </style>
</head>

	

<body>
	<!-- Notifications -->
    <c:if test="${not empty param.success}">
    <div class="notification success">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${param.success == 'ajout'}">Salle ajoutée avec succès !</c:when>
            <c:when test="${param.success == 'modification'}">Salle modifiée avec succès !</c:when>
            <c:when test="${param.success == 'suppression'}">Salle supprimée avec succès !</c:when>
        </c:choose>
    </div>
</c:if>

<c:if test="${not empty param.error}">
    <div class="notification error">
        <i class="fas fa-exclamation-triangle"></i>
        <c:choose>
            <c:when test="${param.error == 'designationVide'}">Le nom de la salle est requis</c:when>
            <c:when test="${param.error == 'missingId'}">ID de salle manquant</c:when>
            <c:when test="${param.error == 'invalidCode'}">Code de salle invalide</c:when>
            <c:otherwise>Erreur système</c:otherwise>
        </c:choose>
    </div>
</c:if>

    <c:if test="${not empty error}">
        <div class="notification error">
            <i class="fas fa-exclamation-triangle"></i>
            <c:choose>
                <c:when test="${error == 'designationVide'}">Le nom de la salle est requis</c:when>
                <c:when test="${error == 'missingId'}">ID de salle manquant</c:when>
                <c:when test="${error == 'invalidCode'}">Code de salle invalide</c:when>
                <c:otherwise>Erreur système</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <header class="header">
        <h1 class="header-title"><i class="fas fa-door-open"></i> Gestion des Salles</h1>
        <div class="nav-buttons">
            <button class="button back" onclick="window.location.href='index.jsp'">
                <i class="fas fa-arrow-left"></i> Retour
            </button>
        </div>
    </header>

    <div class="container">
        <div class="table-header">
            <button class="button add" onclick="openAddModal()">
                <i class="fas fa-plus"></i> Ajouter
            </button>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty salles}">
                            <tr>
                                <td colspan="3">
                                    <div class="empty-state">
                                        <i class="fas fa-door-closed"></i>
                                        <h3>Aucune salle trouvée</h3>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="salle" items="${salles}">
                                <tr>
                                    <td>${salle.codesal}</td>
                                    <td>${salle.designation}</td>
                                    <td>
                                        <div class="actions-cell">
                                            <button class="icon-btn" 
                                                onclick="openEditModal(
                                                    '${salle.codesal}', 
                                                    '${salle.designation}'
                                                )">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="icon-btn" onclick="confirmDelete('${salle.codesal}')">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Modal Ajout -->
    <div id="addSalleModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeAddModal()">&times;</span>
            <h2 class="modal-title">Nouvelle Salle</h2>
            <form action="SalleController" method="post">
                <input type="hidden" name="action" value="ajouter">
                <div class="form-group">
                    <label>Nom :</label>
                    <input type="text" name="designation" required>
                </div>
                <div style="text-align: right; margin-top: 1rem;">
                    <button class="button add" type="submit">
                        <i class="fas fa-save"></i> Enregistrer
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Modification -->
    <div id="editSalleModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeEditModal()">&times;</span>
            <h2 class="modal-title">Modifier Salle</h2>
            <form action="SalleController" method="post">
                <input type="hidden" name="action" value="modifier">
                <input type="hidden" name="codesal" id="editCodesal">
                <div class="form-group">
                    <label>Nom :</label>
                    <input type="text" name="designation" id="editDesignation" required>
                </div>
                <div style="text-align: right; margin-top: 1rem;">
                    <button class="button edit" type="submit">
                        <i class="fas fa-sync-alt"></i> Mettre à jour
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openAddModal() {
            document.getElementById("addSalleModal").style.display = "block";
        }

        function closeAddModal() {
            document.getElementById("addSalleModal").style.display = "none";
        }

        function openEditModal(codesal, designation) {
            document.getElementById("editCodesal").value = codesal;
            document.getElementById("editDesignation").value = designation;
            document.getElementById("editSalleModal").style.display = "block";
        }

        function closeEditModal() {
            document.getElementById("editSalleModal").style.display = "none";
        }

        function confirmDelete(codesal) {
            if (confirm("Voulez-vous vraiment supprimer cette salle ?")) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'SalleController';

                const action = document.createElement('input');
                action.type = 'hidden';
                action.name = 'action';
                action.value = 'supprimer';
                form.appendChild(action);

                const cs = document.createElement('input');
                cs.type = 'hidden';
                cs.name = 'codesal';
                cs.value = codesal;
                form.appendChild(cs);

                document.body.appendChild(form);
                form.submit();
            }
        }

        window.onclick = function(event) {
            if (event.target.classList.contains('modal')) {
                document.querySelectorAll('.modal').forEach(modal => {
                    modal.style.display = 'none';
                });
            }
        }
    </script>
    <script>
    // Animation améliorée des notifications
    document.addEventListener('DOMContentLoaded', () => {
        const notifications = document.querySelectorAll('.notification');
        
        notifications.forEach(notification => {
            // Force le recalcul du style pour déclencher l'animation
            void notification.offsetHeight;
            
            notification.style.transition = 'right 0.5s ease-in-out';
            notification.classList.add('show');
            
            setTimeout(() => {
                notification.classList.remove('show');
                setTimeout(() => {
                    notification.parentNode.removeChild(notification);
                }, 500);
            }, 4000);
        });
    });
</script>
    
</body>
<script>
	    // Gestion des notifications
	    document.addEventListener('DOMContentLoaded', function() {
	        const notifications = document.querySelectorAll('.notification');
	        
	        notifications.forEach(notification => {
	            setTimeout(() => notification.classList.add('show'), 100);
	            
	            setTimeout(() => {
	                notification.classList.remove('show');
	                setTimeout(() => notification.remove(), 500);
	            }, 4000);
	        });
	    });
	</script>
</html>