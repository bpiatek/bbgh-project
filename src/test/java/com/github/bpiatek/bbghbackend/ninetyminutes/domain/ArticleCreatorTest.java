package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestUtils.*;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.github.bpiatek.bbghbackend.BbghBackendApplication;
import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.ninetyminutes.utils.TestsConfiguration;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 08/12/2020
 */
@SpringBootTest(classes = {
    NinetyMinutesCommentsExtractor.class,
    NinetyMinutesArticleExtractor.class,
    CommentCreator.class,
    ArticleCreator.class,
    TextToLocalDateTimeParser.class
})
@ContextConfiguration(classes = {BbghBackendApplication.class})
@ExtendWith(MockitoExtension.class)
class ArticleCreatorTest {

  @Autowired
  private ArticleCreator articleCreator;

  @Test
  void shouldFindNewCommentInOldArticle() {
    // given
    Article firstRun = articleCreator.createFromPage(createPage(), createHtmlParseData(readHtmlTestFile(HTML_EXAMPLE_FILE_4)), Optional.empty());
    int firstRunCommentsCount = firstRun.getComments().size();

    // when
    Article secondRun = articleCreator.createFromPage(createPage(), createHtmlParseData(readHtmlTestFile(HTML_EXAMPLE_FILE_4_EXTRA_COMMENT)), of(firstRun));

    // then
    assertThat(firstRunCommentsCount).isLessThan(secondRun.getComments().size());
  }

  private Page createPage() {
    WebURL wevUrl = new WebURL();
    wevUrl.setURL("example.com");
    return new Page(wevUrl);
  }

  private HtmlParseData createHtmlParseData(String html) {
    HtmlParseData htmlParseData = new HtmlParseData();
    htmlParseData.setHtml(html);

    return htmlParseData;
  }
}
