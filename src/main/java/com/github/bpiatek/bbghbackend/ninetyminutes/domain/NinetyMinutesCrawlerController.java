package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.dao.ArticleRepository;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
class NinetyMinutesCrawlerController {

  public static final String NINETY_MINUTES_URL = "http://www.90minut.pl/";

  private final ArticleCreator articleCreator;
  private final ArticleRepository articleRepository;
  private final String crawlerTempFolder;

  NinetyMinutesCrawlerController(
      ArticleCreator articleCreator,
      ArticleRepository articleRepository,
      @Value("${crawler.storage.folder}") String crawlerTempFolder
  ) {
    this.articleCreator = articleCreator;
    this.articleRepository = articleRepository;
    this.crawlerTempFolder = crawlerTempFolder;
  }

  void run90minutesCrawler() throws Exception {
    CrawlConfig config = new CrawlConfig();
    config.setCrawlStorageFolder(crawlerTempFolder + "90minut");
    config.setPolitenessDelay(1000);
    config.setMaxDepthOfCrawling(-1);
    config.setMaxPagesToFetch(-1);
    final PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
    controller.addSeed(NINETY_MINUTES_URL);

    int numberOfCrawlers = 1;

    CrawlController.WebCrawlerFactory<NinetyMinutesCrawler> factory = () -> new NinetyMinutesCrawler(articleCreator, articleRepository);

    controller.startNonBlocking(factory, numberOfCrawlers);
  }
}