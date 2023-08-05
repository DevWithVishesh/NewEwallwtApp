package com.icsd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.icsd.auth.token.Token;
import com.icsd.auth.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Customer implements UserDetails {
    @Id
    @GeneratedValue
    private int customerId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String contactNo;

    @OneToOne
    @JoinColumn(name = "addressFk")
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "pwd")
    private String password;
    @Transient
    private String confirmPassword;
    private LocalDateTime registrationDate;

    @OneToOne
    private CustomerDocuments customerDocument;

    private LocalDate expireDate;

    @OneToMany(targetEntity = Account.class, mappedBy = "customer")
    private List<Account> accounts = new ArrayList<Account>();
    @OneToMany(mappedBy = "customer")
    private List<Token> tokens;
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", address=" + address +
                ", gender=" + gender +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", registerationdate=" + registrationDate +
                ", customerDocument=" + customerDocument +
                ", expiredate=" + expireDate +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.USER.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}