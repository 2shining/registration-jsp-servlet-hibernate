package dao;

import model.User;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import java.util.List;

public class UserDao {

    public void saveUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    // Méthode pour récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        Session session = null;
        List<User> users = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession(); // Ouvre la session Hibernate
            session.beginTransaction(); // Démarre la transaction
            
            // Utilise HQL pour récupérer tous les professeurs
            Query query = session.createQuery("FROM User");
            users = query.list(); // Exécute la requête et récupère les résultats
            if (users != null && !users.isEmpty()) {
                System.out.println("Nombre d'utilisateurs récupérés: " + users.size());
            } else {
                System.out.println("Aucun utilisateur trouvé");
            }
            // Ajouter un log pour vérifier combien d'éléments ont été récupérés
            System.out.println("Nombre d'utilisateurs récupérés: " + users.size());

            session.getTransaction().commit(); // Valide la transaction
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close(); // Ferme la session
            }
        }
        
        // Vérifier si la liste est vide
        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé dans la base de données.");
        }
        
        return users; // Retourne la liste des utilisateurs
    }
    
    public boolean deleteUser(int codeprof) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Recherche de l'utilisateur à supprimer
            User user = (User) session.get(User.class, codeprof);
            
            if (user != null) {
                session.delete(user);
                transaction.commit();
                System.out.println("Utilisateur supprimé avec succès : " + codeprof);
                return true;
            } else {
                System.out.println("Utilisateur introuvable : " + codeprof);
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
    
    public List<User> searchUsers(String searchTerm) {
        Session session = null;
        List<User> users = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            // Requête HQL avec LIKE pour rechercher par nom ou codeprof
            Query query = session.createQuery("FROM User WHERE LOWER(nom) LIKE LOWER(:searchTerm) OR CAST(codeprof AS string) LIKE :searchTerm");
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            users = query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return users;
    }
    
    
    public boolean updateUser(int codeprof, String nom, String prenom, String grade) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Récupérer l'utilisateur par son identifiant
            User user = (User) session.get(User.class, codeprof);
            if (user != null) {
                // Mettre à jour les informations de l'utilisateur
                user.setNom(nom);
                user.setPrenom(prenom);
                user.setGrade(grade);

                session.update(user); // Mettre à jour l'utilisateur dans la base de données
                transaction.commit();
                System.out.println("Utilisateur mis à jour avec succès : " + codeprof);
                return true;
            } else {
                System.out.println("Utilisateur introuvable pour la mise à jour : " + codeprof);
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