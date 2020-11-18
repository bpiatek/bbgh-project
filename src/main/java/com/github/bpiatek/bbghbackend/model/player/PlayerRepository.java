package com.github.bpiatek.bbghbackend.model.player;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
interface PlayerRepository extends Repository<Player, Long> {
  Player save(Player player);

  Optional<Player> findById(Long id);

  @Query(value = "SELECT max(player.url_id) FROM player", nativeQuery = true)
  Integer findLastPlayerIdRead();

  Page<Player> findAll(Pageable pageable);
}
