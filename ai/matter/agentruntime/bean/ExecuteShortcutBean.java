package ai.matter.agentruntime.bean;

/**
 * 执行快捷指令bean
 */
public class ExecuteShortcutBean {
    private String shortcutName;
    private String executeShortcutKey;//执行快捷指令的快捷键
    private String openJarvisOneKey = "0x02";//打开oneAction的快捷指令
    private int clickAllowNum = 0;//点击允许弹窗次数
    private int clickAllowInterval = 500;//点击弹窗间隔（查找间隔时间）

    public String getOpenJarvisOneKey() {
        return openJarvisOneKey;
    }

    public void setOpenJarvisOneKey(String openJarvisOneKey) {
        this.openJarvisOneKey = openJarvisOneKey;
    }

    public int getClickAllowNum() {
        return clickAllowNum;
    }

    public void setClickAllowNum(int clickAllowNum) {
        this.clickAllowNum = clickAllowNum;
    }

    public int getClickAllowInterval() {
        return clickAllowInterval;
    }

    public void setClickAllowInterval(int clickAllowInterval) {
        this.clickAllowInterval = clickAllowInterval;
    }

    public String getShortcutName() {
        return shortcutName;
    }

    public void setShortcutName(String value) {
        this.shortcutName = value;
    }

    public String getExecuteShortcutKey() {
        return executeShortcutKey;
    }

    public void setShortcutKeys(String value) {
        this.executeShortcutKey = value;
    }

    @Override
    public String toString() {
        return "ExecuteShortcutBean{" +
                "shortcutName='" + shortcutName + '\'' +
                ", shortcutKey='" + executeShortcutKey + '\'' +
                '}';
    }
}
