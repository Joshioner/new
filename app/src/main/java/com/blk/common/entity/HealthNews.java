package com.blk.common.entity;


/**
 * 健康资讯信息表
 */
public class HealthNews {
    //编号
    private int nid;
    //标题
    private String title;
    //作者
    private String author;
    //封面截图
    private String coverUrl;
    //状态
    private int status;
    //是否置顶
    private int isTop;
    //内容
    private String content;
    //操作时间
    private String operTime;
    //收藏状态
    private  int collection_status; //收藏状态

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public int getCollection_status() {
        return collection_status;
    }

    public void setCollection_status(int collection_status) {
        this.collection_status = collection_status;
    }
}
