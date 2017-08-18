package com.liemily.stock;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Emily Li on 23/07/2017.
 */
@Repository
@Lazy
public interface StockRepository extends JpaRepository<Stock, String> {
    @Modifying
    @Query("UPDATE Stock SET volume = volume - ?2 WHERE symbol = ?1 AND volume - ?2 >= 0")
    int remove(String stockSymbol, int volume);

    @Modifying
    @Query("UPDATE Stock SET volume = volume + ?2 WHERE symbol = ?1")
    int add(String stockSymbol, int volume);
}
