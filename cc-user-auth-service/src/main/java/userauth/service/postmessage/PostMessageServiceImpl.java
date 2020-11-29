package userauth.service.postmessage;

import userauth.domain.PostMessage;
import userauth.repository.PostRepository;
import userauth.request.postmessage.PostMessageRequest;
import userauth.service.postmessage.api.PostMessageService;
import userauth.utility.response.ResponseGenerator;
import userauth.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


@Service
public class PostMessageServiceImpl implements PostMessageService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public void addPostMessage(PostMessageRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostMessage post = new PostMessage(
                request.getPosterId(),
                request.getArticleId(),
                request.getTargetId(),
                request.getContent(),
                request.getStatus(),
                timestamp.toString()
        );
        postRepository.save(post);
    }

    @Override
    public List<PostMessage> findPostMessageByArticleIdAndStatus(long articleId, String status) {
        List<PostMessage> postList = postRepository.findByArticleIdAndStatus(articleId, status);
        return postList;
    }

    @Override
    public PostMessage findPostMessageById(Long id) {
        PostMessage message = postRepository.findById(id.longValue());
        return message;
    }
}
