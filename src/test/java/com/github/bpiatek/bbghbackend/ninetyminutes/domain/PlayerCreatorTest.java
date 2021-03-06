package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.bpiatek.bbghbackend.model.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@SpringBootTest(classes ={
    TextToLocalDateTimeParser.class,
    PlayerCreator.class
})
class PlayerCreatorTest {

  @Autowired
  private PlayerCreator playerCreator;

  @Test
  void shouldCorrectlyCreatePlayerOneFromHtml() {
    // given
    String html = readHtmlTestFile(HTML_PLAYER_1_FILE);
    Player expectedPlayer = Player.builder()
        .firstName("Paweł")
        .lastName("Abbott")
        .dateOfBirth(LocalDate.of(1982, 5, 5))
        .currentTeam(null)
        .urlId(1)
        .build();

    // when
    Player actualPlayer = playerCreator.createFromHtml(html, 1);

    // then
    assertThat(actualPlayer).isEqualTo(expectedPlayer);
  }

  @Test
  void shouldCorrectlyCreatePlayerTwoFromHtml() {
    // given
    String html = readHtmlTestFile(HTML_PLAYER_2_FILE);
    Player expectedPlayer = Player.builder()
        .firstName("Ânderson")
        .lastName("Rodrigues Santana")
        .currentTeam(null)
        .dateOfBirth(LocalDate.of(1977, 6, 14))
        .urlId(1)
        .build();

    // when
    Player actualPlayer = playerCreator.createFromHtml(html, 1);

    // then
    assertThat(actualPlayer).isEqualTo(expectedPlayer);
  }

  @Test
  void shouldCorrectlyCreatePlayerThreeWithCurrentTeamFromHtml() {
    // given
    String html = readHtmlTestFile(HTML_PLAYER_3_FILE);
    Player expectedPlayer = Player.builder()
        .firstName("Robert")
        .lastName("Lewandowski")
        .currentTeam("FC Bayern München")
        .dateOfBirth(LocalDate.of(1988, 8, 21))
        .urlId(2)
        .build();

    // when
    Player actualPlayer = playerCreator.createFromHtml(html, 2);

    // then
    assertThat(actualPlayer).isEqualTo(expectedPlayer);
  }
}
