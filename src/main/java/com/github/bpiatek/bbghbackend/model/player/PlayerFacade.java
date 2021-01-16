package com.github.bpiatek.bbghbackend.model.player;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_DOWN;

import com.github.bpiatek.bbghbackend.model.mention.Mention;

import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PlayerFacade {

  private final PlayerRepository playerRepository;

  public Player save(Player player) {
    return playerRepository.save(player);
  }

  public Player findById(Long id) {
    log.debug("Looking for PLAYER with ID: {}", id);
    return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
  }

  public Integer findLastSavedPlayer() {
    log.debug("Find last saved PLAYER ID in database");
    return playerRepository.findLastPlayerIdRead();
  }

  public Page<Player> findAll(Pageable pageable) {
    return playerRepository.findAll(pageable);
  }

  public Page<Player> search(String text, Pageable pageable) {
    log.info("Searching for player: {}", text);
    return playerRepository.search(text, pageable);
  }

  public SentimentCounter playerPercentage(List<Mention> mentions) {
    SentimentCounter sentimentCounter = populateSentimentCounter(mentions);
    if (noMentions(sentimentCounter) || noNegativeMentions(sentimentCounter)) {
      return sentimentCounter;
    }

    BigDecimal positiveAndNegative = sentimentCounter.getPositive().add(sentimentCounter.getNegative());

    // (POSITIVE / (POSITIVE + NEGATIVE) ) * 100
    BigDecimal ratio = sentimentCounter.getPositive()
        .divide(positiveAndNegative, 2, HALF_DOWN)
        .multiply(new BigDecimal("100"));

    sentimentCounter.setRatio(ratio);

    return sentimentCounter;
  }

  private boolean noMentions(SentimentCounter sentimentCounter) {
    return sentimentCounter.getNegative().add(sentimentCounter.getPositive()).compareTo(ZERO) == 0;
  }

  private boolean noNegativeMentions(SentimentCounter sentimentCounter) {
    return sentimentCounter.getNegative().compareTo(ZERO) == 0;
  }

  private SentimentCounter populateSentimentCounter(List<Mention> mentions) {
    int positive = 0;
    int negative = 0;
    int neutral = 0;
    int notChecked = 0;

    for (Mention response : mentions) {
      switch (response.getSentiment()) {
        case NEUTRAL:
          neutral++;
          break;
        case NEGATIVE:
          negative++;
          break;
        case POSITIVE:
          positive++;
          break;
        case NOT_CHECKED:
          notChecked++;
          break;
        default:
      }
    }

    return new SentimentCounter(valueOf(positive), valueOf(negative), valueOf(neutral), valueOf(notChecked), ZERO);
  }
}
