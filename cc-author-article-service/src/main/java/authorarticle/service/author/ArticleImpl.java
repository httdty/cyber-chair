package authorarticle.service.author;

import authorarticle.domain.Article;
import authorarticle.repository.ArticleRepository;
import authorarticle.service.author.api.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implement all functions in ArticleRepositoryService
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */

@Service
public class ArticleImpl implements ArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article findArticleById(String id) {
        return articleRepository.findById(Long.parseLong(id));
    }

    @Override
    public List<Article> findArticleByIdNot(String id) {
        return articleRepository.findByIdNot(Long.parseLong(id));
    }

    @Override
    public List<Article> findArticleByContributorName(String contributorName) {
        return articleRepository.findByContributorName(contributorName);
    }

    @Override
    public List<Article> findArticleByMeetingName(String meetingName) {
        return articleRepository.findByMeetingName(meetingName);
    }

    @Override
    public List<Article> findArticleByMeetingNameAndStatus(String meetingName, String status) {
        return articleRepository.findByMeetingNameAndStatus(meetingName, status);
    }

    @Override
    public List<Article> findArticleByContributorNameAndMeetingName(String contributeName, String meetingName) {
        return articleRepository.findByContributorNameAndMeetingName(contributeName, meetingName);
    }

    @Override
    public Boolean saveArticle(Article article) {
        try {
            articleRepository.save(article);
            return true;
        } catch (Exception ex) {
            System.out.println("bugging save article");
        }
        return false;
    }
}