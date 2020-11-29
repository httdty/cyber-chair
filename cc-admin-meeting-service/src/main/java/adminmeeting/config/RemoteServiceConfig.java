package adminmeeting.config;

import adminmeeting.utility.ApiUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 服务调用配置文件，具体配置在application.properties中
 */
@Configuration
@ConfigurationProperties(prefix = "remote")
public class RemoteServiceConfig {

    private String check;
    private String findArticleById;
    private String findUserByUsername;
    private String findReviewRelationByReviewerIdAndMeetingId;
    private String findReviewRelationByReviewStatusAndMeetingId;
    private String findUserByName;
    private String findArticleByIdNot;
    private String findArticleByMeetingNameAndStatus;
    private String findUserByEmail;
    private String findReviewRelationByReviewerIdAndArticleId;
    private String saveReviewRelation;
    private String findUserById;
    private String findUserByFullnameAndEmail;
    private String saveArticle;
    private String findReviewRelationByArticleId;
    private String savePostMessage;
    private String findPostMessageByArticleIdAndStatus;
    private String findPostMessageById;
    private String findReviewRelationByMeetingId;
    private String saveRebuttal;
    private String findRebuttalByArticleId;
    private String findRebuttalByIdNot;
    private String findArticleByContributorNameAndMeetingName;

    public String getFindArticleByContributorNameAndMeetingName() {
        return findArticleByContributorNameAndMeetingName;
    }

    public void setFindArticleByContributorNameAndMeetingName(String findArticleByContributorNameAndMeetingName) {
        this.findArticleByContributorNameAndMeetingName = findArticleByContributorNameAndMeetingName;
    }

    public String getSaveRebuttal() {
        return saveRebuttal;
    }

    public void setSaveRebuttal(String saveRebuttal) {
        this.saveRebuttal = saveRebuttal;
    }

    public String getFindRebuttalByArticleId() {
        return findRebuttalByArticleId;
    }

    public void setFindRebuttalByArticleId(String findRebuttalByArticleId) {
        this.findRebuttalByArticleId = findRebuttalByArticleId;
    }

    public String getFindRebuttalByIdNot() {
        return findRebuttalByIdNot;
    }

    public void setFindRebuttalByIdNot(String findRebuttalByIdNot) {
        this.findRebuttalByIdNot = findRebuttalByIdNot;
    }

    public String getFindReviewRelationByMeetingId() {
        return findReviewRelationByMeetingId;
    }

    public void setFindReviewRelationByMeetingId(String findReviewRelationByMeetingId) {
        this.findReviewRelationByMeetingId = findReviewRelationByMeetingId;
    }

    public String getFindPostMessageById() {
        return findPostMessageById;
    }

    public void setFindPostMessageById(String findPostMessageById) {
        this.findPostMessageById = findPostMessageById;
    }


    public String getFindPostMessageByArticleIdAndStatus() {
        return findPostMessageByArticleIdAndStatus;
    }

    public void setFindPostMessageByArticleIdAndStatus(String findPostMessageByArticleIdAndStatus) {
        this.findPostMessageByArticleIdAndStatus = findPostMessageByArticleIdAndStatus;
    }
    public String getSavePostMessage() {
        return savePostMessage;
    }

    public void setSavePostMessage(String savePostMessage) {
        this.savePostMessage = savePostMessage;
    }



    public String getFindReviewRelationByArticleId() {
        return findReviewRelationByArticleId;
    }

    public void setFindReviewRelationByArticleId(String findReviewRelationByArticleId) {
        this.findReviewRelationByArticleId = findReviewRelationByArticleId;
    }

    public String getSaveArticle() {
        return saveArticle;
    }

    public void setSaveArticle(String saveArticle) {
        this.saveArticle = saveArticle;
    }

    public String getFindUserByFullnameAndEmail() {
        return findUserByFullnameAndEmail;
    }

    public void setFindUserByFullnameAndEmail(String findUserByFullnameAndEmail) {
        this.findUserByFullnameAndEmail = findUserByFullnameAndEmail;
    }

    public String getFindUserById() {
        return findUserById;
    }

    public void setFindUserById(String findUserById) {
        this.findUserById = findUserById;
    }


    public String getFindUserByEmail() {
        return findUserByEmail;
    }

    public void setFindUserByEmail(String findUserByEmail) {
        this.findUserByEmail = findUserByEmail;
    }

    public String getFindArticleByMeetingNameAndStatus() {
        return findArticleByMeetingNameAndStatus;
    }

    public void setFindArticleByMeetingNameAndStatus(String findArticleByMeetingNameAndStatus) {
        this.findArticleByMeetingNameAndStatus = findArticleByMeetingNameAndStatus;
    }

    public String getFindReviewRelationByReviewerIdAndArticleId() {
        return findReviewRelationByReviewerIdAndArticleId;
    }

    public void setFindReviewRelationByReviewerIdAndArticleId(String findReviewRelationByReviewerIdAndArticleId) {
        this.findReviewRelationByReviewerIdAndArticleId = findReviewRelationByReviewerIdAndArticleId;
    }

    public String getFindReviewRelationByReviewerIdAndMeetingId() {
        return findReviewRelationByReviewerIdAndMeetingId;
    }

    public void setFindReviewRelationByReviewerIdAndMeetingId(String findReviewRelationByReviewerIdAndMeetingId) {
        this.findReviewRelationByReviewerIdAndMeetingId = findReviewRelationByReviewerIdAndMeetingId;
    }

    public String getFindReviewRelationByReviewStatusAndMeetingId() {
        return findReviewRelationByReviewStatusAndMeetingId;
    }

    public void setFindReviewRelationByReviewStatusAndMeetingId(String findReviewRelationByReviewStatusAndMeetingId) {
        this.findReviewRelationByReviewStatusAndMeetingId = findReviewRelationByReviewStatusAndMeetingId;
    }
    public String getSaveReviewRelation() {
        return saveReviewRelation;
    }

    public void setSaveReviewRelation(String saveReviewRelation) {
        this.saveReviewRelation = saveReviewRelation;
    }

    public String getFindUserByUsername() {
        return findUserByUsername;
    }

    public void setFindUserByUsername(String findUserByUsername) {
        this.findUserByUsername = findUserByUsername;
    }



    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getFindArticleById() {
        return findArticleById;
    }

    public void setFindArticleById(String findArticleById) {
        this.findArticleById = findArticleById;
    }

    public String getFindUserByName() {
        return findUserByName;
    }
    public void setFindUserByName(String findUserByName) {
        this.findUserByName = findUserByName;
    }

    public String getFindArticleByIdNot() {
        return findArticleByIdNot;
    }
    public void setFindArticleByIdNot(String findUserByName) {
        this.findArticleByIdNot = findArticleByIdNot;
    }


}