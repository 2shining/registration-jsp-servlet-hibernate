<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    if (request.getAttribute("users") == null) {
        response.sendRedirect("index");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Professeurs</title>
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
        .button.navigate { background-color: var(--accent-color); }
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

        .search-section {
            display: flex;
            gap: 0.5rem;
            width: 400px;
        }

        .search-input {
            flex-grow: 1;
            padding: 0.6rem 1rem;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 0.9rem;
            height: 20px;
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
    align-items: flex-start
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
                box-sizing: border-box; /* Ajouter cette propriété */
            
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
		    bottom: 20px; /* Changé de top à bottom */
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
		    transition: right 0.5s ease-in-out; /* Animation uniquement sur right */
		}
		
		.notification.show {
		    right: 20px; /* Maintien de la même valeur pour right */
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
                <c:when test="${param.success == 'add'}">Professeur ajouté avec succès !</c:when>
                <c:when test="${param.success == 'edit'}">Professeur modifié avec succès !</c:when>
                <c:when test="${param.success == 'delete'}">Professeur supprimé avec succès !</c:when>
            </c:choose>
        </div>
    </c:if>
	
    <header class="header">
        <h1 class="header-title"><i class="fas fa-chalkboard-teacher"></i> Gestion des Professeurs</h1>
        <div class="nav-buttons">
            <button class="button navigate" onclick="window.location.href='SalleController'">
                <i class="fas fa-door-open"></i> Salles
            </button>
            <button class="button navigate" onclick="window.location.href='OccuperController'">
                <i class="fas fa-calendar-alt"></i> Occupations
            </button>
        </div>
    </header>
    
    
    <div class="container">
        <div class="table-header">
            <button class="button add" onclick="openAddModal()">
                <i class="fas fa-plus"></i> Ajouter
            </button>
            
            <div class="search-section">
                <form method="get" action="searchUser" style="display: contents;">
                    <input type="text" name="query" class="search-input" placeholder="Rechercher...">
                    <button type="submit" class="button navigate">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
            </div>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>Code Prof</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Grade</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty users}">
                            <tr>
                                <td colspan="5">
                                    <div class="empty-state">
                                        <i class="fas fa-user-slash"></i>
                                        <h3>Aucun professeur trouvé</h3>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.codeprof}</td>
                                    <td>${user.nom}</td>
                                    <td>${user.prenom}</td>
                                    <td>${user.grade}</td>
                                    <td>
                                        <div class="actions-cell">
                                            <button class="icon-btn" 
                                                onclick="openEditModal(
                                                    '${user.codeprof}', 
                                                    '${user.nom}', 
                                                    '${user.prenom}', 
                                                    '${user.grade}'
                                                )">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="icon-btn" onclick="confirmDelete('${user.codeprof}')">
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
    <div id="addProfModal" class="modal">
    <div class="modal-content">
        <span class="close-btn" onclick="closeAddModal()">&times;</span>
        <h2 class="modal-title">Nouveau Professeur</h2>
        <form action="addUser" method="post">
            <div class="form-group">
                <label>Nom :</label>
                <input type="text" name="nom" required>
            </div>
            <div class="form-group">
                <label>Prénom :</label>
                <input type="text" name="prenom" required>
            </div>
            <div class="form-group">
                <label>Grade :</label>
                <input type="text" name="grade" required>
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
    <div id="editProfModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeEditModal()">&times;</span>
            <h2 class="modal-title">Modifier Professeur</h2>
            <form action="editUser" method="post">
                <input type="hidden" name="codeprof" id="editCodeProf">
                <div class="form-group">
                    <label>Nom :</label>
                    <input type="text" name="nom" id="editNom" required>
                </div>
                <div class="form-group">
                    <label>Prénom :</label>
                    <input type="text" name="prenom" id="editPrenom" required>
                </div>
                <div class="form-group">
                    <label>Grade :</label>
                    <input type="text" name="grade" id="editGrade" required>
                </div>
                <button class="button edit" type="submit">
                    <i class="fas fa-sync-alt"></i> Mettre à jour
                </button>
            </form>
        </div>
    </div>

    <script>
    // Notifications
    document.addEventListener('DOMContentLoaded', function() {
        const notification = document.querySelector('.notification');
        if (notification) {
            setTimeout(() => notification.classList.add('show'), 100);
            setTimeout(() => {
                notification.classList.remove('show');
                setTimeout(() => notification.remove(), 500);
            }, 4000);
        }
    });
    
        // Gestion des modales
        function openAddModal() {
            document.getElementById("addProfModal").style.display = "block";
        }

        function closeAddModal() {
            document.getElementById("addProfModal").style.display = "none";
        }

        function openEditModal(codeprof, nom, prenom, grade) {
            document.getElementById("editCodeProf").value = codeprof;
            document.getElementById("editNom").value = nom;
            document.getElementById("editPrenom").value = prenom;
            document.getElementById("editGrade").value = grade;
            document.getElementById("editProfModal").style.display = "block";
        }

        function closeEditModal() {
            document.getElementById("editProfModal").style.display = "none";
        }

        function confirmDelete(codeprof) {
            if (confirm("Voulez-vous vraiment supprimer ce professeur ?")) {
                window.location.href = "deleteUser?codeprof=" + codeprof;
            }
        }

        // Fermer les modales en cliquant à l'extérieur
        window.onclick = function(event) {
            if (event.target.classList.contains('modal')) {
                document.querySelectorAll('.modal').forEach(modal => {
                    modal.style.display = 'none';
                });
            }
        }
    </script>
</body>
</html>