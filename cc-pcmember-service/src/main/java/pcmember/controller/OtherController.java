package pcmember.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pcmember.repository.RebuttalRepository;
import pcmember.repository.ReviewRelationRepository;
import pcmember.utility.Rebuttal;
import pcmember.utility.ReviewRelation;

import java.util.List;
import java.util.Set;

@RestController
public class OtherController {
    @Autowired
    RebuttalRepository rebuttalRepository;
    @Autowired
    ReviewRelationRepository reviewRelationRepository;
    @ApiOperation(value = "根据Id查rebuttal表", response = Rebuttal.class)
    @GetMapping("/rebuttal/findById")
    public ResponseEntity<Rebuttal> rebuttalFindById(long id){
        return ResponseEntity.ok(rebuttalRepository.findById(id));
    }
    @ApiOperation(value = "根据ArticleId查rebuttal表", response = Rebuttal.class, responseContainer = "List")
    @GetMapping("/rebuttal/findByArticleId")
    public ResponseEntity<List<Rebuttal>> rebuttalFindByArticleId(long articleId){
        return ResponseEntity.ok(rebuttalRepository.findByArticleId(articleId));
    }
    @ApiOperation(value = "根据Id查rebuttal表中不是这个Id的记录", response = Rebuttal.class, responseContainer = "List")
    @GetMapping("/rebuttal/findByIdNot")
    public ResponseEntity<List<Rebuttal>> rebuttalFindByIdNot(long idNot){
        return ResponseEntity.ok(rebuttalRepository.findByIdNot(idNot));
    }
    @ApiOperation(value = "将rebuttal对象存入rebuttal表", response = Rebuttal.class, responseContainer = "List")
    @PostMapping("/rebuttal/save")
    public ResponseEntity<?> rebuttalSave(Rebuttal rebuttal){
        return ResponseEntity.ok(rebuttalRepository.save(rebuttal));
    }

    @ApiOperation(value = "根据Id查reviewRelation表", response = ReviewRelation.class)
    @GetMapping("/reviewRelation/findById")
    public ResponseEntity<ReviewRelation> reviewFindById(long id){
        return ResponseEntity.ok(reviewRelationRepository.findById(id));
    }
    @ApiOperation(value = "根据ArticleId查reviewRelation表", response = ReviewRelation.class, responseContainer = "Set")
    @GetMapping("/reviewRelation/findReviewRelationsByArticleId")
    public ResponseEntity<Set<ReviewRelation>> reviewFindReviewRelationsByArticleId(long articleId){
        return ResponseEntity.ok(reviewRelationRepository.findReviewRelationsByArticleId(articleId));
    }
    @ApiOperation(value = "根据ReviewerId和MeetingId查reviewRelation表", response = ReviewRelation.class, responseContainer = "List")
    @GetMapping("/reviewRelation/findByReviewerIdAndMeetingId")
    public ResponseEntity<List<ReviewRelation>> reviewFindByReviewerIdAndMeetingId(long reviewerId, long meetingId){
        return ResponseEntity.ok(reviewRelationRepository.findByReviewerIdAndMeetingId(reviewerId,meetingId));
    }
    @ApiOperation(value = "根据ReviewerId和ArticleId查reviewRelation表", response = ReviewRelation.class)
    @GetMapping("/reviewRelation/findByReviewerIdAndArticleId")
    public ResponseEntity<ReviewRelation> reviewFindByReviewerIdAndArticleId(long reviewerId, long articleId){
        return ResponseEntity.ok(reviewRelationRepository.findByReviewerIdAndArticleId(reviewerId,articleId));
    }
    @ApiOperation(value = "根据Id查reviewRelation表中不是这个Id的对象", response = ReviewRelation.class,responseContainer = "List")
    @GetMapping("/reviewRelation/findByIdNot")
    public ResponseEntity<List<ReviewRelation>> reviewFindByIdNot(long id){
        return ResponseEntity.ok(reviewRelationRepository.findByIdNot(id));
    }
    @ApiOperation(value = "根据ReviewStatus查reviewRelation表", response = ReviewRelation.class,responseContainer = "List")
    @GetMapping("/reviewRelation/findByReviewStatus")
    public ResponseEntity<List<ReviewRelation>> reviewFindByReviewStatus(String reviewStatus){
        return ResponseEntity.ok(reviewRelationRepository.findByReviewStatus(reviewStatus));
    }
    @ApiOperation(value = "根据ArticleId查reviewRelation表", response = ReviewRelation.class,responseContainer = "List")
    @GetMapping("/reviewRelation/findByArticleId")
    public ResponseEntity<List<ReviewRelation>> reviewFindByArticleId(long articleId){
        return ResponseEntity.ok(reviewRelationRepository.findByArticleId(articleId));
    }
    @ApiOperation(value = "根据MeetingId查reviewRelation表", response = ReviewRelation.class,responseContainer = "List")
    @GetMapping("/reviewRelation/findByMeetingId")
    public ResponseEntity<List<ReviewRelation>> reviewFindByMeetingId(long meetingId){
        return ResponseEntity.ok(reviewRelationRepository.findByMeetingId(meetingId));
    }
    @ApiOperation(value = "根据ReviewStatus和MeetingId查reviewRelation表", response = ReviewRelation.class,responseContainer = "List")
    @GetMapping("/reviewRelation/findByReviewStatusAndMeetingId")
    public ResponseEntity<List<ReviewRelation>> reviewFindByReviewStatusAndMeetingId(String reviewStatus, long meetingId){
        return ResponseEntity.ok(reviewRelationRepository.findByReviewStatusAndMeetingId(reviewStatus,meetingId));
    }
    @ApiOperation(value = "将reviewRelation对象存入表中")
    @PostMapping("/reviewRelation/save")
    public ResponseEntity<?> reviewSave(ReviewRelation reviewRelation){
        return ResponseEntity.ok(reviewRelationRepository.save(reviewRelation));
    } //可以不写@RequestParam
//    @GetMapping("/test")
//    public ResponseEntity<String> test(){
//        return ResponseEntity.ok("ok");
//    }
}
