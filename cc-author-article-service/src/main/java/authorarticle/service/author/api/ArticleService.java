package authorarticle.service.author.api;

import authorarticle.domain.Article;

import java.util.List;

/**
 * Service for other services
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */


public interface ArticleService {
    Article findArticleById(String id);
//    all List<Article> blow
    List<Article> findArticleByIdNot(String id);
    List<Article> findArticleByContributorName(String contributorName);
    List<Article> findArticleByMeetingName(String meetingName);
    List<Article> findArticleByMeetingNameAndStatus(String meetingName,String status);
    List<Article> findArticleByContributorNameAndMeetingName(String contributeName,String meetingName);
    Boolean saveArticle(Article article);
}
