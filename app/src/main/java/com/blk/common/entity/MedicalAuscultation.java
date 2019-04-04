package com.blk.common.entity;


/**
 * 就医听诊表
 */

public class MedicalAuscultation {
    //编号
    private int mid;
    //音频地址
    private String videoUrl;
    //音频内容
    private String content;
    //用户id
    private int uid;
    //操作时间
    private String operTime;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }
}
