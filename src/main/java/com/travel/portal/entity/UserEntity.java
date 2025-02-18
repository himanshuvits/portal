package com.travel.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TBL_USER")
public class UserEntity {
    @Column(name = "userId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int userId;

    @Column(name = "userName")
    String userName;

    @Column(name = "userEmail")
    String userEmail;

    @Column(name = "userBand")
    String userBand;
}
