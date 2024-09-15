package az.edu.turing.unitech.domain.entity;

import az.edu.turing.unitech.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false, length = 15)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 15)
    private String lastName;

    @Column(name = "pin", nullable = false, unique = true, length = 7)
    @Size(min = 7, max = 7, message = ("Pin must be exactly 7 symbol!"))
    private String pin;

    @Column(nullable = false, length = 60)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)  // Fetch roles immediately when user is loaded
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();

    private String email;

    private LocalDateTime updateDate;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountEntity> accountEntities;
}
