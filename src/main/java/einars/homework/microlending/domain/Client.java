package einars.homework.microlending.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Client.
 */
@Entity
@Table(name = "client", catalog = "microlending")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Loan> loans = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<LoanExtension> loanExtensions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public Client loans(Set<Loan> loans) {
        this.loans = loans;
        return this;
    }

    public Client addLoan(Loan loan) {
        this.loans.add(loan);
        loan.setClient(this);
        return this;
    }

    public Client removeLoan(Loan loan) {
        this.loans.remove(loan);
        loan.setClient(null);
        return this;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public Set<LoanExtension> getLoanExtensions() {
        return loanExtensions;
    }

    public Client loanExtensions(Set<LoanExtension> loanExtensions) {
        this.loanExtensions = loanExtensions;
        return this;
    }

    public Client addLoanExtension(LoanExtension loanExtension) {
        this.loanExtensions.add(loanExtension);
        loanExtension.setClient(this);
        return this;
    }

    public Client removeLoanExtension(LoanExtension loanExtension) {
        this.loanExtensions.remove(loanExtension);
        loanExtension.setClient(null);
        return this;
    }

    public void setLoanExtensions(Set<LoanExtension> loanExtensions) {
        this.loanExtensions = loanExtensions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
