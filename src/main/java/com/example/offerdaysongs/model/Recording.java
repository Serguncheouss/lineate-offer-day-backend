package com.example.offerdaysongs.model;

import liquibase.pro.packaged.E;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
public class Recording {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //FIXME: Maybe replace id type by primitive long?
    // In all other model classes uses long
    // -- Sergey Strelchenko, 20.02.2022
    Long id;
    String title;
    String version;
    ZonedDateTime releaseTime;
    @OneToOne(fetch = FetchType.LAZY)
    //FIXME: If we try insert or update a recording entity
    // in the database we get entity with 'null' in the Singer field
    // If it is not right, we will need replace the insertable and updatable parameters by 'true'
    // -- Sergey Strelchenko, 20.02.2022
    @JoinColumn(name = "id", insertable = false, updatable = false)
    Singer singer;
}
