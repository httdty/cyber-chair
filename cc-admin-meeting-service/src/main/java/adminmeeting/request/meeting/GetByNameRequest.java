package adminmeeting.request.meeting;

public class GetByNameRequest {
    private String meetingName;

    public GetByNameRequest(){}

    public GetByNameRequest(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingName() {
        return meetingName;
    }
    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

}
