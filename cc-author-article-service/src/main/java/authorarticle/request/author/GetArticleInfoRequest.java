package authorarticle.request.author;

/**
 * For Article Info Request
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */

public class GetArticleInfoRequest {
    private String userId;
    private String articleId;

    public GetArticleInfoRequest(String userId, String articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getArticleId() {
        return articleId;
    }
}