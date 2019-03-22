package com.blk.medical_record.entity;

/**
 * Created by asus on 2018/2/6.
 */

public class HealthyNews {
    int  news_id;  //id
    String title;  // 标题
    String author_name; //作者
    String content;    //内容
    String image_cover;  //封面截图
    int collection_status; //收藏状态

    public HealthyNews() {}

    public HealthyNews(String title, String author_name, String content)
    {
        this.title = title;
        this.content = content;
        this.author_name = author_name;
    }

    public HealthyNews(int news_id, String title, String author_name, String content, String image_cover, int collection_status) {
        this.news_id = news_id;
        this.title = title;
        this.author_name = author_name;
        this.content = content;
        this.image_cover = image_cover;
        this.collection_status = collection_status;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public int getCollection_status() {
        return collection_status;
    }

    public void setCollection_status(int collection_status) {
        this.collection_status = collection_status;
    }


    public String getContent() {
        return content;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
