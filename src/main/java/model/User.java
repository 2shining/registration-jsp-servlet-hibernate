package model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codeprof;  
    
    private String nom;
    private String prenom;
    private String grade;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Occuper> occupations;

    public User() {}

    public User(String nom, String prenom, String grade) {
        this.nom = nom;
        this.prenom = prenom;
        this.grade = grade;
    }

    public int getCodeprof() { return codeprof; }
    public void setCodeprof(int codeprof) { this.codeprof = codeprof; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Set<Occuper> getOccupations() { return occupations; }
    public void setOccupations(Set<Occuper> occupations) { this.occupations = occupations; }
}
