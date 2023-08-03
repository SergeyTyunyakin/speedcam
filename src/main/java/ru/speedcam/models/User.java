package ru.speedcam.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.speedcam.forms.UserForm;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="users_seq")
    @SequenceGenerator(name="users_seq", sequenceName="user_id_seq", allocationSize = 1)
    @Column(name = "id", updatable=false, nullable=false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login")
    private String login;

    @Column(name = "hash_password")
    private String hashPassword;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private UserState state;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @OneToOne
    @JoinColumn(name = "region_code")
    private Region region;

    public static User from(UserForm form) {
        return User.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .build();
    }

    public String getRegionCode(){
        return getRegion().getRegionCode();
    }

}