package com.team1.investsim.entities;

import org.springframework.data.domain.Persistable;

public interface Identifiable {
    long getId();
    void setId(long id);
}