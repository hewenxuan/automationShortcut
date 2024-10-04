package ai.matter.agentruntime.bean.data;

import ai.matter.agentruntime.bean.GroupKeyBean;

/**
 * IMessage自动化配置
 */
public class IMessageAutomationConfig {
    private String executeShortcutName;//自动化执行（上传信息快捷指令名称）
    private String getPasteboardPhoneShortcutKeys;//获取手机号快捷指令快捷键
    private GroupKeyBean groupKeys;//快捷组合键

    private String shortcutKey = "";
    private int executeGetContactsNum;//要执行多少次快捷键，获取通讯录数据

    private int configureIMessageAutomationSuccessNums;//配置iMessage自动化成功次数
    private long getContactsWaitTime = 10000;//获取通讯录联系人等待时间

    public String getShortcutKey() {
        return shortcutKey;
    }

    public void setShortcutKey(String shortcutKey) {
        this.shortcutKey = shortcutKey;
    }

    public int getConfigureIMessageAutomationSuccessNums() {
        return configureIMessageAutomationSuccessNums;
    }

    public void setConfigureIMessageAutomationSuccessNums(int configureIMessageAutomationSuccessNums) {
        this.configureIMessageAutomationSuccessNums = configureIMessageAutomationSuccessNums;
    }

    public GroupKeyBean getGroupKeys() {
        return groupKeys;
    }

    public void setGroupKeys(GroupKeyBean groupKeys) {
        this.groupKeys = groupKeys;
    }

    public long getGetContactsWaitTime() {
        return getContactsWaitTime;
    }

    public void setGetContactsWaitTime(long getContactsWaitTime) {
        this.getContactsWaitTime = getContactsWaitTime;
    }

    public String getExecuteShortcutName() {
        return executeShortcutName;
    }

    public void setExecuteShortcutName(String value) {
        this.executeShortcutName = value;
    }

    public String getGetPasteboardPhoneShortcutKeys() {
        return getPasteboardPhoneShortcutKeys;
    }

    public void setGetPasteboardPhoneShortcutKeys(String value) {
        this.getPasteboardPhoneShortcutKeys = value;
    }


    public int getExecuteGetContactsNum() {
        return executeGetContactsNum;
    }

    public void setExecuteGetContactsNum(int value) {
        this.executeGetContactsNum = value;
    }
}
