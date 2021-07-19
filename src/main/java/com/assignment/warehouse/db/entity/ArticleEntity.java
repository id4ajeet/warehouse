package com.assignment.warehouse.db.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Table(name = "article")
public class ArticleEntity {
    @Id
    private String id;
    private String name;
    private String stock;
}
