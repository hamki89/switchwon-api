package com.switchwon.switchwonapi.user.entity;

import com.switchwon.switchwonapi.support.jpa.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "switchwon_user")
public class User extends BaseTimeEntity {
    @Id
    private String id;

    private String name;
}
