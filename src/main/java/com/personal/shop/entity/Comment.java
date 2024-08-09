package com.personal.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "display_name")
    private String displayName;

    @Column(nullable = false, name = "rating")
    private Double rating;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "parent_id")
    private Long parentId;
}
