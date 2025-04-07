package util;

import org.hibernate.cfg.Configuration;

public class TestHibernate {
    public static void main(String[] args) {
        try {
            System.out.println("⏳ Chargement de hibernate.cfg.xml...");
            new Configuration().configure("hibernate.cfg.xml");
            System.out.println("✅ Fichier de configuration trouvé !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement du fichier !");
            e.printStackTrace();
        }
    }
}
