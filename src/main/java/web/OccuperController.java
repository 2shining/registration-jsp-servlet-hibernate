package web;

import dao.OccuperDAO;
import model.Occuper;
import model.OccuperId;
import model.Salle;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/OccuperController")
public class OccuperController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OccuperDAO occuperDAO;
    private static final Logger LOGGER = Logger.getLogger(OccuperController.class.getName());

    @Override
    public void init() {
        occuperDAO = new OccuperDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Récupération des paramètres de notification
            String success = request.getParameter("success");
            String error = request.getParameter("error");

            // Chargement des données
            List<Occuper> occupations = occuperDAO.getAllOccupations();
            List<User> professeurs = occuperDAO.getAllProfesseurs();
            List<Salle> salles = occuperDAO.getAllSalles();

            // Stockage dans le contexte de requête
            request.setAttribute("occupations", occupations);
            request.setAttribute("professeurs", professeurs);
            request.setAttribute("salles", salles);
            
            // Transmission des paramètres de notification
            if(success != null) request.setAttribute("success", success);
            if(error != null) request.setAttribute("error", error);

            request.getRequestDispatcher("occuper.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des données", e);
            response.sendRedirect("error.jsp?message=Erreur de chargement des données");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                ajouterOccupation(request, response);
            } else if ("supprimer".equals(action)) {
                supprimerOccupation(request, response);
            } else if ("modifier".equals(action)) {
                modifierOccupation(request, response);
            } else {
                response.sendRedirect("OccuperController");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur dans la gestion des occupations", e);
            response.sendRedirect("error.jsp?message=Erreur de traitement");
        }
    }

    private void modifierOccupation(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
        try {
            int codeprof = Integer.parseInt(request.getParameter("codeprof"));
            int codesal = Integer.parseInt(request.getParameter("codesal"));
            String newDateStr = request.getParameter("date");

            if (newDateStr == null || newDateStr.trim().isEmpty()) {
                response.sendRedirect("OccuperController?error=dateManquante");
                return;
            }

            boolean updated = occuperDAO.updateOccupation(new OccuperId(codeprof, codesal), newDateStr);
            String redirect = updated ? 
                "OccuperController?success=modification" : 
                "OccuperController?error=echecModification";
            
            response.sendRedirect(redirect);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("OccuperController?error=idInvalide");
        } catch (Exception e) {
            response.sendRedirect("OccuperController?error=erreurSysteme");
        }
    }

    private void ajouterOccupation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Récupération des paramètres
        String codeprofStr = request.getParameter("codeprof");
        String codesalStr = request.getParameter("codesal");
        String dateStr = request.getParameter("date");

        try {
            // Validation des entrées
            if (codeprofStr == null || codesalStr == null || dateStr == null ||
                codeprofStr.isEmpty() || codesalStr.isEmpty() || dateStr.isEmpty()) {
                response.sendRedirect("OccuperController?error=champsManquants");
                return;
            }

            // Conversion des paramètres
            int codeprof = Integer.parseInt(codeprofStr);
            int codesal = Integer.parseInt(codesalStr);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            // Création des objets
            OccuperId id = new OccuperId(codeprof, codesal);
            Occuper occuper = new Occuper(id, date);

            // Appel DAO
            occuperDAO.ajouterOccuper(occuper);
            
            // Redirection succès
            response.sendRedirect("OccuperController?success=ajout");

        } catch (NumberFormatException e) {
            response.sendRedirect("OccuperController?error=formatInvalide");
        } catch (java.text.ParseException e) {
            response.sendRedirect("OccuperController?error=dateInvalide");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur critique lors de l'ajout", e);
            response.sendRedirect("OccuperController?error=erreurSysteme");
        }
    }

    private void supprimerOccupation(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
        try {
            int codeprof = Integer.parseInt(request.getParameter("codeprof"));
            int codesal = Integer.parseInt(request.getParameter("codesal"));

            OccuperId id = new OccuperId(codeprof, codesal);
            boolean deleted = occuperDAO.deleteOccupation(id);
            String redirect = deleted ? 
                "OccuperController?success=suppression" : 
                "OccuperController?error=echecSuppression";
            
            response.sendRedirect(redirect);

        } catch (NumberFormatException e) {
            response.sendRedirect("OccuperController?error=idInvalide");
        } catch (Exception e) {
            response.sendRedirect("OccuperController?error=erreurSysteme");
        }
    }
}