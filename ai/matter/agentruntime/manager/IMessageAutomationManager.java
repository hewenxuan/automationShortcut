package ai.matter.agentruntime.manager;

import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_IMESSAGE_CONFIG_AUTO_FIND_EDIT_SEARCH_ITEM_MESSAGE;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_15000;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_3000;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_5000;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.MESSAGE;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_2000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_3000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_500;

import android.text.TextUtils;

import ai.matter.agentruntime.bean.GroupKeyBean;
import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.bean.data.IMessageAutomationConfig;
import ai.matter.agentruntime.bean.data.IMessageAutomationUpdateContact;
import ai.matter.agentruntime.bean.location.MouseLocations;
import ai.matter.agentruntime.enums.IPhoneModelEnum;
import ai.matter.agentruntime.utils.GsonUtils;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.ShortcutUtil;
import ai.matter.extbt.profile.Constants;

/**
 * IMessage自动化消息管理
 */
public class IMessageAutomationManager {
    private static final String TAG = "Shortcut IMessageAutomationManager ";

    /**
     * 安装IMessage自动化
     */
    public static ResponseResult installIMessageAutomation1(IMessageAutomationConfig iMessageAutomationConfig, String traceId) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，mouseLocations is null", GsonUtils.toJson(iMessageAutomationConfig)));
        }
        LogUtils.d(TAG, "installIMessageAutomation，turnOnNarration open，start open shortcuts app");
        GroupKeyBean groupKeys = iMessageAutomationConfig.getGroupKeys();
        if (groupKeys == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，groupKeys is null", GsonUtils.toJson(iMessageAutomationConfig)));
        }
        int configureIMessageAutomationSuccessNums = iMessageAutomationConfig.getConfigureIMessageAutomationSuccessNums();
        int executeGetContactsNum = iMessageAutomationConfig.getExecuteGetContactsNum();
        if (configureIMessageAutomationSuccessNums >= executeGetContactsNum) {
            return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "installIMessageAutomation，all success", GsonUtils.toJson(iMessageAutomationConfig)));
        }
//        ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_SHORTCUTS, traceId);//打开快捷指令app
        ShortcutUtil.openShortcutExt(false, traceId);//打开快捷指令app
        LogUtils.d(TAG, "installIMessageAutomation，shortcuts app is open，start call back shortcuts home page");
        Thread.sleep(SLEEP_TIME_500);
        ShortcutUtil.callBackMainPageExt();//回到主页
        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "installIMessageAutomation，shortcuts home page");


        ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsGalleryTabButtonLocation);//Gallery tab点击
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//右下角
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();
        Thread.sleep(SLEEP_TIME_500);


//        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//右下角
//        Thread.sleep(SLEEP_TIME_500);
//        ExtKeyboardMouseManager.getInstance().onSpaceKey();
//       Thread.sleep(SLEEP_TIME_1000);
//        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//右下角
//        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "installIMessageAutomation，start find automation tab button and click");
//        ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAutomationTabButtonLocation);//移动到自动化tab按钮处点击
        ExtKeyboardMouseManager.getInstance().pressLeftKey();//中间tab自动化
        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();

        //一共要设置多少个自动化
        for (int i = configureIMessageAutomationSuccessNums; i < executeGetContactsNum; i++) {
            LogUtils.d(TAG, "installIMessageAutomation，set current automation times：" + (i + 1) + " all times = " + executeGetContactsNum);

            LogUtils.d(TAG, "installIMessageAutomation，start add automation button and click");
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);//右上角

            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击新建自动化
            Thread.sleep(SLEEP_TIME_2000);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到输入框
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击输入框

            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installIMessageAutomation，input message");
            ExtKeyboardMouseManager.getInstance().inputString(MESSAGE);//输入内容
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressEnterKey();
            Thread.sleep(SLEEP_TIME_2000);

//            LogUtils.d(TAG, "installIMessageAutomation，find message item and click");
//            ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAutomationSearchMessageItemLocation);//搜索出来的message位置  Message X when I get a message from mom button

            if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_3000, () -> {
                LogUtils.d(TAG, "installIMessageAutomation，find message item");
                ShortcutUtil.moveToTapExt(mouseLocations.shortcutsAutomationSearchMessageItemLocation);//搜索出来的message位置  Message X when I get a message from mom button
            }, KEYWORDS_IMESSAGE_CONFIG_AUTO_FIND_EDIT_SEARCH_ITEM_MESSAGE)) {
                LogUtils.d(TAG, "installIMessageAutomation，find message item success，click");
                ExtKeyboardMouseManager.getInstance().leftClick();//点击输入框
            } else {
                return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，find message item fail", GsonUtils.toJson(iMessageAutomationConfig)));
            }


            Thread.sleep(SLEEP_TIME_2000);
            LogUtils.d(TAG, "installIMessageAutomation，find run immediately and click");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);//右上角
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//自动运行
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击自动运行

            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installIMessageAutomation，find sender and click");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);//右上角
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//联系人
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击联系人
            Thread.sleep(SLEEP_TIME_2000);

            LogUtils.d(TAG, "installIMessageAutomation，close Permission pop-up");
            //进入到选择联系人界面，（可能有弹窗，点击先关闭弹窗）
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击激活一次
            Thread.sleep(SLEEP_TIME_1000);
            LogUtils.d(TAG, "installIMessageAutomation，start get contacts");
            //执行获取联系人电话号码，开始粘贴数据(快捷键执行快捷指令获取)
            ShortcutUtil.sendGroupKeyExt(groupKeys);
            LogUtils.d(TAG, "installIMessageAutomation，wait get contacts");
            Thread.sleep(iMessageAutomationConfig.getGetContactsWaitTime());
            LogUtils.d(TAG, "installIMessageAutomation，contacts stickup");
            ExtKeyboardMouseManager.getInstance().stickup();//粘贴数据
            Thread.sleep(SLEEP_TIME_2000);

            LogUtils.d(TAG, "installIMessageAutomation，find done button and click");
            ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAutomationContactDownButtonLocation);//选择联系人页面，右上角完成按钮

            Thread.sleep(SLEEP_TIME_3000);
            LogUtils.d(TAG, "installIMessageAutomation，find next button and click");
            ExtKeyboardMouseManager.getInstance().leftDoubleClick();//点击next按钮
            Thread.sleep(SLEEP_TIME_2000);

            //ctrl+up  down space input enter ，找位置
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到输入框
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击输入框
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installIMessageAutomation，input shortcut name");
            ExtKeyboardMouseManager.getInstance().inputString(iMessageAutomationConfig.getExecuteShortcutName());//输入快捷指令内容
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressEnterKey();
            Thread.sleep(SLEEP_TIME_2000);

            //LogUtils.d(TAG, "installIMessageAutomation，start find shortcut name");
            //ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAutomationSelectShortcutSecondItemLocation);//快捷指令-新建自动化-最后一步选择快捷指令页面-搜索出来的第1项位置

            //验证快捷指令名字(第一个位置)  select add matter push message button
            if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
                LogUtils.d(TAG, "installIMessageAutomation，start find shortcut name");
                ShortcutUtil.moveToTapExt(mouseLocations.shortcutsAutomationSelectShortcutSecondItemLocation);//快捷指令-新建自动化-最后一步选择快捷指令页面-搜索出来的第1项位置）
            }, KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON)) {
                LogUtils.d(TAG, "installIMessageAutomation，find shortcut name success，click");
                ExtKeyboardMouseManager.getInstance().leftDoubleClick();
            } else {
                return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，find shortcut name fail", GsonUtils.toJson(iMessageAutomationConfig)));
            }

            LogUtils.d(TAG, "installIMessageAutomation，set current automation times：" + (i + 1), " success");
            iMessageAutomationConfig.setConfigureIMessageAutomationSuccessNums(i + 1);
        }

        return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "installIMessageAutomation，success", GsonUtils.toJson(iMessageAutomationConfig)));
    }


    public static ResponseResult installIMessageAutomation(IMessageAutomationConfig iMessageAutomationConfig, String traceId) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，mouseLocations is null", GsonUtils.toJson(iMessageAutomationConfig)));
        }
        LogUtils.d(TAG, "installIMessageAutomation，turnOnNarration open，start open shortcuts app");
        String shortcutKey = iMessageAutomationConfig.getShortcutKey();
        if (TextUtils.isEmpty(shortcutKey)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，shortcutKey is null", GsonUtils.toJson(iMessageAutomationConfig)));
        }
        int configureIMessageAutomationSuccessNums = iMessageAutomationConfig.getConfigureIMessageAutomationSuccessNums();
        int executeGetContactsNum = iMessageAutomationConfig.getExecuteGetContactsNum();
        if (configureIMessageAutomationSuccessNums >= executeGetContactsNum) {
            return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "installIMessageAutomation，all success", GsonUtils.toJson(iMessageAutomationConfig)));
        }
//        ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_SHORTCUTS, traceId);//打开快捷指令app
        ShortcutUtil.openShortcutExt(false, traceId);//打开快捷指令app
        LogUtils.d(TAG, "installIMessageAutomation，shortcuts app is open，start call back shortcuts home page");
        Thread.sleep(SLEEP_TIME_1000);
        ShortcutUtil.callBackMainPageExt();//回到主页
        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "installIMessageAutomation，shortcuts home page");

        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角
        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//右下角gallery
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//右下角gallery，有时候一次不生效，得两次
        LogUtils.d(TAG, "installIMessageAutomation，start find gallery tab button");
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressLeftKey();//tab自动化
        LogUtils.d(TAG, "installIMessageAutomation，start find automation tab button and click");
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击tab自动化

        //一共要设置多少个自动化
        for (int i = configureIMessageAutomationSuccessNums; i < executeGetContactsNum; i++) {
            LogUtils.d(TAG, "installIMessageAutomation，set current automation times：" + (i + 1) + " all times = " + executeGetContactsNum);

            LogUtils.d(TAG, "installIMessageAutomation，start add automation button and click");
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//添加自动化

            LogUtils.d(TAG, "installIMessageAutomation，start add automation button and click");
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击新建自动化
            Thread.sleep(SLEEP_TIME_2000);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到输入框，可以在这验证
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击输入框

            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installIMessageAutomation，input message");
            ExtKeyboardMouseManager.getInstance().inputString(MESSAGE);//输入内容
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressEnterKey();
            Thread.sleep(SLEEP_TIME_1000);

            LogUtils.d(TAG, "installIMessageAutomation，start find message item and click");
            ExtKeyboardMouseManager.getInstance().pressRightKey();
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//message
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//message
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到message上
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击message


            Thread.sleep(SLEEP_TIME_2000);
            LogUtils.d(TAG, "installIMessageAutomation，find run immediately and click");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);//右上角
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//自动运行
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击自动运行

            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installIMessageAutomation，find sender and click");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.UP);//右上角
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//联系人
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击联系人
            Thread.sleep(SLEEP_TIME_2000);

            LogUtils.d(TAG, "installIMessageAutomation，close Permission pop-up");
            //进入到选择联系人界面，（可能有弹窗，点击先关闭弹窗）
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击关闭弹窗
            Thread.sleep(SLEEP_TIME_1000);
            LogUtils.d(TAG, "installIMessageAutomation，start get contacts");
            //执行获取联系人电话号码，开始粘贴数据(快捷键执行快捷指令获取)
//            ShortcutUtil.sendGroupKeyExt(groupKeys);
//            if (Utils.getBlecConnectStatus()) {
//                if (!Utils.sendBleKey(shortcutKey)) {
//                    return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，sendBleKey fail", GsonUtils.toJson(iMessageAutomationConfig)));
//                }
//            } else {
//                return endConfig(new ResponseResult(RESPONSE_CODE_BLE_CONNECT_FAIL, "installIMessageAutomation，ble is not connect", GsonUtils.toJson(iMessageAutomationConfig)));
//            }

//            Thread.sleep(SLEEP_TIME_500);

            LogUtils.d(TAG, "installIMessageAutomation，wait get contacts");
            Thread.sleep(iMessageAutomationConfig.getGetContactsWaitTime());
            LogUtils.d(TAG, "installIMessageAutomation，contacts stickup");
//            ExtKeyboardMouseManager.getInstance().stickup();//粘贴数据
            ExtKeyboardMouseManager.getInstance().inputString("test");//输入快捷指令内容 todo 测试
            Thread.sleep(SLEEP_TIME_2000);

            LogUtils.d(TAG, "installIMessageAutomation，find done button and click");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角cancel
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressRightKey();//done按钮
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击done
            Thread.sleep(SLEEP_TIME_2000);

            LogUtils.d(TAG, "installIMessageAutomation，find next button and click");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角back
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressRightKey();//next按钮
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击next
            Thread.sleep(SLEEP_TIME_1000);

            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//左上角返回箭头
            //ctrl+up  down space input enter ，找位置
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到输入框
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击输入框
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "installIMessageAutomation，input shortcut name：" + iMessageAutomationConfig.getExecuteShortcutName());
            ExtKeyboardMouseManager.getInstance().inputString(iMessageAutomationConfig.getExecuteShortcutName());//输入快捷指令内容
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().pressEnterKey();
            Thread.sleep(SLEEP_TIME_500);

            //验证快捷指令名字(第一个位置)  select add matter push message button
            String phoneModel = mouseLocations.phoneModel;
            if (IPhoneModelEnum.IPHONE_MODEL_16.getModelName().equals(phoneModel) ||
                    IPhoneModelEnum.IPHONE_MODEL_16_PLUS.getModelName().equals(phoneModel) ||
                    IPhoneModelEnum.IPHONE_MODEL_16_PRO.getModelName().equals(phoneModel) ||
                    IPhoneModelEnum.IPHONE_MODEL_16_PRO_MAX.getModelName().equals(phoneModel)
            ) {
                ExtKeyboardMouseManager.getInstance().pressRightKey();
                Thread.sleep(SLEEP_TIME_1000);
                ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//message
                Thread.sleep(SLEEP_TIME_1000);
                ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);//message
                Thread.sleep(SLEEP_TIME_1000);
                if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_3000, () -> {
                    LogUtils.d(TAG, "installIMessageAutomation，start find shortcut name");
                    ExtKeyboardMouseManager.getInstance().pressDownKey();//移动到message上
                }, KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON)) {
                    LogUtils.d(TAG, "installIMessageAutomation，find shortcut name success，click");
                    ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击message
                } else {
                    return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，find shortcut name fail", GsonUtils.toJson(iMessageAutomationConfig)));
                }
                Thread.sleep(SLEEP_TIME_1000);
            } else {
                if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
                    LogUtils.d(TAG, "installIMessageAutomation，start find shortcut name");
                    ShortcutUtil.moveToTapExt(mouseLocations.shortcutsAutomationSelectShortcutSecondItemLocation);//快捷指令-新建自动化-最后一步选择快捷指令页面-搜索出来的第1项位置）
                }, KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON)) {
                    LogUtils.d(TAG, "installIMessageAutomation，find shortcut name success，click");
                    ExtKeyboardMouseManager.getInstance().leftDoubleClick();
                } else {
                    return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "installIMessageAutomation，find shortcut name fail", GsonUtils.toJson(iMessageAutomationConfig)));
                }
                Thread.sleep(SLEEP_TIME_1000);
//              ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAutomationSelectShortcutSecondItemLocation);//快捷指令-新建自动化-最后一步选择快捷指令页面-搜索出来的第1项位置）
            }
            LogUtils.d(TAG, "installIMessageAutomation，set current automation times：" + (i + 1), " success");
            iMessageAutomationConfig.setConfigureIMessageAutomationSuccessNums(i + 1);
        }
        return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "installIMessageAutomation，success", GsonUtils.toJson(iMessageAutomationConfig)));
    }

    private static ResponseResult endConfig(ResponseResult result) {
        if (result.getCode() == RESPONSE_CODE_SUCCESS) {
            LogUtils.d(TAG, result.getMessage());
        } else {
            LogUtils.e(TAG, result.getMessage());
        }
        return result;
    }


    /**
     * IMessage自动化更新联系人 tagone,17611077813，17611077814,17611077815,
     */
    public static ResponseResult updateIMessageAutomationConstant(IMessageAutomationUpdateContact iMessageAutomationUpdate, String traceId) throws InterruptedException {
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant，mouseLocations is null"));
        }
        LogUtils.d(TAG, "updateIMessageAutomationConstant，turnOnNarration open，start open shortcuts app");
        GroupKeyBean groupKeys = iMessageAutomationUpdate.getGroupKeys();
        if (groupKeys == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant，groupKeys is null"));
        }
        String automationNameTag = iMessageAutomationUpdate.getAutomationNameTag();
        if (automationNameTag == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant，automationNameTag is null"));
        }
        String phones = iMessageAutomationUpdate.getPhones();
        if (phones == null) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant，phones is null"));
        }

//        ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_SHORTCUTS, traceId);//打开快捷指令app
        ShortcutUtil.openShortcutExt(false, traceId);//打开快捷指令app
        LogUtils.d(TAG, "updateIMessageAutomationConstant，shortcuts app is open，start call back shortcuts home page");
        Thread.sleep(SLEEP_TIME_500);
        ShortcutUtil.callBackMainPageExt();//回到主页
        LogUtils.d(TAG, "updateIMessageAutomationConstant，shortcuts home page");

        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.LEFT_CTRL, Constants.DOWN);//移动到右下角
        Thread.sleep(SLEEP_TIME_500);

        LogUtils.d(TAG, "updateIMessageAutomationConstant，start find automation tab button");
        ExtKeyboardMouseManager.getInstance().pressLeftKey();//移动到自动化tab按钮处
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击automation tab button

        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.LEFT_CTRL, Constants.UP);//移动到最上面

        LogUtils.d(TAG, "updateIMessageAutomationConstant，start find  needs to be modified automation");
        //开始找要修改的自动化
//        ResponseResult isFindAutomationSuccess = findAutomationTag(automationNameTag, KEYWORDS_IMESSAGE_CONFIG_AUTO_SHORTCUTS_TAB, traceId);
//        if (isFindAutomationSuccess.getCode() != RESPONSE_CODE_SUCCESS) {
//            return isFindAutomationSuccess;
//        }
        //todo 临时，就当找到了
        ExtKeyboardMouseManager.getInstance().pressDownKey();//往下移动，找指定的
        Thread.sleep(SLEEP_TIME_500);


        LogUtils.d(TAG, "updateIMessageAutomationConstant，find  needs to be modified automation success，click");
        //找到了对应的自动化，进入开始操作
        ExtKeyboardMouseManager.getInstance().onSpaceKey();
        Thread.sleep(SLEEP_TIME_2000);

        //down right right right,找到当收到xxx的消息
        ExtKeyboardMouseManager.getInstance().pressDownKey();
        Thread.sleep(SLEEP_TIME_500);
        ShortcutUtil.pressRightKeyExt(3);
        LogUtils.d(TAG, "updateIMessageAutomationConstant，click when i get message from xxx");
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击当收到xxx的消息

        Thread.sleep(SLEEP_TIME_2000);
        LogUtils.d(TAG, "updateIMessageAutomationConstant，click sender");
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击联系人

//        Thread.sleep(5000);
//        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击激活编辑框
//        LogUtils.d(TAG, "updateIMessageAutomationConstant，input phones");
//        ExtKeyboardMouseManager.getInstance().inputString(phones);//输入电话号码

        //进入到选择联系人界面，（可能有弹窗，点击先关闭弹窗）--- 粘贴复制
        Thread.sleep(5000);
        LogUtils.d(TAG, "installIMessageAutomation，start get contacts");
        //执行获取联系人电话号码，开始粘贴数据(快捷键执行快捷指令获取)
        ShortcutUtil.sendGroupKeyExt(groupKeys);
        ExtKeyboardMouseManager.getInstance().allSelect();//全选
        ExtKeyboardMouseManager.getInstance().onBackSpaceKey();//删除
        LogUtils.d(TAG, "installIMessageAutomation，wait get contacts");
        Thread.sleep(iMessageAutomationUpdate.getGetContactsWaitTime());
        LogUtils.d(TAG, "installIMessageAutomation，contacts stickup");
        ExtKeyboardMouseManager.getInstance().stickup();//粘贴数据

        Thread.sleep(SLEEP_TIME_2000);
//        ExtKeyboardMouseManager.getInstance().resetMousePositionTopRight();//右上角
//        Thread.sleep(SLEEP_TIME_500);
//        ExtKeyboardMouseManager.getInstance().tap(-5, 40);//todo 鼠标位置 选择联系人页面，右上角完成按钮
//        LogUtils.d(TAG, "updateIMessageAutomationConstant，find done button success，click");
//        Thread.sleep(SLEEP_TIME_500);
//        ExtKeyboardMouseManager.getInstance().leftClick();//点击第一个完成

        ShortcutUtil.moveToTapAndClickExt(mouseLocations.shortcutsAutomationContactDownButtonLocation);//todo 鼠标位置 选择联系人页面，右上角完成按钮

        Thread.sleep(SLEEP_TIME_2000);
        ExtKeyboardMouseManager.getInstance().leftClick();//点击第二个完成

        return new ResponseResult(RESPONSE_CODE_SUCCESS, "updateIMessageAutomationConstant，success");
    }

    /**
     * 查找指定tag的自动化
     */
    private static ResponseResult findAutomationTag(String automationNameTag, String endString, String traceId) throws InterruptedException {
        //第一次
        String voiceText = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_15000, () -> {
            ExtKeyboardMouseManager.getInstance().pressDownKey();//往下移动，找指定的
        });

        //验证文本，是找到指定的自动化了，还是到底了
        if (voiceText.contains(automationNameTag)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "updateIMessageAutomationConstant，findAutomationTag，success"));
        } else if (voiceText.contains(endString)) {
            return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant，findAutomationTag，not find"));
        }

        while (true) {

            ExtKeyboardMouseManager.getInstance().pressRightKey();
            voiceText = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, 15000, () -> {
                ExtKeyboardMouseManager.getInstance().pressRightKey();//找指定的自动化
            });

            //验证文本，是找到指定的自动化了，还是到底了
            if (voiceText.contains(automationNameTag)) {
                return endConfig(new ResponseResult(RESPONSE_CODE_SUCCESS, "updateIMessageAutomationConstant，findAutomationTag，success"));
            } else if (voiceText.contains(endString)) {
                return endConfig(new ResponseResult(RESPONSE_CODE_FAIL, "updateIMessageAutomationConstant，findAutomationTag，not find"));
            }
        }
    }


}
