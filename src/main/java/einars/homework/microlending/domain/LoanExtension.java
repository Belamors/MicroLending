package einars.homework.microlending.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A LoanExtension.
 */
@Entity
@Table(name = "loan_extension", catalog = "microlending")
public class LoanExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "duration", nullable = false)
    private LocalDate duration;

    @ManyToOne(optional = false)
    @NotNull
    private Loan loan;

    @ManyToOne(optional = false)
    @NotNull
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDuration() {
        return duration;
    }

    public LoanExtension duration(LocalDate duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(LocalDate duration) {
        this.duration = duration;
    }

    public Loan getLoan() {
        return loan;
    }

    public LoanExtension loan(Loan loan) {
        this.loan = loan;
        return this;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Client getClient() {
        return client;
    }

    public LoanExtension client(Client client) {
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
        LoanExtension loanExtension = (LoanExtension) o;
        if (loanExtension.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loanExtension.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LoanExtension{" +
            "id=" + getId() +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
