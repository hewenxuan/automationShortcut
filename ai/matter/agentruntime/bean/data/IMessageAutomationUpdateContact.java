package ai.matter.agentruntime.bean.data;

import ai.matter.agentruntime.bean.GroupKeyBean;

/**
 * IMessage自动化更新
 */
public class IMessageAutomationUpdateContact {
    private String automationNameTag;//获取自动化TAG，用于查找指定自动化
    private long getContactsWaitTime;
    private GroupKeyBean groupKeys;//快捷组合键

    private String phones = "";//要增加的手机号码

    private long executeGetContactsNum;//要执行多少次快捷键，获取通讯录数据

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public GroupKeyBean getGroupKeys() {
        return groupKeys;
    }

    public void setGroupKeys(GroupKeyBean groupKeys) {
        this.groupKeys = groupKeys;
    }

    public String getAutomationNameTag() {
        return automationNameTag;
    }

    public void setAutomationNameTag(String automationNameTag) {
        this.automationNameTag = automationNameTag;
    }

    public long getGetContactsWaitTime() {
        return getContactsWaitTime;
    }

    public void setGetContactsWaitTime(long value) {
        this.getContactsWaitTime = value;
    }

    public long getExecuteGetContactsNum() {
        return executeGetContactsNum;
    }

    public void setExecuteGetContactsNum(long value) {
        this.executeGetContactsNum = value;
    }
}