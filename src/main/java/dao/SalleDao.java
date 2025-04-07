package dao;

import model.Salle;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import java.util.List;

public class SalleDao {

    public void saveSalle(Salle salle) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(salle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Salle> getAllSalles() {
        Session session = null;
        List<Salle> salles = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            
            Query query = session.createQuery("FROM Salle");
            salles = query.list();
            
            if (salles != null && !salles.isEmpty()) {
                System.out.println("Nombre de salles récupérées: " + salles.size());
            } else {
                System.out.println("Aucune salle trouvée");
            }
            
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        
        if (salles == null || salles.isEmpty()) {
            System.out.println("Aucune salle trouvée dans la base de données.");
        }
        
        return salles;
    }
    
    public boolean deleteSalle(int codesal) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Salle salle = (Salle) session.get(Salle.class, codesal);
            if (salle != null) {
                session.delete(salle);
                transaction.commit();
                System.out.println("Salle supprimée avec succès : " + codesal);
                return true;
            } else {
                System.out.println("Salle introuvable : " + codesal);
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
    
    public boolean updateSalle(int codesal, String designation) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Salle salle = (Salle) session.get(Salle.class, codesal);
            if (salle != null) {
                salle.setDesignation(designation);
                session.update(salle);
                transaction.commit();
                System.out.println("Salle mise à jour avec succès : " + codesal);
                return true;
            } else {
                System.out.println("Salle introuvable pour la mise à jour : " + codesal);
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
}
