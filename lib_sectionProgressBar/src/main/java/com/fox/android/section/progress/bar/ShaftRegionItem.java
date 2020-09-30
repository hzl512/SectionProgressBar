package com.fox.android.section.progress.bar;


/**
 * 类描述：分段显示区域的对象
 * 创建人：FoxHuang
 * 创建时间：2020/9/24 14:28
 */
public class ShaftRegionItem {

    private long startSection;

    private long endSection;

    private float startX;

    private float endX;

    private String startSectionStr;

    private String endSectionStr;

    public String getStartSectionStr() {
        return startSectionStr;
    }

    public void setStartSectionStr(String startSectionStr) {
        this.startSectionStr = startSectionStr;
    }

    public String getEndSectionStr() {
        return endSectionStr;
    }

    public void setEndSectionStr(String endSectionStr) {
        this.endSectionStr = endSectionStr;
    }

    public long getStartSection() {
        return startSection;
    }

    public void setStartSection(long startSection) {
        this.startSection = startSection;
    }

    public long getEndSection() {
        return endSection;
    }

    public void setEndSection(long endTimeMillis) {
        this.endSection = endTimeMillis;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    @Override
    public String toString() {
        return "ShaftRegionItem{" +
                "startSection=" + startSection +
                ", endSection=" + endSection +
                ", startX=" + startX +
                ", endX=" + endX +
                ", startSectionStr='" + startSectionStr + '\'' +
                ", endSectionStr='" + endSectionStr + '\'' +
                '}';
    }
}
