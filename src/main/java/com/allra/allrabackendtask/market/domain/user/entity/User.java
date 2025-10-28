package com.allra.allrabackendtask.market.domain.user.entity;

import com.allra.allrabackendtask.market.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB02USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends CommonEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(length = 200)
    private String address;


}

