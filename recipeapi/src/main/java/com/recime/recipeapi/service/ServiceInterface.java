package com.recime.recipeapi.service;

import java.util.List;
import java.util.Optional;

public interface ServiceInterface<T, Dto> {
    public Optional<T> save(T e);

    public List<T> save(List<T> e);

    public Optional<T> saveDto(Dto dto);
}
