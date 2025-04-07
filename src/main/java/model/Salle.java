package model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Salle {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codesal;  
    
    private String designation;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Occuper> occupations;

    public Salle() {}

    public Salle(String designation) {
        this.designation = designation;
    }

    public int getCodesal() { return codesal; }
    public void setCodesal(int codesal) { this.codesal = codesal; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Set<Occuper> getOccupations() { return occupations; }
    public void setOccupations(Set<Occuper> occupations) { this.occupations = occupations; }
}
