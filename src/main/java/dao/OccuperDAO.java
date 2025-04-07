package dao;

import model.Occuper;
import model.OccuperId;
import model.Salle;
import model.User;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OccuperDAO {

    // ✅ Ajouter une occupation
	public void ajouterOccuper(Occuper occuper) {
	    Transaction transaction = null;
	    Session session = null;

	    // Vérification si le professeur et la salle existent
	    if (!professeurExiste(occuper.getId().getCodeprof())) {
	        System.out.println("Erreur : Le professeur avec code " + occuper.getId().getCodeprof() + " n'existe pas !");
	        return;
	    }
	    if (!salleExiste(occuper.getId().getCodesal())) {
	        System.out.println("Erreur : La salle avec code " + occuper.getId().getCodesal() + " n'existe pas !");
	        return;
	    }

	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        transaction = session.beginTransaction();
	        session.save(occuper);
	        transaction.commit();
	        System.out.println("Occupation ajoutée avec succès !");
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    } finally {
	        if (session != null) session.close();
	    }
	}

    // ✅ Récupérer toutes les occupations
	public List<Occuper> getAllOccupations() {
	    Session session = null;
	    List<Occuper> occupations = null;
	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        session.beginTransaction();
	        
	        Query query = session.createQuery("FROM Occuper");
	        occupations = query.list();
	        
	        session.getTransaction().commit();

	        // Vérification du nombre de résultats
	        System.out.println("Nombre d'occupations récupérées : " + (occupations != null ? occupations.size() : 0));

	        return occupations;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null) session.close();
	    }
	    return occupations;
	}

	public List<User> getAllProfesseurs() {
	    Session session = null;
	    List<User> professeurs = null;
	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        session.beginTransaction();
	        
	        Query query = session.createQuery("FROM User");
	        professeurs = query.list();
	        
	        session.getTransaction().commit();
	        return professeurs;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null) session.close();
	    }
	    return professeurs;
	}

	// ✅ Récupérer toutes les salles
	public List<Salle> getAllSalles() {
	    Session session = null;
	    List<Salle> salles = null;
	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        session.beginTransaction();
	        
	        Query query = session.createQuery("FROM Salle");
	        salles = query.list();
	        
	        session.getTransaction().commit();
	        return salles;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null) session.close();
	    }
	    return salles;
	}


    // ✅ Supprimer une occupation
    public boolean deleteOccupation(OccuperId id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Occuper occuper = (Occuper) session.get(Occuper.class, id);
            if (occuper != null) {
                session.delete(occuper);
                transaction.commit();
                System.out.println("Occupation supprimée avec succès !");
                return true;
            } else {
                System.out.println("Occupation introuvable.");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

 // ✅ Mettre à jour une occupation
    public boolean updateOccupation(OccuperId id, String newDateString) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Occuper occuper = (Occuper) session.get(Occuper.class, id);
            if (occuper != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date newDate = dateFormat.parse(newDateString);
                
                occuper.setDate(newDate);
                session.update(occuper);
                transaction.commit();
                System.out.println("Occupation mise à jour avec succès !");
                return true;
            } else {
                System.out.println("Occupation introuvable pour la mise à jour.");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    private boolean professeurExiste(int codeprof) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean existe = (session.get(model.User.class, codeprof) != null);
        session.close();
        return existe;
    }

    // Vérifier si une salle existe
    private boolean salleExiste(int codesal) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean existe = (session.get(model.Salle.class, codesal) != null);
        session.close();
        return existe;
    }
}
