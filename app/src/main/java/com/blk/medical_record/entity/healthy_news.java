package com.blk.medical_record.entity;

/**
 * Created by asus on 2018/2/6.
 */

public class healthy_news {
    String title;  // 标题
    String author_name; //作者
    String content;    //内容

    public healthy_news()
    {}

    public healthy_news(String title,String author_name,String content)
    {
        this.title = title;
        this.content = content;
        this.author_name = author_name;
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
