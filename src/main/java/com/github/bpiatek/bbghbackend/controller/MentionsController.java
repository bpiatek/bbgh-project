package com.github.bpiatek.bbghbackend.controller;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.mention.Mention;
import com.github.bpiatek.bbghbackend.model.mention.MentionFacade;
import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import com.github.bpiatek.bbghbackend.model.mention.api.CreateMentionRequest;
import com.github.bpiatek.bbghbackend.model.player.Player;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;
import static org.mortbay.jetty.HttpStatus.ORDINAL_201_Created;


/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Log4j2
@Api(tags = "Mentions controller")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/mentions")
public class MentionsController {
  private final MentionFacade mentionFacade;
  private final CommentFacade commentFacade;
  private final PlayerFacade playerFacade;

  public MentionsController(MentionFacade mentionFacade, CommentFacade commentFacade, PlayerFacade playerFacade) {
    this.mentionFacade = mentionFacade;
    this.commentFacade = commentFacade;
    this.playerFacade = playerFacade;
  }

  @ApiOperation(value = "Search mentions")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all mentions"),
  })
  @ApiPageable
  @GetMapping
  Page<Mention> searchArticles(@ApiIgnore Pageable pageable) {
    return mentionFacade.search(pageable);
  }

  @ApiOperation(value = "Create mention.")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_201_Created, message = "Successfully created mention"),
  })
  @PostMapping
  ResponseEntity<Mention> createArticle(@RequestBody CreateMentionRequest createMentionRequest) {

    Mention mention = Mention.builder()
        .comment(this.commentFacade.findById(createMentionRequest.getCommentId()))
        .player(this.playerFacade.findById(createMentionRequest.getPlayerId()))
        .sentiment(createMentionRequest.getSentiment() != null ? createMentionRequest.getSentiment() : MentionSentiment.NOT_CHECKED)
        .build();

    this.mentionFacade.save(mention);
    return ResponseEntity.status(HttpStatus.CREATED).body(mention);
  }
}