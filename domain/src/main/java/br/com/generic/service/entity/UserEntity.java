package br.com.generic.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "tb_users")
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "id", length = 255, unique = true, nullable = false)
    private String id;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "doc", length = 255)
    private String doc;

    @Column(name = "cellphone", length = 255)
    private String cellphone;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "is_send_email", nullable = false)
    private Boolean isSendEmail = false;

    @Column(name = "dt_birthday")
    private Date dtBirthday;

    @Column(name = "sms_token", length = 255)
    private String smsToken;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;


    @Column(name = "nm_created", nullable = false, length = 255)
    private String nmCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_created", nullable = false, updatable = false)
    private Date dtCreated;

    @Column(name = "nm_edited", length = 255)
    private String nmEdited;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_updated", insertable = false)
    private Date dtUpdated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }
}

