package pcmember.controller;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pcmember.rest.RestRPC;
import pcmember.service.*;
import pcmember.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class PcmemberController {
    @Autowired
    RestRPC restRPC;
    @Autowired
    ReviewService reviewService;
    @ApiOperation(value = "根据会议名称查看review状态", response = ResponseEntity.class)
    @GetMapping("/meeting/reviewArticles")
    public ResponseEntity<?> getInfoOfReview(String pcMemberName, String meetingName, @RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.getInfoOfReview(pcMemberName, meetingName));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "根据文章查看review状态", response = ResponseEntity.class)
    @GetMapping("/meeting/reviewArticle")
    public ResponseEntity<?> getInfoOfArticleToReview(String pcMemberName, String articleId,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.getInfoOfArticleToReview(pcMemberName, articleId));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "对当前会议的文章进行review，并存储数据", response = ResponseEntity.class)
    @PostMapping("/meeting/reviewer")
    public ResponseEntity<?> review(@RequestBody ReviewRequest request,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.review(request));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "根据articleId对文章进行review，并存储数据", response = ResponseEntity.class)
    @GetMapping("/user/reviews")
    public ResponseEntity<?> getAllReviewsOfArticle(String articleId,@RequestHeader("authorization") String token){
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.getReviewsOfArticle(articleId));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "查询已经review过的文章", response = ResponseEntity.class)
    @GetMapping("/meeting/alreadyReviewedInfo")
    public ResponseEntity<?> getAlreadyReviewedInfo(String pcMemberName, String articleId,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.getAlreadyReviewedInfo(pcMemberName, articleId));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "开始review前的一些配置", response = ResponseEntity.class)
    @PostMapping("/meeting/beginReview")
    public ResponseEntity<?> beginReview(@RequestBody BeginReviewRequest request,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.beginReview(request));
        else return ResponseEntity.badRequest().build();
    }
//    @PostMapping("/meeting/publish")
//    public ResponseEntity<?> reviewPublish(@RequestBody ResultPublishRequest request,@RequestHeader("authorization") String token) {
//        if(restRPC.cheackAuth(token))
//            return ResponseEntity.ok(reviewService.reviewPublish(request));
//        else return ResponseEntity.badRequest().build();
//    }
    @ApiOperation(value = "拒稿后再次review的操作", response = ResponseEntity.class)
    @PostMapping("/meeting/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest request,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.updateReview(request));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "review分为多次确认步骤", response = ResponseEntity.class)
    @PostMapping("/meeting/reviewConfirm")
    public ResponseEntity<?> reviewConfirm(@RequestBody ReviewConfirmRequest request,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.reviewConfirm(request));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "拒稿操作", response = ResponseEntity.class)
    @PostMapping("/meeting/rebuttal")
    public ResponseEntity<?> rebuttal(@RequestBody RebuttalRequest request,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.rebuttal(request));
        else return ResponseEntity.badRequest().build();
    }
    @ApiOperation(value = "获取拒稿信息", response = ResponseEntity.class)
    @GetMapping("/meeting/rebuttalInfo")
    public ResponseEntity<?> getRebuttalInfo(String articleId,@RequestHeader("authorization") String token) {
        if(restRPC.cheackAuth(token))
            return ResponseEntity.ok(reviewService.getRebuttalInfo(articleId));
        else return ResponseEntity.badRequest().build();
    }
//    @PostMapping("/meeting/finalPublish")
//    public ResponseEntity<?> finalPublish(@RequestBody FinalPublishRequest pcmember.request) {
//        return ResponseEntity.ok(reviewService.finalPublish(pcmember.request));
//    }
}
