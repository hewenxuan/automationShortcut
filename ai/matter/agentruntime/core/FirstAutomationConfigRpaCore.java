package ai.matter.agentruntime.core;

import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_EXCEPTION_ERROR;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_2000;

import java.util.ArrayList;
import java.util.List;

import ai.matter.agentruntime.bean.AutomationShortcutConfigBean;
import ai.matter.agentruntime.bean.MqttMessageBean;
import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.bean.data.AutomationShortcutProgress;
import ai.matter.agentruntime.bean.data.Shortcut;
import ai.matter.agentruntime.manager.ExtKeyboardMouseManager;
import ai.matter.agentruntime.manager.FirstAutomationOtherConfigRpaManager;
import ai.matter.agentruntime.manager.LogServiceManager;
import ai.matter.agentruntime.manager.MqttConnectManager;
import ai.matter.agentruntime.manager.ShortcutManager;
import ai.matter.agentruntime.manager.VoiceInjectionManager;
import ai.matter.agentruntime.utils.GsonUtils;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.ShortcutUtil;
import ai.matter.agentruntime.utils.ThreadUtils;
import ai.matter.lightrpc.RpcServiceHelper;

/**
 * 首次自动化配置
 */
public class FirstAutomationConfigRpaCore {
    private static final String TAG = "Shortcut FirstAutomationConfigRpaCore ";

    /**
     * 开始配置
     */
    public static void startFirstConfig(MqttMessageBean mqttMessageBean, RpcServiceHelper.RpcCallback rpcCallback) {
        ThreadUtils.getCpuPool().execute(() -> {
            String traceId = mqttMessageBean.getTraceId();
            try {
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, new ResponseResult(RESPONSE_CODE_SUCCESS, "FirstAutomationConfigRpaCore start config"));//给服务端发消息，代表开始执行了
                LogServiceManager.getInstance().log(traceId, "receive startFirstConfig message，start config " + GsonUtils.toJson(mqttMessageBean));
                AutomationShortcutConfigBean configBean = GsonUtils.fromJson(GsonUtils.toJson(mqttMessageBean.getParams()), AutomationShortcutConfigBean.class);//获取配置bean
                if (configBean == null) {
                    LogUtils.e(TAG, "startFirstConfig，configBean is null");
                    LogServiceManager.getInstance().log(traceId, "startFirstConfig，configBean is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取自动化快捷指令配置bean失败"));
                    return;
                }
                AutomationShortcutProgress progressManagerToServer = GsonUtils.fromJson(GsonUtils.toJson(mqttMessageBean.getParams()), AutomationShortcutProgress.class);//获取进度管理器bean(给服务端回复进度用)
                if (progressManagerToServer == null) {
                    LogUtils.e(TAG, "startFirstConfig，progressManagerToServer is null");
                    LogServiceManager.getInstance().log(traceId, "progressManagerToServer，configBean is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取进度管理器bean失败progressManagerToServer"));
                    return;
                }
                AutomationShortcutConfigBean.ProgressManager progressManager = progressManagerToServer.getProgressManager();//当前进度情况判断使用
                if (progressManager == null) {
                    LogUtils.e(TAG, "startFirstConfig，progressManager is null");
                    LogServiceManager.getInstance().log(traceId, "progressManager，configBean is null");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "获取进度管理器bean失败progressManager"));
                    return;
                }

                int fullKeyboardControlAutoHideTime = configBean.getFullKeyboardControlAutoHideTime();//获取全键盘控制自动隐藏时间

                List<Shortcut> shortcuts = configBean.getShortcuts();//获取快捷指令列表
                String shortcutFolderName = configBean.getShortcutFolderName();//获取快捷指令文件夹名称
                String uniqueId = configBean.getUniqueId();//获取设备id

                //判断版本是否支持
//                if (configBean.getOs() == null || !configBean.getOs().equals("iOS")) {
//                    LogUtils.e(TAG, "startFirstConfig，version is not support");
//                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_FAIL, "版本不支持"));
//                    return;
//                }
                String phoneModel = configBean.getPhoneModel();
                String versionCode = configBean.getVersionCode();
//                ShortcutUtil.setMouseLocations(phoneModel, versionCode);
                ShortcutUtil.setMouseLocations("iphone15 pro", "17");
//                ShortcutUtil.setMouseLocations("iphone12", "17");

                boolean connect = ExtKeyboardMouseManager.getInstance().connect();//连接蓝牙
                if (connect) {
                    ResponseResult  openNarrationResult= VoiceInjectionManager.openNarration(traceId);//打开旁白
                    openNarrationResult.setData(GsonUtils.toJson(progressManagerToServer));//设置结果
                    if (!uploadProgress(traceId, openNarrationResult, rpcCallback)) {
                        LogUtils.e(TAG, "startFirstConfig，openNarration failed");
                        return;
                    }

                    //语音打开辅助触控功能
                    if (!progressManager.isOpenAssistiveTouch()) {//如果没有打开辅助触控功能
                        LogUtils.d(TAG, "startFirstConfig，openAssistiveTouch start");
                        ResponseResult responseResult = openAssistiveTouch(traceId);//打开辅助触控功能
                        progressManager.setOpenAssistiveTouch(responseResult.getCode() == RESPONSE_CODE_SUCCESS);  //设置是否打开辅助触控结果
                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));//设置辅助触控结果
                        LogUtils.d(TAG, "startFirstConfig，openAssistiveTouch result : " + GsonUtils.toJson(responseResult));
                        if (!uploadProgress(traceId, responseResult, rpcCallback)) {
                            endConfigProcess(traceId);
                            return;
                        }
                    } else {
                        LogUtils.d(TAG, "startFirstConfig，openAssistiveTouch is already open");
                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，openAssistiveTouch is already open");
                    }

                    //打开全键盘控制开关
                    if (!progressManager.isOpenFullKeyboardControl()) {//如果没有打开全键盘控制开关
                        Thread.sleep(SLEEP_TIME_1000);
                        LogUtils.d(TAG, "startFirstConfig，openFullKeyboardControl start");
                        ResponseResult responseResult = openFullKeyboardControl(traceId, fullKeyboardControlAutoHideTime);//打开全键盘控制开关
                        progressManager.setIsOpenFullKeyboardControl(responseResult.getCode() == RESPONSE_CODE_SUCCESS); //设置打开全键盘控制开关结果
                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));//设置打开全键盘控制开关结果
                        LogUtils.d(TAG, "startFirstConfig，openFullKeyboardControl result :" + GsonUtils.toJson(responseResult));
                        if (!uploadProgress(traceId, responseResult, rpcCallback)) {
                            endConfigProcess(traceId);
                            return;
                        }
                    } else {
                        LogUtils.d(TAG, "startFirstConfig，openFullKeyboardControl is already open");
                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，openFullKeyboardControl is already open");
                    }

                    //语音打开辅助触控设置页面，开始设置关闭始终显示菜单
                    if (!progressManager.isSetAssistiveTouchConfig()) {//如果没有设置辅助触控配置
                        Thread.sleep(SLEEP_TIME_2000);
                        LogUtils.d(TAG, "startFirstConfig，setAssistiveTouchConfig start");
                        ResponseResult responseResult = setAssistiveTouchConfig(traceId);//设置辅助触控配置
                        progressManager.setSetAssistiveTouchConfig(responseResult.getCode() == RESPONSE_CODE_SUCCESS);
                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));//设置辅助触控配置结果
                        LogUtils.d(TAG, "startFirstConfig，setAssistiveTouchConfig result :" + GsonUtils.toJson(responseResult));
                        uploadProgressAndContinue(traceId, responseResult, rpcCallback);
                    } else {
                        LogUtils.d(TAG, "startFirstConfig，setAssistiveTouchConfig is already set");
                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，setAssistiveTouchConfig is already set");
                    }


                    //创建快捷指令文件夹
//                    if (!progressManager.isCreateShortcutFolder()) {//如果没有创建快捷指令文件夹
//                        LogUtils.d(TAG, "startFirstConfig，createShortcutFolder start");
//                        ResponseResult responseResult = createShortcutFolder(shortcutFolderName, traceId);//创建快捷指令文件夹
//                        progressManager.setCreateShortcutFolder(responseResult.getCode() == RESPONSE_CODE_SUCCESS);
//                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));
//                        LogUtils.d(TAG, "startFirstConfig，createShortcutFolder result :" + GsonUtils.toJson(responseResult));
//                        LogUtils.d(TAG, "startFirstConfig，createShortcutFolder upload result：", GsonUtils.toJson(responseResult));
//                        MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, responseResult);//不管成功失败不结束，继续执行
////                        if (!upLoadProgress(traceId,responseResult, rpcCallback)) {
////                            endConfig(traceId);
////                            return;
////                        }
//                    } else {
//                        LogUtils.d(TAG, "startFirstConfig，createShortcutFolder is already created");
//                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，createShortcutFolder is already created");
//                    }

                    //安装快捷指令列表
                    List<Shortcut> needInstallShortcuts = getNeedInstallICloudLinks(shortcuts);//获取需要安装的快捷指令
                    if (!needInstallShortcuts.isEmpty()) {//如果有需要安装的快捷指令
                        Thread.sleep(SLEEP_TIME_2000);
                        LogUtils.d(TAG, "startFirstConfig，installShortcuts start");
                        ResponseResult responseResult = installShortcutList(shortcuts, progressManagerToServer, traceId, uniqueId);//安装快捷指令列表
                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));//设置安装快捷指令结果
                        LogUtils.d(TAG, "startFirstConfig，installShortcuts result :" + GsonUtils.toJson(responseResult));
                        if (!uploadProgress(traceId, responseResult, rpcCallback)) {
                            endConfigProcess(traceId);
                            return;
                        }
                    } else {
                        LogUtils.d(TAG, "startFirstConfig，installShortcuts is already installed");
                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，installShortcuts is already installed");
                    }

                    //移动快捷指令到文件夹
//                    if (!progressManager.isMoveShortcut()) {//如果没有移动快捷指令到文件夹
//                        LogUtils.d(TAG, "startFirstConfig，moveShortcut start");
//                        ResponseResult responseResult = moveShortcutFolder(shortcuts, shortcutFolderName, traceId);//移动快捷指令到文件夹
//                        progressManager.setMoveShortcut(responseResult.getCode() == RESPONSE_CODE_SUCCESS);//设置移动快捷指令到文件夹结果
//                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));//设置移动快捷指令到文件夹结果
//                        LogUtils.d(TAG, "startFirstConfig，moveShortcut result :" + GsonUtils.toJson(responseResult));
//                        LogUtils.d(TAG, "startFirstConfig，moveShortcut upload result：", GsonUtils.toJson(responseResult));
//                        MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, responseResult);//不管成功失败不结束，继续执行
////                        if (!upLoadProgress(traceId,responseResult, rpcCallback)) {
////                            endConfig(traceId);
////                            return;
////                        }
//                    } else {
//                        LogUtils.d(TAG, "startFirstConfig，moveShortcut is already moved");
//                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，moveShortcut is already moved");
//                    }

                    //设置快捷指令快捷键（执行快捷指令组合键）
                    List<Shortcut> needSetKeysShortcuts = getNeedSetKeysICloudLinks(progressManagerToServer.getShortcuts());//获取需要设置快捷键的快捷指令
                    if (!needSetKeysShortcuts.isEmpty()) {//如果有需要设置快捷键的快捷指令
                        LogUtils.d(TAG, "startFirstConfig，setKeysShortcuts start list", GsonUtils.toJson(needSetKeysShortcuts));
                        Thread.sleep(SLEEP_TIME_2000);
                        ResponseResult responseResult = setAndValidateShortcutKey(needSetKeysShortcuts, progressManagerToServer, traceId);//设置快捷指令快捷键并验证
                        responseResult.setData(GsonUtils.toJson(progressManagerToServer));//设置快捷指令快捷键并验证结果
                        LogUtils.d(TAG, "startFirstConfig，setKeysShortcuts result :" + GsonUtils.toJson(responseResult));
                        MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, responseResult);//不管成功失败不结束，继续执行
                        if (!uploadProgress(traceId, responseResult, rpcCallback)) {
                            endConfigProcess(traceId);
                            return;
                        }
                    } else {
                        LogUtils.d(TAG, "startFirstConfig，setKeysShortcuts is already set");
                        LogServiceManager.getInstance().log(traceId, "startFirstConfig，setKeysShortcuts is already set");
                    }

                    endConfigProcess(traceId); //结束配置流程
                    LogUtils.d(TAG, "startFirstConfig，end:" + GsonUtils.toJson(progressManagerToServer));
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_SUCCESS, "startFirstConfig end", GsonUtils.toJson(progressManagerToServer)));
                    LogServiceManager.getInstance().log(traceId, "startFirstConfig，end:" + GsonUtils.toJson(progressManagerToServer));
                } else {
                    LogUtils.e(TAG, "startFirstConfig，bluetooth connect failed");
                    LogServiceManager.getInstance().log(traceId, "startFirstConfig，bluetooth connect failed");
                    MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL, "蓝牙连接失败", GsonUtils.toJson(progressManagerToServer)));
                }
            } catch (Exception e) {
                LogUtils.e(TAG, "startFirstConfig，exception ：" + e.getMessage());
                MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, new ResponseResult(RESPONSE_CODE_EXCEPTION_ERROR, "startFirstConfig执行异常 ：" + e.getMessage()));
                LogServiceManager.getInstance().log(traceId, "startFirstConfig，exception ：" + e.getMessage());
            }
        });
    }


    /**
     * 上报节点（根据结果判断是否结束）
     */
    private static boolean uploadProgress(String traceId, ResponseResult result, RpcServiceHelper.RpcCallback rpcCallback) {
        String json = GsonUtils.toJson(result);
        if (result.getCode() != RESPONSE_CODE_SUCCESS) {
            LogUtils.e("startFirstConfig，report end ,upload fail node：", json);
            MqttConnectManager.getInstance().sendAsyncMessageToMqttResultEnd(rpcCallback, result);
            LogServiceManager.getInstance().log(traceId, "startFirstConfig，report end ,upload fail node：" + json);
            return false;
        } else {
            LogUtils.d("startFirstConfig，report upload success node：", json);
            MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, result);
            LogServiceManager.getInstance().log(traceId, "startFirstConfig，report end ,upload fail node：" + json);
            return true;
        }
    }

    /**
     * 上报节点(不退出)
     */
    private static void uploadProgressAndContinue(String traceId, ResponseResult result, RpcServiceHelper.RpcCallback rpcCallback) {
        String json = GsonUtils.toJson(result);
        if (result.getCode() != RESPONSE_CODE_SUCCESS) {
            LogUtils.e("startFirstConfig，report end ,upload fail node：", json);
        } else {
            LogUtils.d("startFirstConfig，report upload success node：", json);
        }
        LogServiceManager.getInstance().log(traceId, "startFirstConfig，report end ,upload fail node：" + json);
        MqttConnectManager.getInstance().sendAsyncMessageToMqttResultPart(rpcCallback, result);
    }

    /**
     * 获取需要设置快捷键的 ICloudLink链接集合
     */
    private static List<Shortcut> getNeedSetKeysICloudLinks(List<Shortcut> shortcuts) {
        List<Shortcut> needSetKeysShortcuts = new ArrayList<>();
        if (shortcuts != null && !shortcuts.isEmpty()) {
            for (int i = 0; i < shortcuts.size(); i++) {
                Shortcut shortcut = shortcuts.get(i);
                if (shortcut.isNeedSetKeys() && !shortcut.isSetKeyed() && shortcut.isInstalled()) {//需要设置但是还没有设置
                    needSetKeysShortcuts.add(shortcut);
                }
            }
        }
        return needSetKeysShortcuts;
    }


    /**
     * 获取需要安装的 ICloudLink链接集合
     */
    private static List<Shortcut> getNeedInstallICloudLinks(List<Shortcut> shortcuts) {
        List<Shortcut> needInstallShortcuts = new ArrayList<>();
        if (shortcuts != null && !shortcuts.isEmpty()) {
            for (int i = 0; i < shortcuts.size(); i++) {
                Shortcut shortcut = shortcuts.get(i);
                if (!shortcut.isInstalled()) {
                    needInstallShortcuts.add(shortcut);
                }
            }
        }
        return needInstallShortcuts;
    }

    /**
     * 获取需要设置快捷指令id的快捷指令集合
     */
    private static List<Shortcut> getNeedSetShortcutIdShortcuts(List<Shortcut> shortcuts) {
        List<Shortcut> needSetShortcutIdShortcuts = new ArrayList<>();
        if (shortcuts != null && !shortcuts.isEmpty()) {
            for (int i = 0; i < shortcuts.size(); i++) {
                Shortcut shortcut = shortcuts.get(i);
                if (shortcut.isNeedSetShortcutId() && !shortcut.isSetShortcutId()) {//需要设置快捷指令id并且没有设置快捷指令id
                    needSetShortcutIdShortcuts.add(shortcut);
                }
            }
        }
        return needSetShortcutIdShortcuts;
    }


    /**
     * 打开辅助触控功能
     */
    private static ResponseResult openAssistiveTouch(String traceId) throws InterruptedException {
        return FirstAutomationOtherConfigRpaManager.openAssistiveTouch(traceId);
    }

    /**
     * 打开全键盘控制开关、设置全键盘配置
     * - 「自动隐藏」- 决定了显示在此处的蓝框（打开）
     * - 多久消失（秒，默认 15 秒；最少 1 秒）：调整为1秒
     */
    public static ResponseResult openFullKeyboardControl(String traceId, int num) throws InterruptedException {
        return FirstAutomationOtherConfigRpaManager.openFullKeyboardControl(traceId, num);
    }

    /**
     * 设置辅助触控配置
     */
    public static ResponseResult setAssistiveTouchConfig(String traceId) throws InterruptedException {
        return FirstAutomationOtherConfigRpaManager.setAssistiveTouchConfig(traceId);
    }

    /**
     * 创建快捷指令文件夹
     */
    public static ResponseResult createShortcutFolder(String folderName, String traceId) throws InterruptedException {
        return ShortcutManager.createShortcutFolder(folderName, traceId);
    }

    /**
     * 安装快捷指令列表
     */
    public static ResponseResult installShortcutList(List<Shortcut> shortcuts, AutomationShortcutProgress progressManager, String traceId, String uniqueId) throws InterruptedException {
        boolean success;
        ResponseResult responseResult = new ResponseResult();
        for (int i = 0; i < shortcuts.size(); i++) {
            Thread.sleep(SLEEP_TIME_1000);
            LogUtils.d(TAG, "installShortcutList，installShortcut start：" + shortcuts.get(i).getShortcutName() + "  i = " + i, shortcuts.get(i));
            responseResult = ShortcutManager.installShortcut(shortcuts.get(i), traceId, uniqueId);
            progressManager.getShortcuts().get(i).setInstalled(responseResult.getCode() == RESPONSE_CODE_SUCCESS);//设置结果
//            progressManager.getShortcuts().get(i).setInstalled(true);//设置结果
        }
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "installShortcutList end");
//        return new ResponseResult(success ? RESPONSE_CODE_SUCCESS : RESPONSE_CODE_FAIL, success ? "installShortcutList end" : responseResult.getMessage()); //全部成功，才叫成功
    }

    /**
     * 设置快捷指令快捷键并验证
     */
    public static ResponseResult setAndValidateShortcutKey(List<Shortcut> shortcuts, AutomationShortcutProgress progressManager, String traceId) throws InterruptedException {
//        ResponseResult result = ShortcutManager.enterSettingCommandPage(traceId);//进入设置命令页面
//        if (result.getCode() != RESPONSE_CODE_SUCCESS) {
//            return new ResponseResult(RESPONSE_CODE_FAIL, "enterSettingCommandPage fail");
//        }
//        return new ResponseResult(RESPONSE_CODE_SUCCESS, "enterSettingCommandPage fail");
        return ShortcutManager.setShortcutKey(shortcuts, progressManager, traceId);

//        progressManager.getShortcuts().get(0).setSetKeyed(true);
//        progressManager.getShortcuts().get(0).setVerified(true);
//        return new ResponseResult(RESPONSE_CODE_SUCCESS, "setAndValidateShortcutKey end");
    }

    /**
     * 移动快捷指令到文件夹
     */
    private static ResponseResult moveShortcutFolder(List<Shortcut> shortcuts, String shortcutFolderName, String traceId) throws InterruptedException {
        return ShortcutManager.moveShortcutFolder(shortcuts, shortcutFolderName, traceId);
    }

//    /**
//     * 快捷指令设置id
//     */
//    private static ResponseResult setShortcutId(List<Shortcut> shortcuts, String devicesId, AutomationShortcutProgress progressManager, String traceId) throws InterruptedException {
//        return ShortcutManager.setShortcutId(null, traceId, uniqueId);
//    }

//    /**
//     * Imessage消息自动化配置、可能多个
//     */
//    public static ResponseResult configureIMessageAutomation(AutomationShortcutConfigBean.ProgressManager progressManager, IMessageAutomationConfig iMessageAutomationConfig, String traceId) throws InterruptedException {
//        return IMessageAutomationManager.installIMessageAutomation(iMessageAutomationConfig, traceId);
//    }

    /**
     * 结束配置
     */
    private static void endConfigProcess(String traceId) throws InterruptedException {
//        ShortcutUtil.returnToOneActionWebApp(isWeb, webAppLink, traceId);//回到oneAction/webApp
        VoiceInjectionManager.closeNarration(traceId);//关闭旁白
        ExtKeyboardMouseManager.getInstance().disconnect();//关闭蓝牙
    }

}
