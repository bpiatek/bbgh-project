package com.github.bpiatek.bbghbackend.model.comment;

import static com.github.bpiatek.bbghbackend.model.comment.CommentOpinionStatus.NOT_CHECKED;
import static javax.persistence.EnumType.STRING;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.bpiatek.bbghbackend.model.article.Article;
import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String author;
  private LocalDateTime dateAdded;
  private String content;

  @Enumerated(STRING)
  private CommentOpinionStatus commentOpinionStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private Article article;

  public Comment(String author, String content, LocalDateTime dateAdded) {
    this.author = author;
    this.content = content;
    this.dateAdded = dateAdded;
    this.commentOpinionStatus = NOT_CHECKED;
  }

  @Override
  public String toString() {
    return "Comment{" +
           "id=" + id +
           ", author='" + author + '\'' +
           ", content='" + content + '\'' +
           ", dateAdded='" + dateAdded + '\'' +
           '}';
  }
}