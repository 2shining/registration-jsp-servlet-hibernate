package web;

import dao.UserDao;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet({"/", "/index", "/addUser", "/deleteUser", "/editUser", "/searchUser"}) 
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/deleteUser".equals(action)) {
            deleteUser(request, response);
        } else if ("/searchUser".equals(action)) {
            searchUsers(request, response);
        } else {
            listUsers(request, response);
        }
    }
    
    private void searchUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        UserDao userDAO = new UserDao();
        List<User> users = userDAO.searchUsers(query);
        request.setAttribute("users", users);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/editUser".equals(action)) {
            editUser(request, response);
        } else if ("/addUser".equals(action)) {
            addUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Méthode non autorisée");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDao userDAO = new UserDao();
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String codeprofStr = request.getParameter("codeprof");
        if (codeprofStr != null) {
            try {
                int codeprof = Integer.parseInt(codeprofStr);
                UserDao userDAO = new UserDao();
                boolean deleted = userDAO.deleteUser(codeprof);
                if (deleted) {
                    System.out.println("Suppression réussie pour codeprof " + codeprof);
                } else {
                    System.out.println("Échec de la suppression, utilisateur non trouvé.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Format de codeprof invalide : " + codeprofStr);
            }
        } else {
            System.out.println("Aucun codeprof fourni pour la suppression.");
        }
        response.sendRedirect("index?success=delete");
    }
    
    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String codeprofStr = request.getParameter("codeprof");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String grade = request.getParameter("grade");

        if (codeprofStr != null && nom != null && prenom != null && grade != null) {
            try {
                int codeprof = Integer.parseInt(codeprofStr);
                UserDao userDAO = new UserDao();
                boolean updated = userDAO.updateUser(codeprof, nom, prenom, grade);
                if (updated) {
                    System.out.println("Modification réussie pour codeprof " + codeprof);
                } else {
                    System.out.println("Échec de la modification, utilisateur non trouvé.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Format de codeprof invalide : " + codeprofStr);
            }
        } else {
            System.out.println("Tous les champs doivent être remplis.");
        }
        response.sendRedirect("index?success=edit");
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String grade = request.getParameter("grade");

        if (nom != null && prenom != null && grade != null) {
            User user = new User();
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setGrade(grade);

            UserDao userDAO = new UserDao();
            userDAO.saveUser(user);

            System.out.println("Utilisateur ajouté : " + nom + " " + prenom + " (" + grade + ")");
        } else {
            System.out.println("Tous les champs sont requis pour l'ajout.");
        }
        response.sendRedirect("index?success=add");
    }
}
