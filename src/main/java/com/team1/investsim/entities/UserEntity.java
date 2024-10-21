package com.team1.investsim.entities;

import com.team1.investsim.entities.types.UserType;
import static com.team1.investsim.utils.DocumentUtil.*;
import static com.team1.investsim.utils.UserUtil.*;

import com.team1.investsim.exceptions.InvalidDocumentException;
import com.team1.investsim.exceptions.InvalidEmailException;
import com.team1.investsim.exceptions.InvalidPasswordException;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "portofolios", referencedColumnName = "id")
    private PortfolioEntity portfolio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TokenEntity> tokens;

    public List<TokenEntity> getTokens() {
        return tokens;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public UserEntity() {}

    private UserEntity(String email, String document, String password, UserType type) {
        this.email = email;
        this.document = document;
        this.password = password;
        this.type = type;
    }

    public static UserEntity createUser(String document, String email, String password) throws InvalidEmailException, InvalidPasswordException, InvalidDocumentException {
        Optional<String> treatedDocument = validateAndClearDocument(document);
        Optional<UserType> userType = getTypeByDocument(document);
        if (userType.isEmpty() || treatedDocument.isEmpty()) throw new InvalidDocumentException("Documento informado é inválido.");

        return new UserEntity(validateEmail(email), treatedDocument.get(), password, userType.get());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidEmailException {
        this.email = validateEmail(email);
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) throws InvalidDocumentException {
        Optional<String> treatedDocument = validateAndClearDocument(document);
        if (treatedDocument.isEmpty()) throw new InvalidDocumentException("Documento informado é inválido");
        this.document = treatedDocument.get();
    }

    public UserType getType() {
        return type;
    }

    public void setType(String document) throws InvalidDocumentException {
        Optional<UserType> userType = getTypeByDocument(document);
        if (userType.isEmpty()) throw new InvalidDocumentException("Documento informado é inválido");
        this.type = userType.get();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidPasswordException {
        this.password = validatePassword(password);
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
        return id == that.id || Objects.equals(document, that.document) || Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, document, email);
    }
}