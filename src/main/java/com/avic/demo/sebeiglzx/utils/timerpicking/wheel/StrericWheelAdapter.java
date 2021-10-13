package com.avic.demo.sebeiglzx.utils.timerpicking.wheel;

public class StrericWheelAdapter implements WheelAdapter {

    /** The default min value */
    private String[] strContents;
    /**
     *
     * @param strContents
     */
    public StrericWheelAdapter(String[] strContents){
        this.strContents=strContents;
    }


    public String[] getStrContents() {
        return strContents;
    }


    public void setStrContents(String[] strContents) {
        this.strContents = strContents;
    }


    public String getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            return strContents[index];
        }
        return null;
    }

    public int getItemsCount() {
        return strContents.length;
    }
    /**
     * 鐠佸墽鐤嗛張锟姐亣閻ㄥ嫬顔旀惔锟?
     */
    public int getMaximumLength() {
        int maxLen=5;
        return maxLen;
    }
}
