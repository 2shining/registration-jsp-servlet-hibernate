package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OCCUPER")
public class Occuper {
    
    @EmbeddedId
    private OccuperId id;

    @ManyToOne
    @JoinColumn(name = "codeprof", referencedColumnName = "codeprof", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "codesal", referencedColumnName = "codesal", insertable = false, updatable = false)
    private Salle salle;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    // ✅ Constructeur vide
    public Occuper() {}

    // ✅ Constructeur avec OccuperId et Date
    public Occuper(OccuperId id, Date date) {
        this.id = id;
        this.date = date;
    }

    // ✅ Getters et Setters
    public OccuperId getId() { return id; }
    public void setId(OccuperId id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }
}
