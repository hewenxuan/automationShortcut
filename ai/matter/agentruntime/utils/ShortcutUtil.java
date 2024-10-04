package ai.matter.agentruntime.utils;

import static ai.matter.agentruntime.constant.AsideKeywordsConstant.KEYWORDS_SWITCH_INPUTMETHOD_ENGLISH;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.NETWORK_ALERT_DO_NOT_ALLOW_BUTTON;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_3000;
import static ai.matter.agentruntime.constant.AsideKeywordsConstant.TIME_5000;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_FAIL;
import static ai.matter.agentruntime.constant.ResponseCodeConstant.RESPONSE_CODE_SUCCESS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_ALWAYS_SHOW_MENU;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_ONE_ACTION;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_SAFARI;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_SETTINGS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SEARCH_SHORTCUTS;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_1500;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_2000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_3000;
import static ai.matter.agentruntime.constant.ShortCutConstant.SLEEP_TIME_500;

import android.text.TextUtils;

import ai.matter.agentruntime.bean.GroupKeyBean;
import ai.matter.agentruntime.bean.ResponseResult;
import ai.matter.agentruntime.bean.location.Location;
import ai.matter.agentruntime.bean.location.MouseLocations;
import ai.matter.agentruntime.enums.IPhoneModelEnum;
import ai.matter.agentruntime.enums.KeyEnum;
import ai.matter.agentruntime.manager.ExtKeyboardMouseManager;
import ai.matter.agentruntime.manager.KeyboardMouseManager;
import ai.matter.agentruntime.manager.VoiceAsideVerificationManager;
import ai.matter.agentruntime.manager.VoiceInjectionManager;
import ai.matter.bthid.KeyboardHelper;
import ai.matter.extbt.profile.Constants;

/**
 * 快捷指令工具类
 */
public class ShortcutUtil {

    /**
     * 回到oneAction/webApp
     */
    public static ResponseResult returnToOneActionWebApp(boolean isWeb, String webAppLink, String traceId) throws InterruptedException {
        LogUtils.d("returnToOneActionWebApp，isWeb：" + isWeb + "，webAppLink：" + webAppLink);
        if (isWeb) {
            return openBrowserExt(webAppLink, traceId);
        } else {
            return userSearchOpenAppByName(SEARCH_ONE_ACTION);
//            return VoiceInjectionManager.openOneActionApp(traceId);
        }
    }

    /**
     * 打开浏览器
     */
    public static ResponseResult openBrowser(String url, String traceId) throws InterruptedException {
        if (TextUtils.isEmpty(url)) {
            return new ResponseResult(RESPONSE_CODE_FAIL, "打开链接失败，url为空");
        }
        VoiceInjectionManager.openBrowser(traceId); //语音打开浏览器
//        userSearchOpenAppByName(SEARCH_SAFARI);//打开浏览器
        ShortcutUtil.pressDownKey(5);//往下移动5次
        ShortcutUtil.pressUpKey(1);//往上移动1次，找到输入框
        KeyboardMouseManager.getInstance().pressActivationSpaceKey();//激活输入框连接
        KeyboardMouseManager.getInstance().inputString(url);//输入url链接
        KeyboardMouseManager.getInstance().pressEnterKey();//回车按钮开始请求加载链接
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "打开浏览器成功");
    }

    /**
     * 打开应用（使用快捷键进入搜索页面进行打开）
     * 可直接打开：Settings/Shortcuts/Safari/OneAction/Accessibility/Full keyboard access
     * 进去得确认才能打开：Full keyboard access / AssistiveTouch /Always Show Menu /
     */
    public static ResponseResult userSearchOpenAppByName(String name) throws InterruptedException {
        KeyboardMouseManager.getInstance().pressPowerKey();//回到首页
        Thread.sleep(SLEEP_TIME_1000);
        KeyboardMouseManager.getInstance().openSearchPage();//打开搜索页面
        Thread.sleep(SLEEP_TIME_500);
        KeyboardMouseManager.getInstance().inputString(name);
        KeyboardMouseManager.getInstance().pressEnterKey();
        KeyboardMouseManager.getInstance().pressEnterKey();
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "执行成功");
    }


    /**
     * 返回应用主页
     */
    public static void callBackMainPage() {
        for (int i = 0; i < 10; i++) {
            try {
                KeyboardMouseManager.getInstance().pressBackKey();
                Thread.sleep(SLEEP_TIME_500);
            } catch (InterruptedException e) {

            }
        }
    }


    //是否切换过英文输入法,切换过不再切换
    private static boolean isSwitchEnglishInputMethod = false;

    /**
     * 切换英文输入发（必须在输入框激活后切换） 切换一次就够
     */
    public static void switchEnglishInputMethod(String traceId) {
        if (isSwitchEnglishInputMethod) {
            return;
        }
        for (int i = 0; i < 6; i++) {//切换6次，直到切换到英文为止
            KeyboardMouseManager.getInstance().switchInputMethod();

//            String voiceText = VoiceAsideVerificationManager.getVoiceText(traceId, () -> KeyboardMouseManager.getInstance().leftClick());//获取旁白文字
//            if (voiceText.contains("English")) {
//                isSwitchEnglishInputMethod = true;
//                return;
//            }
        }
    }


    /**
     * 根据鼠标移动到指定位置 旁白验证是否为要找的元素(前提打开旁白)
     *
     * @param traceId   traceId
     * @param x         x坐标
     * @param y         y坐标
     * @param sleepTime 移动后休眠多久
     * @param keyword   关键字
     * @return 是否是要找的元素
     */
    public boolean findElementByMouse(String traceId, int x, int y, long sleepTime, String keyword) {
        KeyboardMouseManager.getInstance().tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> KeyboardMouseManager.getInstance().leftClick(), keyword);
    }


    /**
     * 根据键盘查找某个元素并验证
     *
     * @param traceId            traceId
     * @param keyword            关键字
     * @param isDownFind         是否往下查找
     * @param resetMoveTimes     重置移动次数
     * @param isAsideMode        是否是旁白模式
     * @param isSearchByCategory 是否是分类查查找
     * @param times              查找次数
     * @param keyword            关键字
     * @return 是否是要找的元素
     */
    public static boolean findElementByKeyboard(String traceId, boolean isDownFind, int resetMoveTimes, int times, boolean isAsideMode, boolean isSearchByCategory, String keyword) {
        //旁白模式有时候会丢失焦点，需要鼠标先移动一下，先找到焦点，然后操作
        if (isAsideMode) {
            KeyboardMouseManager.getInstance().resetMousePosition();
            KeyboardMouseManager.getInstance().tap(400, 400);
        } else {
            //todo 非旁白模式下也可能丢失焦点，还不确定怎么恢复焦点
        }
        //重置元素位置
        if (resetMoveTimes > 0) {
            if (isDownFind) {//往下查找，需要滑动到最上面
                if (isAsideMode) {//旁白模式
                    if (isSearchByCategory) {//分类查找
                        ShortcutUtil.pressUpKey(resetMoveTimes);//往上移动
                    } else {//普通查找
                        ShortcutUtil.pressLeftKey(resetMoveTimes);//往上移动
                    }
                } else {//非旁白模式
                    ShortcutUtil.pressUpKey(resetMoveTimes);//往上移动
                }
            } else {//往上查找，需要滑动到最下面
                if (isAsideMode) {//旁白模式
                    if (isSearchByCategory) {//分类查找
                        ShortcutUtil.pressDownKey(resetMoveTimes);//往下移动
                    } else {//普通查找
                        ShortcutUtil.pressRightKey(resetMoveTimes);//往下移动
                    }
                } else {//非旁白模式
                    ShortcutUtil.pressDownKey(resetMoveTimes);//往下移动
                }
            }
        }
        //开始查找元素
        for (int i = 0; i < times; i++) {
            boolean verificationResult = VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
                if (isDownFind) {//往下查找
                    if (isAsideMode) {//旁白模式
                        if (isSearchByCategory) {//分类查找
                            ShortcutUtil.pressDownKey(resetMoveTimes);//往下移动
                        } else {//普通查找
                            ShortcutUtil.pressRightKey(resetMoveTimes);//往下移动
                        }
                    } else {//非旁白模式
                        KeyboardMouseManager.getInstance().pressDownKey();
                    }
                } else {//往上查找
                    if (isAsideMode) {//旁白模式
                        if (isSearchByCategory) {//分类查找
                            ShortcutUtil.pressUpKey(resetMoveTimes);//往上移动
                        } else {//普通查找
                            ShortcutUtil.pressRightKey(resetMoveTimes);//往下移动
                        }
                    } else {//非旁白模式
                        KeyboardMouseManager.getInstance().pressUpKey();
                    }
                }
            }, keyword);
            if (verificationResult) {
                return true;
            }
        }
        return false;
    }

    /**
     * 按下上键（非旁白）
     */
    public static void pressUpKey(int times) {
        for (int i = 0; i < times; i++) {
            KeyboardMouseManager.getInstance().pressUpKey();
        }
    }

    /**
     * 按下下键（非旁白）
     */
    public static void pressDownKey(int times) {
        for (int i = 0; i < times; i++) {
            KeyboardMouseManager.getInstance().pressDownKey();
        }
    }

    /**
     * 按下左键（非旁白）
     */
    public static void pressLeftKey(int times) {
        for (int i = 0; i < times; i++) {
            KeyboardMouseManager.getInstance().pressLeftKey();
        }
    }

    /**
     * 按下右键（非旁白）
     */
    public static void pressRightKey(int times) {
        for (int i = 0; i < times; i++) {
            KeyboardMouseManager.getInstance().pressRightKey();
        }
    }

    /**
     * 按下快捷键
     */
    public static void sendGroupKey(GroupKeyBean groupKeyBean) {
        int modifier = 0;
        if (groupKeyBean.isShift()) {
            modifier = KeyboardHelper.Modifier.LEFT_SHIFT;
        }
        if (groupKeyBean.isAlt()) {
            modifier = KeyboardHelper.Modifier.LEFT_ALT;
        }
        if (groupKeyBean.isCommand()) {
            modifier = KeyboardHelper.Modifier.LEFT_GUI;
        }
        if (groupKeyBean.isCtrl()) {
            modifier = KeyboardHelper.Modifier.LEFT_CTRL;
        }
        KeyboardMouseManager.getInstance().enterKeyCombination(modifier, groupKeyBean.getKey());
    }

    /**
     * 获取keyCode
     */
    public static int getKeyCode(String key) {
        try {
            return KeyEnum.valueOf(key.toUpperCase()).getKeyCode();
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    ///////////////////////////////////////////////////// hid /////////////////////////////////////////////////////

    /**
     * 回到oneAction/webApp
     */
    public static ResponseResult returnToOneActionWebAppExt(boolean isWeb, String webAppLink, String traceId) throws InterruptedException {
        if (isWeb) {
            return openBrowserExt(webAppLink, traceId);
        } else {
            return userSearchOpenAppByNameExt(SEARCH_ONE_ACTION, traceId);
//            return VoiceInjectionManager.openOneActionApp(traceId);
        }
    }

    /**
     * 打开浏览器
     */
    public static ResponseResult openBrowserExt(String url, String traceId) throws InterruptedException {
        if (TextUtils.isEmpty(url)) {
            return new ResponseResult(RESPONSE_CODE_FAIL, "打开链接失败，url为空");
        }
        MouseLocations mouseLocations = getMouseLocations();
        if (mouseLocations == null) {
            LogUtils.e("openBrowserExt，mouseLocations is null");
            return new ResponseResult(RESPONSE_CODE_FAIL, "openBrowserExt，mouseLocations is null");
        }

        //VoiceInjectionManager.openBrowser(traceId); //语音打开浏览器
        userSearchOpenAppByNameExt(SEARCH_SAFARI, traceId);//打开浏览器
        Thread.sleep(SLEEP_TIME_1000);

        moveToTapExt(mouseLocations.browserSearchAddressLocation);//移动到地址栏
//        if (!VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, 3000, () -> {
//            moveToTapExt(mouseLocations.browserSearchAddressLocation);
//        }, BROWSER_ADDRESS_TAB)) {
//            return new ResponseResult(RESPONSE_CODE_FAIL, "openBrowserExt，open browser fail,find address fail");
//        }


        ExtKeyboardMouseManager.getInstance().leftClick();//点击进入地址栏
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().allSelect();//全选
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().onBackSpaceKey();//删除
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().inputString(url);//input（输入地址）
        Thread.sleep(SLEEP_TIME_2000);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();//enter（访问地址）
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();//enter（访问地址）
        Thread.sleep(SLEEP_TIME_500);
//        moveToTapExt(mouseLocations.browserSearchAddressLocation);//移动到地址栏（有时候焦点会消失，或者乱跑，确认让焦点待在地址栏上）
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "openBrowserExt，open browser success");

    }

    /**
     * 打开应用（使用快捷键进入搜索页面进行打开）
     * 可直接打开：Settings/Shortcuts/Safari/OneAction/Accessibility/Full keyboard access
     * 进去得确认才能打开：Full keyboard access / AssistiveTouch /Always Show Menu /
     */
    public static ResponseResult userSearchOpenAppByNameExt(String name, String traceId) throws InterruptedException {
        ExtKeyboardMouseManager.getInstance().pressHomeKey();//回到首页,有时候一次回到的不是home
        Thread.sleep(SLEEP_TIME_2000);
        ExtKeyboardMouseManager.getInstance().openSearchPage();//打开搜索页面
        Thread.sleep(SLEEP_TIME_3000);
//        ShortcutUtil.switchEnglishInputMethodExt(traceId);//切换英文输入法(要开启旁白)
//        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().allSelect();//全选
        ExtKeyboardMouseManager.getInstance().inputString(name);
        Thread.sleep(SLEEP_TIME_3000);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();
        String phoneModel = getMouseLocations().phoneModel;
        if (IPhoneModelEnum.IPHONE_MODEL_15_PRO.getModelName().equals(phoneModel)) {
            Thread.sleep(SLEEP_TIME_500);
            ExtKeyboardMouseManager.getInstance().pressEnterKey();//有可能是中文，多点一次
        }
        Thread.sleep(SLEEP_TIME_500);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "执行成功");
    }

    /**
     * 打开设置某个页面（使用快捷键进入搜索页面进行打开）
     * 可直接打开：Accessibility/Full keyboard access
     * 进去得确认才能打开：Full keyboard access / AssistiveTouch /Always Show Menu /
     */
    public static ResponseResult userSearchOpenSettingPageByNameExt(String name, String traceId) throws InterruptedException {
        userSearchOpenAppByNameExt(SEARCH_SETTINGS, traceId);
        Thread.sleep(SLEEP_TIME_1000);
        callBackMainPageExt();
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//点击搜索
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().allSelect();//全选
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().inputString(name);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();
        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().pressDownKey();
        Thread.sleep(SLEEP_TIME_1500);
        ExtKeyboardMouseManager.getInstance().onSpaceKey();//进入
        Thread.sleep(SLEEP_TIME_2000);
        return new ResponseResult(RESPONSE_CODE_SUCCESS, "执行成功");
    }


    /**
     * 打开辅助触控设置页面
     */
    public static void openAssistiveTouchSettingsExt(boolean isVoice, String traceId) throws InterruptedException {
        if (isVoice) {
            VoiceInjectionManager.openAssistiveTouchSettings(traceId);
        } else {
//            userSearchOpenAppByNameExt(SEARCH_ALWAYS_SHOW_MENU, traceId);
            String phoneModel = mouseLocations.phoneModel;
            if (IPhoneModelEnum.IPHONE_MODEL_16.getModelName().equals(phoneModel) ||
                    IPhoneModelEnum.IPHONE_MODEL_16_PLUS.getModelName().equals(phoneModel) ||
                    IPhoneModelEnum.IPHONE_MODEL_16_PRO.getModelName().equals(phoneModel) ||
                    IPhoneModelEnum.IPHONE_MODEL_16_PRO_MAX.getModelName().equals(phoneModel)
            ) {
                userSearchOpenSettingPageByNameExt(SEARCH_ALWAYS_SHOW_MENU, traceId);
            } else {
                //iphone 15 pro
                userSearchOpenSettingPageByNameExt(SEARCH_ALWAYS_SHOW_MENU, traceId);
                ExtKeyboardMouseManager.getInstance().pressDownKey();
                Thread.sleep(SLEEP_TIME_500);
                ExtKeyboardMouseManager.getInstance().onSpaceKey();//进入
            }
        }
    }

    /**
     * 打开快捷指令页面
     */
    public static void openShortcutExt(boolean isVoice, String traceId) throws InterruptedException {
        if (isVoice) {
            VoiceInjectionManager.openShortcut(traceId);
        } else {
            userSearchOpenAppByNameExt(SEARCH_SHORTCUTS, traceId);
        }
    }

    /**
     * 打开设置页面 Settings
     */
    public static void openSettingsExt(boolean isVoice, String traceId) throws InterruptedException {
        if (isVoice) {
            VoiceInjectionManager.openSettings(traceId);
        } else {
            userSearchOpenAppByNameExt(SEARCH_SETTINGS, traceId);
        }
    }

    /**
     * 打开iPhone浏览器
     */
    public static void openBrowserExt(boolean isVoice, String traceId) throws InterruptedException {
        if (isVoice) {
            VoiceInjectionManager.openBrowser(traceId);
        } else {
            userSearchOpenAppByNameExt(SEARCH_SAFARI, traceId);
        }
    }


    /**
     * 返回应用主页
     */
    public static void callBackMainPageExt() throws InterruptedException {
        Thread.sleep(SLEEP_TIME_2000);//焦点可能丢失，等待2s
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//有时候一进来焦点在输入框里，需要先回车切换焦点才行，要不然返回会失效
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().pressEnterKey();//有时候一进来焦点在输入框里，需要先回车切换焦点才行，要不然返回会失效
        Thread.sleep(SLEEP_TIME_500);
        for (int i = 0; i < 10; i++) {
            ExtKeyboardMouseManager.getInstance().onEscKey();
            Thread.sleep(SLEEP_TIME_500);
        }
        ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//防止焦点乱跑
        Thread.sleep(SLEEP_TIME_500);
    }


    //是否切换过英文输入法,切换过不再切换
    private static boolean isSwitchEnglishInputMethodExt = false;

    /**
     * 切换英文输入发（必须在输入框激活后切换） 切换一次就够
     */
    public static boolean switchEnglishInputMethodExt(String traceId) throws InterruptedException {
        if (isSwitchEnglishInputMethodExt) {
            return false;
        }
        for (int i = 0; i < 6; i++) {//切换6次，直到切换到英文为止
            if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_3000, () -> ExtKeyboardMouseManager.getInstance().switchInputMethod(), KEYWORDS_SWITCH_INPUTMETHOD_ENGLISH)) {
                isSwitchEnglishInputMethodExt = true;
                return true;
            }
            Thread.sleep(SLEEP_TIME_1000);
        }
        return false;
    }


    /**
     * 根据鼠标移动到指定位置 旁白验证是否为要找的元素(前提打开旁白)
     *
     * @param traceId   traceId
     * @param x         x坐标
     * @param y         y坐标
     * @param sleepTime 移动后休眠多久
     * @param keyword   关键字
     * @return 是否是要找的元素
     */
    public boolean findElementByMouseExt(String traceId, int x, int y, long sleepTime, String keyword) {
        ExtKeyboardMouseManager.getInstance().tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> ExtKeyboardMouseManager.getInstance().leftClick(), keyword);
    }


    /**
     * 根据键盘查找某个元素并验证
     *
     * @param traceId            traceId
     * @param keyword            关键字
     * @param isDownFind         是否往下查找
     * @param resetMoveTimes     重置移动次数
     * @param isAsideMode        是否是旁白模式
     * @param isSearchByCategory 是否是分类查查找
     * @param times              查找次数
     * @param keyword            关键字
     * @return 是否是要找的元素
     */
    public static boolean findElementByKeyboardExt(String traceId, boolean isDownFind, int resetMoveTimes, int times, boolean isAsideMode, boolean isSearchByCategory, String keyword) throws InterruptedException {
        //旁白模式有时候会丢失焦点，需要鼠标先移动一下，先找到焦点，然后操作
        if (isAsideMode) {
            ExtKeyboardMouseManager.getInstance().resetMousePositionTopLeft();
            ExtKeyboardMouseManager.getInstance().tap(400, 400);
        } else {
            //todo 非旁白模式下也可能丢失焦点，还不确定怎么恢复焦点
        }
        //重置元素位置
        if (resetMoveTimes > 0) {
            if (isDownFind) {//往下查找，需要滑动到最上面
                if (isAsideMode) {//旁白模式
                    if (isSearchByCategory) {//分类查找
                        ShortcutUtil.pressUpKeyExt(resetMoveTimes);//往上移动
                    } else {//普通查找
                        ShortcutUtil.pressLeftKeyExt(resetMoveTimes);//往上移动
                    }
                } else {//非旁白模式
                    ShortcutUtil.pressUpKeyExt(resetMoveTimes);//往上移动
                }
            } else {//往上查找，需要滑动到最下面
                if (isAsideMode) {//旁白模式
                    if (isSearchByCategory) {//分类查找
                        ShortcutUtil.pressDownKeyExt(resetMoveTimes);//往下移动
                    } else {//普通查找
                        ShortcutUtil.pressRightKeyExt(resetMoveTimes);//往下移动
                    }
                } else {//非旁白模式
                    ShortcutUtil.pressDownKeyExt(resetMoveTimes);//往下移动
                }
            }
        }
        //开始查找元素
        for (int i = 0; i < times; i++) {
            boolean verificationResult = VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout(traceId, TIME_5000, () -> {
                if (isDownFind) {//往下查找
                    if (isAsideMode) {//旁白模式
                        if (isSearchByCategory) {//分类查找
                            ShortcutUtil.pressDownKeyExt(resetMoveTimes);//往下移动
                        } else {//普通查找
                            ShortcutUtil.pressRightKeyExt(resetMoveTimes);//往下移动
                        }
                    } else {//非旁白模式
                        KeyboardMouseManager.getInstance().pressDownKey();
                    }
                } else {//往上查找
                    if (isAsideMode) {//旁白模式
                        if (isSearchByCategory) {//分类查找
                            ShortcutUtil.pressUpKeyExt(resetMoveTimes);//往上移动
                        } else {//普通查找
                            ShortcutUtil.pressRightKeyExt(resetMoveTimes);//往下移动
                        }
                    } else {//非旁白模式
                        ExtKeyboardMouseManager.getInstance().pressUpKey();
                    }
                }
            }, keyword);
            if (verificationResult) {
                return true;
            }
        }
        return false;
    }

    /**
     * 按下上键（非旁白）
     */
    public static void pressUpKeyExt(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            ExtKeyboardMouseManager.getInstance().pressUpKey();
            Thread.sleep(SLEEP_TIME_500);
        }
    }

    /**
     * 按下下键（非旁白）
     */
    public static void pressDownKeyExt(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            ExtKeyboardMouseManager.getInstance().pressDownKey();
            Thread.sleep(SLEEP_TIME_500);
        }
    }

    /**
     * 按下左键（非旁白）
     */
    public static void pressLeftKeyExt(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            ExtKeyboardMouseManager.getInstance().pressLeftKey();
            Thread.sleep(SLEEP_TIME_500);
        }
    }

    /**
     * 按下右键（非旁白）
     */
    public static void pressRightKeyExt(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            ExtKeyboardMouseManager.getInstance().pressRightKey();
            Thread.sleep(SLEEP_TIME_500);
        }
    }


    /**
     * 按下快捷键(ext)
     */
    public static void sendGroupKeyExt(GroupKeyBean groupKeyBean) {
        byte result = (byte) ((groupKeyBean.isShift() ? Constants.LEFT_SHIFT : 0) | (groupKeyBean.isCtrl() ? Constants.LEFT_CTRL : 0) | (groupKeyBean.isAlt() ? Constants.LEFT_ALT : 0) | (groupKeyBean.isCommand() ? Constants.LEFT_GUI : 0));
        ExtKeyboardMouseManager.getInstance().sendModifierKeyboardEvent(result, groupKeyBean.getKey());
    }

    /**
     * 滚动到页面顶部
     */
    public static void scrollPageTopExt() throws InterruptedException {
        ExtKeyboardMouseManager.getInstance().resetMousePositionTopLeft();//左上角
//        ExtKeyboardMouseManager.getInstance().resetMousePositionTopRight();
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().tap(1, 1);
        ExtKeyboardMouseManager.getInstance().leftDoubleClick();//回到设置首页最上面
        Thread.sleep(SLEEP_TIME_500);
    }

    /**
     * 移动到指定位置
     */
    public static void moveToTapExt(Location location) throws InterruptedException {
        if (location == null) {
            return;
        }
        switch (location.getCorner()) {
            case 1:
                ExtKeyboardMouseManager.getInstance().resetMousePositionTopLeft();//左上角
                break;
            case 2:
                ExtKeyboardMouseManager.getInstance().resetMousePositionTopRight();//右上角
                break;
            case 3:
                ExtKeyboardMouseManager.getInstance().resetMousePositionBottomLeft();//左下角
                break;
            case 4:
                ExtKeyboardMouseManager.getInstance().resetMousePositionBottomRight();//右下角
                break;
            default:
                return;
        }
        Thread.sleep(SLEEP_TIME_1000);
        ExtKeyboardMouseManager.getInstance().tap(location.getX(), location.getY());
        Thread.sleep(SLEEP_TIME_500);
    }

    /**
     * 移动到指定位置并点击
     */
    public static void moveToTapAndClickExt(Location location) throws InterruptedException {
        moveToTapExt(location);
        Thread.sleep(SLEEP_TIME_500);
        ExtKeyboardMouseManager.getInstance().leftDoubleClick();
        Thread.sleep(SLEEP_TIME_500);
    }

    /**
     * 关闭网络请求弹窗
     */
    public static boolean closeNetworkAlertAndAllowExt() throws InterruptedException {
        //网络请求弹窗检测，并允许
        if (VoiceAsideVerificationManager.getInstance().voiceAsideVerificationWithTimeout("", TIME_3000, () -> {
            ExtKeyboardMouseManager.getInstance().pressModifierThenKey(Constants.RIGHT_CTRL, Constants.DOWN);
        }, NETWORK_ALERT_DO_NOT_ALLOW_BUTTON)) {
            LogUtils.d("closeNetworkAlertExt", "closeNetworkAlertExt，find don't allow button");
            ExtKeyboardMouseManager.getInstance().pressModifierThenKeyNew(Constants.TAB, Constants.LEFT);//移动到  wlan & mobile 处
            Thread.sleep(SLEEP_TIME_500);
            LogUtils.d("closeNetworkAlertExt", "closeNetworkAlertExt，find wlan & mobile button，click");
            ExtKeyboardMouseManager.getInstance().onSpaceKey();//wlan & mobile button，click
//            Thread.sleep(SLEEP_TIME_1000);
//            LogUtils.d("closeNetworkAlertExt", "closeNetworkAlertExt，click refresh button");
//            ExtKeyboardMouseManager.getInstance().onSpaceKey();//click refresh button
            return true;
        } else {
            return false;
        }
    }


    private static MouseLocations mouseLocations = new MouseLocations();

    /**
     * 第一次获取鼠标位置
     */
    public static MouseLocations setMouseLocations(String phoneMode, String version) {
        mouseLocations = AssetsUtil.getMouseLocations(phoneMode, version);
        mouseLocations.phoneModel = phoneMode;
        return mouseLocations;
    }

    /**
     * 获取鼠标位置
     */
    public static MouseLocations getMouseLocations() {
        return mouseLocations;
    }

}
