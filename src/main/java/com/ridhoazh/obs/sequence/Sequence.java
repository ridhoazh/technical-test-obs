package com.ridhoazh.obs.sequence;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

@Entity
@Table(name = "sequence")
@SuppressWarnings("deprecation")
public class Sequence {

    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name = "ordinal", length = 20, columnDefinition = "NUMBER")
    private Long ordinal = 0L;

    @Column(name = "last_update")
    private LocalDate lastUpdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Long ordinal) {
        this.ordinal = ordinal;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}


