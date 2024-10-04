package ai.matter.agentruntime.manager;

import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_BLUETOOTH_DEVICES;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_ERROR;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_HAND;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_INSTALL_SHORTCUT_ADD_SHORTCUT_BUTTON;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_INSTALL_SHORTCUT_BROWSER_OPEN_SHORTCUT_BUTTON;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_INSTALL_SHORTCUT_CLOSE_SHORTCUTS_BUTTON;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_INSTALL_SHORTCUT_VERIFICATION;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_JARVIS;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_SET_SHORTCUT_ID_TEXT_FIELD;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_SHORTCUTS_HEADING;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_10000;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_3000;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_5000;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_BLE_CONNECT_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_2000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_3000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_500;

import android.text.TextUtils;

import java.util.List;

import ai.matter.agentruntime.bean.ExecuteShortcutBean;
import ai.matter.agentruntime.bean.GroupKeyBean;
import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.bean.data.AutomationShortcutProgress;
import ai.matter.agentruntime.bean.data.Shortcut;
import ai.matter.agentruntime.bean.location.MouseLocations;
import ai.matter.agentruntime.enums.IPhoneModelEnum;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.ShortcutUtil;
import ai.matter.agentruntime.utils.Utils;
import ai.matter.extbt.profile.Constants;

/**
 * 快捷指令管理
 */
public class ShortcutManager {
    private static final String TAG = "Shortcut ShortcutManager ";

    private static boolean isFirstOpenShortcut = true;//是否第一次打开快捷指令
    private static boolean isCloseNetworkAlert = false;//是否关闭过网络弹窗

    /**
     * 安装快捷指令
     */
    public static ResponseResult installShortcut(Shortcut shortcut, String traceId, String uniqueId) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut，mouseLocations is null"));
        }
        String iCloudLink = shortcut.getICloudLink();
        if (TextUtils.isEmpty(iCloudLink)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut fail，iCloudLink is null"));
        }
        LogUtils.d(TAG, "installShortcut，open openSearchPage", shortcut);

        ShortcutUtil.userSearchOpenAppByNameExt(iCloudLink, traceId);
//        Thread.sleep(SLEEP_TIME_1000);
//        ExtKeyboardMouseManager.getInstance().pressEnterKey();

        Thread.sleep(SLEEP_TIME_2000);

        //首次进入检测，关闭首次进入快捷指令弹窗
        if (isFirstOpenShortcut) {
            LogUtils.d(TAG, "installShortcut，first open shortcut first,find close Shortcuts button", shortcut);
            //开始验证是否首测进入到快捷指令页面
            if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout("", TIME_3000, () -> {
//                        ExtKeyboardMouseManager.getInstance().pressDownKey();
                ShortcutUtil.pressDownKeyExt(4);//移动到 close Shortcuts button
            }, KEYWORDS_INSTALL_SHORTCUT_CLOSE_SHORTCUTS_BUTTON)) {
                LogUtils.d(TAG, "installShortcut，find close Shortcuts button");
                ExtKeyboardMouseManager.getInstance().pressUpKey();//accept & continue
                Thread.sleep(SLEEP_TIME_500);
                LogUtils.d(TAG, "installShortcut，find accept & continue button，click");
                ExtKeyboardMouseManager.getInstance().onSpaceKey();//click accept & continue
            }
            //只检测一次，不再检测
            isFirstOpenShortcut = false;
        }

        //网络请求弹窗检测，并允许
        LogUtils.d(TAG, "installShortcut，find close network alert and allow isCloseNetworkAlert：" + isCloseNetworkAlert, shortcut);
        if (!isCloseNetworkAlert) {
            if (ShortcutUtil.closeNetworkAlertAndAllowExt()) {
                LogUtils.d(TAG, "installShortcut，close network alert and allow success，reset install", shortcut);
                installShortcut(shortcut, traceId, uniqueId);
            }
            isCloseNetworkAlert = true;
        }

        LogUtils.d(TAG, "installShortcut，wait install loading is complete", shortcut);
        Thread.sleep(shortcut.getInstallLoadWaitTime());//等待加载完成

        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//切换到cancel
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//切换到cancel
        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "installShortcut，find add shortcut button", shortcut);
        if (!VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_3000, () -> {
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//验证添加快捷指令按钮 ctrl + down 移动到最下面，添加快捷指令按钮
        }, KEYWORDS_INSTALL_SHORTCUT_ADD_SHORTCUT_BUTTON)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut fail，find add shortcut button fail"));
        }
        Thread.sleep(SLEEP_TIME_2000);
        LogUtils.d(TAG, "installShortcut，click add shortcut button", shortcut);
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {//Add short alert. You already have a shortcut named ,验证是否有重复弹窗
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击添加快捷指令按钮
        }, KEYWORDS_INSTALL_SHORTCUT_VERIFICATION)) {
            //有重复的，要处理替换
            LogUtils.d(TAG, "installShortcut，Add short alert. You already have a shortcut", shortcut);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//切换到replace
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installShortcut，find replace button success,click", shortcut);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击替换按钮
        }

        boolean isNeedSetShortcutId = shortcut.isNeedSetShortcutId() && !shortcut.isSetShortcutId();//是否需要设置id并且没有设置过
        LogUtils.d(TAG, "installShortcut，isNeedSetShortcutId:" + isNeedSetShortcutId, shortcut);
        if (isNeedSetShortcutId) {
            Thread.sleep(SLEEP_TIME_3000);
            ResponseResult responseResult = setShortcutId(shortcut, traceId, uniqueId);
            if (responseResult.getCode() == RESPONSE_CODE_FAIL) {
                return endConfig(responseResult);
            }
        }
        return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "installShortcut success"));
    }

    /**
     * 安装快捷指令
     */
    public static ResponseResult installShortcut1(Shortcut shortcut, String traceId, String uniqueId) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut，mouseLocations is null"));
        }
        String iCloudLink = shortcut.getICloudLink();
        if (TextUtils.isEmpty(iCloudLink)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut fail，iCloudLink is null"));
        }
        LogUtils.d(TAG, "installShortcut，open browser:" + shortcut.getShortcutName());

        if (ShortcutUtil.openBrowserExt(iCloudLink, traceId).getCode() != RESPONSE_CODE_SUCCESS) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut fail，open browser fail"));
        }
        LogUtils.d(TAG, "installShortcut，open browser success，wait url loading is complete");
        Thread.sleep(shortcut.getWebpageResponseWaitTime());//等待加载完成
        LogUtils.d(TAG, "installShortcut，find open button");
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_10000, () -> {
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);
//            ShortcutUtil.moveToTapExt(mouseLocations.browserOpenShortcutButtonLocation);//移动到打开快捷指令按钮位置
        }, KEYWORDS_INSTALL_SHORTCUT_BROWSER_OPEN_SHORTCUT_BUTTON)) {
            LogUtils.d(TAG, "installShortcut，click open button");
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//打开
            Thread.sleep(100);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//打开 有时候一次不生效，点击两次
        } else {
            LogUtils.d(TAG, "installShortcut，find open shortcuts button fail");
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut fail，find open shortcuts button fail"));
        }

        LogUtils.d(TAG, "installShortcut，click open button success，wait shortcut loading is complete");
        Thread.sleep(shortcut.getInstallLoadWaitTime());//等待加载完成
        LogUtils.d(TAG, "installShortcut，shortcut loading is complete，ctrl+down，start find add shortcut button");

        //移动鼠标到左上角取消位置（焦点切换到取消位置，防止焦点乱跑到状态栏）
//        ShortcutUtil.moveToTapExt(mouseLocations.shortcutsAddShortcutCancelButtonLocation);///移动鼠标到左上角取消位置
        ExtKeyboardMouseManager.getInstance().onTabKey();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onTabKey();
        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "installShortcut，find add shortcut button");
        if (!VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//验证添加快捷指令按钮 ctrl + down 移动到最下面，添加快捷指令按钮
        }, KEYWORDS_INSTALL_SHORTCUT_ADD_SHORTCUT_BUTTON)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut fail，find add shortcut button fail"));
        }
        Thread.sleep(SLEEP_TIME_2000);
        LogUtils.d(TAG, "installShortcut，click add shortcut button");
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_10000, () -> {//Add short alert. You already have a shortcut named ,验证是否有重复弹窗
            //有时候移动鼠标，焦点过不来，焦点会在返回按钮那，需要键点往下再切焦点。只用键盘切换焦点，有时候焦点在状态栏时候，就切不过来，需要两个配合使用
//            ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAddShortcutButtonLocation);
//            Thread.sleep(SLEEP_TIME_500);
//            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击添加快捷指令按钮
        }, KEYWORDS_INSTALL_SHORTCUT_VERIFICATION)) {
            //有重复的，要处理替换
            LogUtils.d(TAG, "installShortcut，Add short alert. You already have a shortcut", shortcut);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//切换到replace
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installShortcut，find replace button success,click", shortcut);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击替换按钮
        }

        boolean isNeedSetShortcutId = shortcut.isNeedSetShortcutId() && !shortcut.isSetShortcutId();//是否需要设置id并且没有设置过
        LogUtils.d(TAG, "installShortcut，isNeedSetShortcutId:" + isNeedSetShortcutId);
        if (isNeedSetShortcutId) {
            ResponseResult responseResult = setShortcutId(shortcut, traceId, uniqueId);
            if (responseResult.getCode() == RESPONSE_CODE_FAIL) {
                return endConfig(responseResult);
            }
        }
        return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "installShortcut success"));
    }


    /**
     * 进入到设置命令页面
     */
    public static ResponseResult enterSettingCommandPage(String traceId) throws InterruptedException {

        return null;
    }


    /**
     * 设置快捷指令快捷键
     */
    public static ResponseResult setShortcutKey(List<Shortcut> shortcuts, AutomationShortcutProgress progressManager, String traceId) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installShortcut，mouseLocations is null"));
        }
//      VoiceInjectionManager.openAssistiveTouchSettings(traceId);
        ShortcutUtil.openAssistiveTouchSettingsExt(false, traceId);
//        ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_ALWAYS_SHOW_MENU, traceId);
        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "setShortcutKey，call back assistive touch settings page top");
        ShortcutUtil.scrollPageTopExt();
        Thread.sleep(SLEEP_TIME_1000);

        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);
        Thread.sleep(SLEEP_TIME_500);

        ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到辅助触控开关位置
        Thread.sleep(SLEEP_TIME_500);

        String phoneModel = mouseLocations.phoneModel;
        if (IPhoneModelEnum.IPHONE_MODEL_16.getModelName().equals(phoneModel) ||
                IPhoneModelEnum.IPHONE_MODEL_16_PLUS.getModelName().equals(phoneModel) ||
                IPhoneModelEnum.IPHONE_MODEL_16_PRO.getModelName().equals(phoneModel) ||
                IPhoneModelEnum.IPHONE_MODEL_16_PRO_MAX.getModelName().equals(phoneModel)
        ) {
            ShortcutUtil.pressRightKeyExt(14);
        } else {
            //iphone 15 pro
            ShortcutUtil.pressRightKeyExt(13);
        }
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击devices

        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角
        Thread.sleep(SLEEP_TIME_500);
//        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角
//        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "setShortcutKey，start find ble jarvis");
        String voiceTextJarvis = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_5000, () -> {
            ExtKeyboardMouseManager.getInstance().pressDownKey();//找ble jarvis
//            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击devices 切到设备页面，ble位置验证
        });
        if (!TextUtils.isEmpty(voiceTextJarvis)) {
            LogUtils.d(TAG, "setShortcutKey，find ble jarvis voiceText:" + voiceTextJarvis);
            if (voiceTextJarvis.contains(KEYWORDS_JARVIS) && !voiceTextJarvis.contains(KEYWORDS_HAND)) {
                LogUtils.d(TAG, "setShortcutKey，find ble jarvis success，click open");
                ExtKeyboardMouseManager.getInstance().onSpaceKey();
            } else if (voiceTextJarvis.contains(KEYWORDS_ERROR)) {
                return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutKey fail，find ble jarvis error"));
            } else {//当前这个不是ble，往下再找5个
                for (int i = 0; i < 5; i++) {
                    voiceTextJarvis = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_5000, () -> {
                        ExtKeyboardMouseManager.getInstance().pressRightKey();//切换下一个进行验证
                    });
                    LogUtils.d(TAG, "setShortcutKey，find ble jarvis voiceText:" + voiceTextJarvis);
                    if (voiceTextJarvis.contains(KEYWORDS_JARVIS) && !voiceTextJarvis.contains(KEYWORDS_HAND)) {
                        LogUtils.d(TAG, "setShortcutKey，find ble jarvis success，click open");
                        ExtKeyboardMouseManager.getInstance().onSpaceKey();
                    } else if (voiceTextJarvis.contains(KEYWORDS_BLUETOOTH_DEVICES)) {
                        return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutKey fail，find ble jarvis end"));
                    } else if (voiceTextJarvis.contains(KEYWORDS_ERROR)) {
                        return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutKey fail，find ble jarvis error"));
                    }
                }
            }
        } else {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutKey fail，find ble jarvis fail"));
        }

        LogUtils.d(TAG, "setShortcutKey，start set keys button");
        List<Shortcut> shortcuts1 = progressManager.getShortcuts();

        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressDownKey();


        for (int i = 0; i < shortcuts.size(); i++) {
            Shortcut shortcut = shortcuts.get(i);
            LogUtils.d(TAG, "setShortcutKey，click customize additional buttons,start config", shortcut);
            Thread.sleep(SLEEP_TIME_2000);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到customize additional
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//customize additional
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "setShortcutKey，start find keys button，sendBleKey", shortcut);

            if (Utils.getBlecConnectStatus()) {
                if (!Utils.sendBleKey(shortcut.getShortcutKey())) {//ble 按下快捷键，再进入找对应快捷指令
                    ExtKeyboardMouseManager.getInstance().onEscKey();//ble执行失败，返回，开始找下一个
                    LogUtils.e(TAG, "setShortcutKey，sendBleKey fail", shortcut);
                    continue;
                }
            } else {
                return endConfig(new ResponseResult(RESPONSE_CODE_BLE_CONNECT_FAIL, "setShortcutKey，ble is not connect"));
            }

            Thread.sleep(SLEEP_TIME_3000);
            LogUtils.d(TAG, "setShortcutKey，start voice aside verification", shortcut);
            String voiceTextFirst = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_5000, () -> {
                ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.LEFT_CTRL, Constants.DOWN);//切到最下面
            });
            if (!TextUtils.isEmpty(voiceTextFirst)) {
                LogUtils.d(TAG, "setShortcutKey，find shortcut name voiceTextFirst:" + voiceTextFirst, shortcut);
                if (voiceTextFirst.contains(shortcut.getShortcutName())) {
                    LogUtils.d(TAG, "setShortcutKey，find shortcut name success and click", shortcut);
                    ExtKeyboardMouseManager.getInstance().onSpaceKey();//选中
                    Thread.sleep(SLEEP_TIME_500);
                    ExtKeyboardMouseManager.getInstance().onSpaceKey();//有时候一次会失效
                    setShortcutsKeyResult(shortcuts1, shortcut);//设置结果
                    Thread.sleep(SLEEP_TIME_500);
                    ExtKeyboardMouseManager.getInstance().onEscKey();//返回，开始找下一个
                    continue;
                } else if (voiceTextFirst.contains(KEYWORDS_ERROR)) {
                    return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutKey，getVoiceText error"));
                }
            }

            int times = 1;//查找次数
            while (true) {
                if (times == 20) {//找20次，找不到就退出
                    break;
                }
                Thread.sleep(SLEEP_TIME_3000);
                LogUtils.d(TAG, "setShortcutKey，start voice aside verification ,start times:" + times, shortcut);
                times++;

                String voiceText = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_5000, () -> {
                    ExtKeyboardMouseManager.getInstance().pressLeftKey();
                });
                //Ghost under score master under score service button.
                if (!TextUtils.isEmpty(voiceText)) {
                    voiceText = voiceText.replace("under ", "");//替换下划线
                    LogUtils.d(TAG, "setShortcutKey，find shortcut name voiceText:" + voiceText, shortcut);
                    if (voiceText.contains(shortcut.getShortcutName())) {
                        LogUtils.d(TAG, "setShortcutKey，find shortcut name success and click", shortcut);
                        ExtKeyboardMouseManager.getInstance().onSpaceKey();//选中
                        Thread.sleep(SLEEP_TIME_500);
                        ExtKeyboardMouseManager.getInstance().onSpaceKey();//有时候一次会失效
                        setShortcutsKeyResult(shortcuts1, shortcut);//设置结果
                        Thread.sleep(SLEEP_TIME_500);
                        ExtKeyboardMouseManager.getInstance().onEscKey();//返回，开始找下一个
                        break;
                    } else if (voiceText.contains(KEYWORDS_SHORTCUTS_HEADING)) {//到了结束标志
                        LogUtils.e(TAG, "setShortcutKey，find shortcut name fail end Shortcuts, Heading", shortcut);
                        ExtKeyboardMouseManager.getInstance().onEscKey();//返回，开始找下一个
                        break;
                    } else if (voiceText.contains(KEYWORDS_ERROR)) {
                        return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutKey，voiceText error"));
                    }
                }
            }
        }
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "setAndValidateShortcutKey end");
    }


    /**
     * 设置快捷键结果
     */
    private static void setShortcutsKeyResult(List<Shortcut> shortcuts, Shortcut shortcut) {
        for (int i = 0; i < shortcuts.size(); i++) {
            if (shortcuts.get(i).getShortcutName().equals(shortcut.getShortcutName())) {
                shortcuts.get(i).setKeyed(true);
                break;
            }
        }
    }


    /**
     * 执行快捷指令
     */
    public static ResponseResult executeShortcut(String shortcutKey) {
        LogUtils.d(TAG, "setShortcutKey，start find keys button，sendBleKey", shortcutKey);
        if (Utils.getBlecConnectStatus()) {
            if (!Utils.sendBleKey(shortcutKey)) {
                return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "sendBleKey，num is invalid"));
            } else {
                return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "executeShortcut success"));
            }
        } else {
            return endConfig(new ResponseResult(RESPONSE_CODE_BLE_CONNECT_FAIL, "setShortcutKey，ble is not connect"));
        }
    }

    /**
     * 执行快捷指令
     */
    public static ResponseResult executeShortcut(ExecuteShortcutBean executeShortcutBean, String traceId) throws InterruptedException {
        LogUtils.d(TAG, "executeShortcut，start executeShortcut");
        String shortcutKey = executeShortcutBean.getExecuteShortcutKey();//执行快捷指令的key
        String openJarvisOneKey = executeShortcutBean.getOpenJarvisOneKey();//打开jarvis one key
        int clickAllowNum = executeShortcutBean.getClickAllowNum();//点击允许的次数（0，不需要验证）
        if (Utils.getBlecConnectStatus() && !TextUtils.isEmpty(shortcutKey)) {
            if (clickAllowNum == 0) {//不需要点击弹窗
                if (!Utils.sendBleKey(shortcutKey)) {
                    return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "sendBleKey，shortcutKey num is invalid"));
                } else {
                    return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "executeShortcut success"));
                }
            } else {//要点击弹窗
                if (!TextUtils.isEmpty(openJarvisOneKey)) {//打开jarvis one
                    if (Utils.sendBleKey(openJarvisOneKey)) {
                        LogUtils.d(TAG, "executeShortcut，openJarvisOneKey success");//打开成功等带1秒
                        Thread.sleep(SLEEP_TIME_2000);
                        clickAllowButton(executeShortcutBean, 1, traceId, false);//执行完快捷指令再去点击点击允许按钮
                    } else {
                        LogUtils.e(TAG, "endBleKey，openJarvisOneKey num is invalid");
                    }
                } else {
                    LogUtils.e(TAG, "executeShortcut，openJarvisOneKey is null");
                }

                if (!TextUtils.isEmpty(shortcutKey)) {//要点弹窗（第一次收到iMessage时候会有弹窗，但是不需要执行快捷键）
                    if (!Utils.sendBleKey(shortcutKey)) {
                        return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "sendBleKey，num is invalid"));
                    } else {
                        Thread.sleep(SLEEP_TIME_2000);
                        return clickAllowButton(executeShortcutBean, clickAllowNum, traceId, true);//执行完快捷指令再去点击点击允许按钮
                    }
                }
                return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "executeShortcut success"));
            }
        } else {
            return endConfig(new ResponseResult(RESPONSE_CODE_BLE_CONNECT_FAIL, "setShortcutKey，ble is not connect"));
        }
    }


    /**
     * 点击允许按钮
     */
    private static ResponseResult clickAllowButton(ExecuteShortcutBean executeShortcutBean, int clickAllowNum, String traceId, boolean isDisconnect) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "executeShortcut，mouseLocations is null"));
        }
        boolean connect = ExtKeyboardMouseManager.getInstance().connect();
        if (connect) {
            for (int i = 0; i < clickAllowNum; i++) {
                Thread.sleep(executeShortcutBean.getClickAllowInterval());
                LogUtils.d(TAG, "executeShortcut，click allow button times:" + (i + 1));
                ShortcutUtil.moveToTapAndClickExt(mouseLocations.allowButtonLocation);
            }
            if (isDisconnect) {
                ExtKeyboardMouseManager.getInstance().disconnect();
            }
        } else {
            return endConfig(new ResponseResult(RESPONSE_CODE_EXT_BLUETOOTH_CONNECT_FAIL, "executeShortcut fail，ext bluetooth connect fail"));
        }
        return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "executeShortcut success"));
    }


    /**
     * 执行快捷指令(外挂蓝牙)
     */
    public static ResponseResult executeShortcut(GroupKeyBean groupKeyBean) {
        ShortcutUtil.sendGroupKeyExt(groupKeyBean);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "执行快捷指令成功");
    }

    /**
     * 删除快捷指令
     */
    public static ResponseResult deleteShortcut(String shortcutId) throws InterruptedException {
        // TODO: 实现删除快捷指令的逻辑
        return null;
    }

    /**
     * 更新快捷指令
     */
    public static ResponseResult updateShortcut(String name) throws InterruptedException {
        // TODO: 实现更新快捷指令的逻辑
        return null;
    }

    /**
     * 更新快捷指令Id
     */
    public static ResponseResult updateShortcutId(String name) throws InterruptedException {
        // TODO: 实现更新快捷指令的逻辑
        return null;
    }

    /**
     * 创建快捷指令文件夹
     */
    public static ResponseResult createShortcutFolder(String folderName, String traceId) throws InterruptedException {
        // TODO: 实现创建快捷指令文件夹的逻辑
        return null;
    }

    private static ResponseResult endConfig(ResponseResult result) {
        if (result.getCode() == RESPONSE_CODE_SUCCESS) {
            LogUtils.d(TAG, result.getMessage());
        } else {
            LogUtils.e(TAG, result.getMessage());
        }
//        ExtKeyboardMouseManager.getInstance().disconnect();
        return result;
    }

    /**
     * 快捷指令归类（移动快捷指令到文件夹）
     */
    public static ResponseResult moveShortcutFolder(List<Shortcut> shortcuts, String shortcutFolderName, String traceId) throws InterruptedException {
        // TODO: 实现快捷指令归类的逻辑
        return null;
    }

    public static ResponseResult setShortcutId(Shortcut shortcut, String traceId, String uniqueId) throws InterruptedException {
        LogUtils.d(TAG, "setShortcutId，start setShortcutId", shortcut);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//切到最上面
        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "setShortcutId，find search field and click", shortcut);
        ExtKeyboardMouseManager.getInstance().pressDownKey();//找输入框
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();
        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "setShortcutId，input shortcut name", shortcut);
        ExtKeyboardMouseManager.getInstance().inputString(shortcut.getShortcutName());
        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();
        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();
        Thread.sleep(SLEEP_TIME_1000);

        LogUtils.d(TAG, "setShortcutId，search shortcut name", shortcut);
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_10000, () -> {
            ExtKeyboardMouseManager.getInstance().pressDownKey();//旁白验证是否搜索到了 thin red line push field access and button access have a double
        }, KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON)) { // KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON  KEYWORDS_SET_SHORTCUT_ID_SEARCH_RESULT_ONE_BUTTON
            LogUtils.d(TAG, "setShortcutId，search shortcut name success,edit shortcut", shortcut);
            ExtKeyboardMouseManager.getInstance().rightClick();//右键进入快捷指令里面'
        } else {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutId fail，search shortcut name fail"));
        }


        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "setShortcutId，find edit button and click", shortcut);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//进入编辑页面
        Thread.sleep(SLEEP_TIME_1000);

        ShortcutUtil.pressDownKeyExt(1);

        int count = 20;
        boolean verifyResult;
        do {
            verifyResult = VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000,
                    () -> ExtKeyboardMouseManager.getInstance().pressDownKey(),//text text field double tap to edit use the rotary
                    KEYWORDS_SET_SHORTCUT_ID_TEXT_FIELD);
            Thread.sleep(SLEEP_TIME_1000);
            if (verifyResult) {
                LogUtils.d(TAG, "setShortcutId，find text field success", shortcut);
                break;
            }
            LogUtils.e(TAG, "setShortcutId，find text field fail", shortcut);
        } while (count-- > 0);


        if (verifyResult) {
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().allSelect();
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onBackSpaceKey();
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "setShortcutId，input uniqueId", shortcut);
            ExtKeyboardMouseManager.getInstance().inputString(uniqueId);
            Thread.sleep(3500);

            LogUtils.d(TAG, "setShortcutId，find done button", shortcut);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);
            Thread.sleep(SLEEP_TIME_1000);
            ShortcutUtil.pressRightKeyExt(2);
            LogUtils.d(TAG, "setShortcutId，find done button success，click", shortcut);
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();
            return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "setShortcutId success"));
        } else {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "setShortcutId fail，find text field fail"));
        }
    }
}
