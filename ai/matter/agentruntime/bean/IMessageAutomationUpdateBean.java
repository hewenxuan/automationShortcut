package ai.matter.agentruntime.bean;

import ai.matter.agentruntime.bean.data.IMessageAutomationUpdateContact;
/**
 * 更新IMessage联系人bean
 */
public class IMessageAutomationUpdateBean {
    private String webAppLink;
    private String password;
    private String phoneModel;
    private String os;
    private IMessageAutomationUpdateContact iMessageAutomationUpdateContact;
    private String resolution;
    private boolean isWebApp;
    private String mac;
    private String versionCode;

    public String getWebAppLink() {
        return webAppLink;
    }

    public void setWebAppLink(String value) {
        this.webAppLink = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String value) {
        this.phoneModel = value;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String value) {
        this.os = value;
    }

    public IMessageAutomationUpdateContact getIMessageAutomationUpdate() {
        return iMessageAutomationUpdateContact;
    }

    public void setIMessageAutomationUpdate(IMessageAutomationUpdateContact value) {
        this.iMessageAutomationUpdateContact = value;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String value) {
        this.resolution = value;
    }

    public boolean getIsWebApp() {
        return isWebApp;
    }

    public void setIsWebApp(boolean value) {
        this.isWebApp = value;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String value) {
        this.mac = value;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String value) {
        this.versionCode = value;
    }
}


