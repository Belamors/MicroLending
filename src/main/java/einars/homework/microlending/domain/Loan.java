package einars.homework.microlending.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Loan.
 */
@Entity
@Table(name = "loan", catalog = "microlending")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Float amount;

    @NotNull
    @Column(name = "accepted", nullable = false)
    private Boolean accepted;

    @Column(name = "term")
    private LocalDate term;

    @NotNull
    @Column(name = "ip", nullable = false)
    private String ip;

    @ManyToOne(optional = false)
    @NotNull
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public Loan amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public Loan accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDate getTerm() {
        return term;
    }

    public Loan term(LocalDate term) {
        this.term = term;
        return this;
    }

    public void setTerm(LocalDate term) {
        this.term = term;
    }

    public String getIp() {
        return ip;
    }

    public Loan ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Client getClient() {
        return client;
    }

    public Loan client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Loan loan = (Loan) o;
        if (loan.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Loan{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", accepted='" + isAccepted() + "'" +
            ", term='" + getTerm() + "'" +
            ", ip='" + getIp() + "'" +
            "}";
    }
}
