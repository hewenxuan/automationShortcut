package ai.matter.agentruntime.manager;

import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_ASSISTIVE_TOUCH_ALWAYS_SHOW_MENU_OFF;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_FULL_KEYBOARD_ACCESS;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_FULL_KEYBOARD_ACCESS_AUTO_HIDE_OFF;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_FULL_KEYBOARD_ACCESS_OFF;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_FULL_KEYBOARD_SETTING_SEARCH_EDITTEXT;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_5000;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_FULL_KEYBOARD_ACCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_FULL_KEYBOARD_ACCESS_AITO_HIDE;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_FULL_KEYBOARD_ACCESS_EDIT;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_SETTINGS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_500;

import android.text.TextUtils;

import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.bean.location.MouseLocations;
import ai.matter.agentruntime.enums.IPhoneModelEnum;
import ai.matter.agentruntime.utils.LogUtils;
import ai.matter.agentruntime.utils.ShortcutUtil;
import ai.matter.extbt.profile.Constants;

/**
 * 第一个自动化配置其他Rpa配置操作
 */
public class FirstAutomationOtherConfigRpaManager {
    private static final String TAG = "Shortcut FirstAutomationOtherConfigRpaManager ";

    /**
     * 打开辅助触控功能
     */
    public static ResponseResult openAssistiveTouch(String traceId) throws InterruptedException {
        //语音打开辅助触控功能
        return VoiceInjectionManager.openAccessibilityTouch(traceId);
    }

    /**
     * 打开全键盘控制开关、设置全键盘配置
     * - 「自动隐藏」- 决定了显示在此处的蓝框（打开）
     * - 多久消失（秒，默认 15 秒；最少 1 秒）：调整为1秒
     */
    public static ResponseResult openFullKeyboardControl1(String traceId, int num) throws InterruptedException {
        ExtKeyboardMouseManager.getInstance().resetMousePositionTopLeft();//左上角
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            LogUtils.e(TAG, "openFullKeyboardControl，mouseLocations is null");
            return new ResponseResult(RESPONSE_CODE_FAIL, "openFullKeyboardControl，mouseLocations is null");
        }
        LogUtils.d(TAG, "openFullKeyboardControl，open setting");
//        VoiceInjectionManager.openSettings(traceId);
        ShortcutUtil.openSettingsExt(false, traceId);//打开设置
        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "openFullKeyboardControl，open setting success,callback main page");
        ShortcutUtil.callBackMainPageExt();//返回主页


//        ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_FULL_KEYBOARD_ACCESS,traceId);
        //右2次，怎么激活

        //一直返回首页

        //ctrl up 最上面

        //right 编辑框

        //tab 激活进入搜索

        //输入full

        //down 搜索第一个结果

        //enter 进入

        //right 3次到 full keyboard access 第一次

        //enter 进入？进不去

        //进入后可以验证，打开开关。

        LogUtils.d(TAG, "openFullKeyboardControl，call back setting main page top");
        ShortcutUtil.scrollPageTopExt();//回到设置首页最上面
//       Thread.sleep(SLEEP_TIME_1000);

        LogUtils.d(TAG, "openFullKeyboardControl，start find edittext");
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
            LogUtils.d(TAG, "openFullKeyboardControl，start find search");
            ShortcutUtil.moveToTapExt(mouseLocations.settingSearchEditTextLocation);//search search field double tap to edit 鼠标位置
        }, KEYWORDS_FULL_KEYBOARD_SETTING_SEARCH_EDITTEXT)) {
            LogUtils.d(TAG, "openFullKeyboardControl，find search success，click");
            ExtKeyboardMouseManager.getInstance().leftClick();//点击搜索
        } else {
            LogUtils.e(TAG, "openFullKeyboardControl，find search fail");
            return new ResponseResult(RESPONSE_CODE_FAIL, "openFullKeyboardControl，find search fail");
        }

        LogUtils.d(TAG, "openFullKeyboardControl，start input full keyboard access");
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().allSelect();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onBackSpaceKey();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().inputString(SEARCH_FULL_KEYBOARD_ACCESS_EDIT);//输入全键盘
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();

        LogUtils.d(TAG, "openFullKeyboardControl，start find search first full keyboard access");
        Thread.sleep(SLEEP_TIME_500);
        ShortcutUtil.moveToTapExt(mouseLocations.settingSearchResultListFirstItemLocation);//搜索出来的第一个  Full keyboard access

        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "openFullKeyboardControl，find search full keyboard access success，click enter");
        ExtKeyboardMouseManager.getInstance().leftClick();//点击进入

        LogUtils.d(TAG, "openFullKeyboardControl，start find first full keyboard access ");
        Thread.sleep(SLEEP_TIME_1000);
        ShortcutUtil.moveToTapExt(mouseLocations.settingFullKeyboardAccessFirstLocation);//Full keyboard access 第一次

        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "openFullKeyboardControl，find first full keyboard access success，click enter");
        ExtKeyboardMouseManager.getInstance().leftClick();//点击Full keyboard access

        Thread.sleep(SLEEP_TIME_1000);
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
            LogUtils.d(TAG, "openFullKeyboardControl，verification full keyboard access Switch button off");
            ShortcutUtil.moveToTapExt(mouseLocations.settingFullKeyboardAccessButtonSecondLocation);//Full keyboard access 第二次 验证是否开启
        }, KEYWORDS_FULL_KEYBOARD_ACCESS_OFF)) {//验证是否开启
            LogUtils.d(TAG, "openFullKeyboardControl，full keyboard access Switch button off,click open");
            ExtKeyboardMouseManager.getInstance().leftClick();//打开
        }

        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "openFullKeyboardControl，move ctrl + down ,go bottom");
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);
        ShortcutUtil.pressLeftKeyExt(2);

        ExtKeyboardMouseManager.getInstance().pressLeftKey();//自动隐藏item auto hide off button  100,-250

        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
            LogUtils.d(TAG, "openFullKeyboardControl，start find auto hide item");
            ExtKeyboardMouseManager.getInstance().pressLeftKey();//自动隐藏item auto hide off button  100,-250
        }, KEYWORDS_FULL_KEYBOARD_ACCESS_AUTO_HIDE_OFF)) {
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击自动隐藏item，进入自动隐藏页面
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//打开自动隐藏
        } else {
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//击自动隐藏item，进入自动隐藏页面
        }

        LogUtils.d(TAG, "openFullKeyboardControl，tab to move to seconds");
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().enterKeyCombination(Constants.TAB);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//激活
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().allSelect();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onBackSpaceKey();
        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "openFullKeyboardControl，input num");
        ExtKeyboardMouseManager.getInstance().inputString("" + num);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();

        LogUtils.d(TAG, "openFullKeyboardControl，set success");
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "openFullKeyboardControl success");
    }

    /**
     * 打开全键盘控制开关、设置全键盘配置
     * - 「自动隐藏」- 决定了显示在此处的蓝框（打开）
     * - 多久消失（秒，默认 15 秒；最少 1 秒）：调整为1秒
     */
    public static ResponseResult openFullKeyboardControl(String traceId, int num) throws InterruptedException {
//        ExtKeyboardMouseManager.getInstance().resetMousePositionTopLeft();//左上角
        ExtKeyboardMouseManager.getInstance().resetMousePositionTopRight();
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            LogUtils.e(TAG, "openFullKeyboardControl，mouseLocations is null");
            return new ResponseResult(RESPONSE_CODE_FAIL, "openFullKeyboardControl，mouseLocations is null");
        }
        String phoneModel = mouseLocations.phoneModel;
        if (IPhoneModelEnum.IPHONE_MODEL_16.getModelName().equals(phoneModel) ||
                IPhoneModelEnum.IPHONE_MODEL_16_PLUS.getModelName().equals(phoneModel) ||
                IPhoneModelEnum.IPHONE_MODEL_16_PRO.getModelName().equals(phoneModel) ||
                IPhoneModelEnum.IPHONE_MODEL_16_PRO_MAX.getModelName().equals(phoneModel)
        ) {
            LogUtils.d(TAG, "openFullKeyboardControl，open setting");
            ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_SETTINGS, traceId);
            Thread.sleep(SLEEP_TIME_1000);
            LogUtils.d(TAG, "openFullKeyboardControl，open setting success,callback main page");
            ShortcutUtil.callBackMainPageExt();
            LogUtils.d(TAG, "openFullKeyboardControl，click search");
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击搜索
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().allSelect();//全选
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "openFullKeyboardControl，input full keyboard access");
            ExtKeyboardMouseManager.getInstance().inputString(SEARCH_FULL_KEYBOARD_ACCESS);
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressEnterKey();
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d(TAG, "openFullKeyboardControl，click search result");
            ShortcutUtil.moveToTapAndClickExt(mouseLocations.settingSearchResultListFirstItemLocation);//搜索出来的第一个  Full keyboard access
            ExtKeyboardMouseManager.getInstance().resetMousePositionTopRight();//右上角
        } else {
            //iphone 15 pro
            ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_FULL_KEYBOARD_ACCESS_AITO_HIDE, traceId);//打开全键盘控制开关页面- Auto-Hide
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().onEscKey();//返回上一级 打开全键盘控制开关页面
        }

        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "openFullKeyboardControl，verification full keyboard access Switch button");
        String voiceTextWithTimeout = VoiceAsideVerificationManager.getInstance().getVoiceTextWithTimeout(traceId, TIME_5000, () -> {
            ShortcutUtil.moveToTapExt(mouseLocations.settingFullKeyboardAccessButtonSecondLocation);//Full keyboard access 第二次 验证是否开启
        });
        if (!TextUtils.isEmpty(voiceTextWithTimeout)) {
            LogUtils.d(TAG, "openFullKeyboardControl，full keyboard access voiceTextWithTimeout ：" + voiceTextWithTimeout);
            if (voiceTextWithTimeout.contains(KEYWORDS_FULL_KEYBOARD_ACCESS)) {
                if (voiceTextWithTimeout.contains(KEYWORDS_FULL_KEYBOARD_ACCESS_OFF)) {
                    LogUtils.d(TAG, "openFullKeyboardControl，full keyboard access Switch button off,click open");
                    ExtKeyboardMouseManager.getInstance().leftClick();//打开
                } else {
                    LogUtils.d(TAG, "openFullKeyboardControl，full keyboard access Switch button is on");
                }
            } else {
                LogUtils.e(TAG, "openFullKeyboardControl，full keyboard access Switch button off fail，location is error");
                return new ResponseResult(RESPONSE_CODE_FAIL, "openFullKeyboardControl fail，location is error");
            }
        } else {
            LogUtils.e(TAG, "openFullKeyboardControl，full keyboard access Switch button off fail，voiceTextWithTimeout is null");
            return new ResponseResult(RESPONSE_CODE_FAIL, "openFullKeyboardControl fail，voiceTextWithTimeout is null");
        }

        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "openFullKeyboardControl，move ctrl + down ,go bottom");
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);
        Thread.sleep(SLEEP_TIME_500);
        ShortcutUtil.pressLeftKeyExt(2);

        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
            LogUtils.d(TAG, "openFullKeyboardControl，start find auto hide item");
            ExtKeyboardMouseManager.getInstance().pressLeftKey();//自动隐藏item auto hide off button  100,-250
        }, KEYWORDS_FULL_KEYBOARD_ACCESS_AUTO_HIDE_OFF)) {
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击自动隐藏item，进入自动隐藏页面
            Thread.sleep(SLEEP_TIME_1000);
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//打开自动隐藏
        } else {
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//击自动隐藏item，进入自动隐藏页面
        }

        LogUtils.d(TAG, "openFullKeyboardControl，tab to move to seconds");
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().enterKeyCombination(Constants.TAB);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//激活
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().allSelect();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onBackSpaceKey();
        Thread.sleep(SLEEP_TIME_500);
        LogUtils.d(TAG, "openFullKeyboardControl，input num");
        ExtKeyboardMouseManager.getInstance().inputString("" + num);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);

        LogUtils.d(TAG, "openFullKeyboardControl，set success");
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "openFullKeyboardControl success");
    }

    /**
     * 设置辅助触控配置
     */
    public static ResponseResult setAssistiveTouchConfig(String traceId) throws InterruptedException {
        LogUtils.d(TAG, "setAssistiveTouchConfig，open assistive touch settings page");
        MouseLocations mouseLocations = ShortcutUtil.getMouseLocations();
        if (mouseLocations == null) {
            LogUtils.e(TAG, "openFullKeyboardControl，mouseLocations is null");
            return new ResponseResult(RESPONSE_CODE_FAIL, "openFullKeyboardControl，mouseLocations is null");
        }
        LogUtils.d(TAG, "setAssistiveTouchConfig，open assistive touch settings page");
//        VoiceInjectionManager.openAssistiveTouchSettings(traceId);//打开辅助触控设置页面
        ShortcutUtil.openAssistiveTouchSettingsExt(false, traceId);
//        ShortcutUtil.userSearchOpenAppByNameExt(SEARCH_ALWAYS_SHOW_MENU, traceId);//打开辅助触控设置页面
        Thread.sleep(SLEEP_TIME_1000);
        LogUtils.d(TAG, "setAssistiveTouchConfig，call back assistive touch settings page top");
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
            ShortcutUtil.pressRightKeyExt(17);
        } else {
            //iphone 15 pro
            ShortcutUtil.pressRightKeyExt(15);
        }

        if (!VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
            LogUtils.d(TAG, "setAssistiveTouchConfig，start find always show menu switch button off");
            //Always show menu, switch button, on. Double tap to toggle setting.
            ExtKeyboardMouseManager.getInstance().pressRightKey();//移动到 always show menu 位置验证
        }, KEYWORDS_ASSISTIVE_TOUCH_ALWAYS_SHOW_MENU_OFF)) {
            LogUtils.d(TAG, "setAssistiveTouchConfig，find always show menu switch button off success，click open");
            ExtKeyboardMouseManager.getInstance().onSpaceKey();
        }
        LogUtils.d(TAG, "setAssistiveTouchConfig，set success");
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "setAssistiveTouchConfig success");
    }

}
