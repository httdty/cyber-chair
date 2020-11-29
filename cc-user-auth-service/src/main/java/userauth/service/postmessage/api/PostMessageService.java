package userauth.service.postmessage.api;

import userauth.domain.PostMessage;
import userauth.request.postmessage.PostMessageRequest;
import userauth.utility.response.ResponseWrapper;

import java.util.List;

public interface PostMessageService {

    public void addPostMessage(PostMessageRequest postMessageRequest);

    public List<PostMessage> findPostMessageByArticleIdAndStatus(long articleId, String status);

    public PostMessage findPostMessageById(Long id);
}
