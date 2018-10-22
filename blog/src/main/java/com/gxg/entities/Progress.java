package com.gxg.entities;

/**
 * 上传进度信息
 * @author 郭欣光
 * @date 2018/10/22 15:01
 */
public class Progress {

    private long bytesRead; //已读取文件的比特数
    private long contentLength;//文件总比特数
    private long items; //正读的第几个文件

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getItems() {
        return items;
    }

    public void setItems(long items) {
        this.items = items;
    }
}
