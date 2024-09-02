package com.sparta.spartanewsfeed.entity;

import com.sparta.spartanewsfeed.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(unique = true, length = 100)
    private String email;
    private String password;
    @Column(length = 100)
    private String name;
    private String address;
    @Column(name = "delete_status")
    private Boolean deleteStatus;

    public void changeName(String name) { this.name = name; }
    public void changeAddress(String address) { this.address = address; }
    public void changePassword(String password) { this.password = password; }

    public User(UserRequestDto dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.deleteStatus = false;
    }

}
