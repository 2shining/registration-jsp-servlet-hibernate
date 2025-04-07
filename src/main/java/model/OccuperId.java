package model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OccuperId implements Serializable {
    
    @Column(name = "codeprof")
    private int codeprof;

    @Column(name = "codesal")
    private int codesal;

    // ✅ Constructeur vide obligatoire pour Hibernate
    public OccuperId() {}

    // ✅ Constructeur avec paramètres
    public OccuperId(int codeprof, int codesal) {
        this.codeprof = codeprof;
        this.codesal = codesal;
    }

    // ✅ Getters et Setters
    public int getCodeprof() { return codeprof; }
    public void setCodeprof(int codeprof) { this.codeprof = codeprof; }

    public int getCodesal() { return codesal; }
    public void setCodesal(int codesal) { this.codesal = codesal; }

    // ✅ Redéfinition de equals() et hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccuperId that = (OccuperId) o;
        return codeprof == that.codeprof && codesal == that.codesal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeprof, codesal);
    }
}
