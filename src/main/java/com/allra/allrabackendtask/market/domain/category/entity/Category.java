package com.allra.allrabackendtask.market.domain.category.entity;

import com.allra.allrabackendtask.market.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TB01CTGRY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends CommonEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false, unique=true, length=100)
    private String name;

    @Column(length=200)
    private String description;
}
