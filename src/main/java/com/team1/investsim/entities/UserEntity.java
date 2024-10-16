package com.team1.investsim.entities;

import com.team1.investsim.entities.types.UserType;
import static com.team1.investsim.utils.DocumentUtil.isValidDocument;
import static com.team1.investsim.utils.UserUtil.*;

import com.team1.investsim.exceptions.InvalidDocumentException;
import com.team1.investsim.exceptions.InvalidEmailException;
import com.team1.investsim.exceptions.InvalidPasswordException;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portofolios", referencedColumnName = "id")
    private PortfolioEntity portfolio;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidEmailException {
        if (!isValidEmail(email)) throw new InvalidEmailException("Email inválido");
        this.email = email;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) throws InvalidDocumentException {
        if (!isValidDocument(document)) throw new InvalidDocumentException("O documento informado é inválido");
        this.document = document;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidPasswordException {
        if (!isValidPassword(password)) throw new InvalidPasswordException("Senha inválida. Para ser válida é necessário ter pelo menos 8 dígitos, uma letra minúscula, uma letra maiúscula e um caractere especial");
        this.password = password;
    }

    public PortfolioEntity getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return id == that.id || Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, document);
    }
}