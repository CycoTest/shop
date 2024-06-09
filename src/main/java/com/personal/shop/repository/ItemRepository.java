package com.personal.shop.repository;

import com.personal.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findPageBy(Pageable page);
    Slice<Item> findSliceBy(Pageable page);
}
