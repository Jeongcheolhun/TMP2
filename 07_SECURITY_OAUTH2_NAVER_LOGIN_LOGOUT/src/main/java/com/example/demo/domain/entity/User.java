package com.example.demo.domain.entity;

import com.example.demo.domain.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="user")
public class User {
    @Id
    private String username;
    private String password;
    private String role;

    //OAUTH2
    private String provider;
    private String providerId;

    public static UserDto entityToDto(User user) {
        UserDto dto = UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .build();

        return dto;
    }
}
