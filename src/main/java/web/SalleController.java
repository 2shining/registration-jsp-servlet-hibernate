package web;

import dao.SalleDao;

import model.Salle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/SalleController")
public class SalleController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SalleDao salleDao;
    private static final Logger LOGGER = Logger.getLogger(SalleController.class.getName());

    @Override
    public void init() {
        salleDao = new SalleDao();
    }

    // ✅ Gérer les requêtes GET (Affichage des salles)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Salle> salles = salleDao.getAllSalles();
            
            // Transmettre les paramètres à la JSP
            request.setAttribute("salles", salles);
            request.setAttribute("success", request.getParameter("success"));
            request.setAttribute("error", request.getParameter("error"));
            
            // Forward au lieu de redirect pour conserver les attributs
            request.getRequestDispatcher("salle.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des salles", e);
            response.sendRedirect("error.jsp");
        }
    }

    // ✅ Gérer les requêtes POST (Ajout, Modification, Suppression)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                ajouterSalle(request, response);
            } else if ("modifier".equals(action)) {
                modifierSalle(request, response);
            } else if ("supprimer".equals(action)) {
                supprimerSalle(request, response);
            } else {
                response.sendRedirect("SalleController");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur dans la gestion des salles", e);
            response.sendRedirect("error.jsp");
        }
    }

    // ✅ Ajouter une salle
    private void ajouterSalle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String designation = request.getParameter("designation");

        if (designation == null || designation.trim().isEmpty()) {
            LOGGER.warning("Tentative d'ajout d'une salle avec une désignation vide");
            response.sendRedirect("salle.jsp?error=designationVide");
            return;
        }

        Salle salle = new Salle(designation);
        salleDao.saveSalle(salle);
        response.sendRedirect("SalleController?success=ajout");
    }

    // ✅ Modifier une salle
    private void modifierSalle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String codeSalParam = request.getParameter("codesal");
            if (codeSalParam == null || codeSalParam.trim().isEmpty()) {
                LOGGER.warning("ID de salle manquant pour la modification");
                response.sendRedirect("salle.jsp?error=missingId");
                return;
            }

            int codesal = Integer.parseInt(codeSalParam);
            String designation = request.getParameter("designation");

            if (designation == null || designation.trim().isEmpty()) {
                LOGGER.warning("Tentative de modification avec une désignation vide");
                response.sendRedirect("salle.jsp?error=designationVide");
                return;
            }

            salleDao.updateSalle(codesal, designation);
            response.sendRedirect("SalleController?success=modification");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Erreur lors de la conversion du code salle", e);
            response.sendRedirect("salle.jsp?error=invalidCode");
        }
    }

    // ✅ Supprimer une salle
    private void supprimerSalle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String codeSalParam = request.getParameter("codesal");
            if (codeSalParam == null || codeSalParam.trim().isEmpty()) {
                LOGGER.warning("ID de salle manquant pour la suppression");
                response.sendRedirect("salle.jsp?error=missingId");
                return;
            }

            int codesal = Integer.parseInt(codeSalParam);
            salleDao.deleteSalle(codesal);
            response.sendRedirect("SalleController?success=suppression");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Erreur lors de la conversion du code salle pour suppression", e);
            response.sendRedirect("salle.jsp?error=invalidCode");
        }
    }
}
