package ai.matter.agentruntime.manager;

import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ai.matter.agentruntime.executor.ExtCrdRpaExecutor;
import ai.matter.extbt.profile.Constants;
import ai.matter.extbt.profile.IctHidEventListener;

/**
 * 键鼠操作管理类
 */
public class ExtKeyboardMouseManager {
    private static final String TAG = "Shortcut ExtKeyboardMouseManager ";

    private static class SingletonHolder {
        private static final ExtKeyboardMouseManager INSTANCE = new ExtKeyboardMouseManager();
    }

    public static ExtKeyboardMouseManager getInstance() {
        return ExtKeyboardMouseManager.SingletonHolder.INSTANCE;
    }

    public ExtCrdRpaExecutor getCrdRpaExecutor() {
        return ExtCrdRpaExecutor.getInstance();
    }

    private boolean isConnectHid = false;
    /**
     * 连接方法
     */
    public boolean connect() {
//        return getCrdRpaExecutor().connectHid();
        if(isConnect()){
            Log.d(TAG, "Bluetooth is already connected, no need to connect again");
            return true;
        }
        //创建CountDownLatch实例，用于同步等待连接结果
        CountDownLatch latch = new CountDownLatch(1);
        getCrdRpaExecutor().connectHid(new IctHidEventListener() {
            @Override
            public void onConnected() {
                Log.d(TAG, "Bluetooth connection successful");
                // 连接成功，设置连接状态为true，连接中状态为false
                isConnectHid = true;
                // 计数减1
                latch.countDown();
            }

            @Override
            public void onDisConnected() {
                Log.e(TAG, "Bluetooth has been disconnected");
                isConnectHid = false;
                // 计数减1
                latch.countDown();
            }
        });
        try {
            // 等待连接结果，如果超时则设置连接状态为false，连接中状态为false
            boolean success = latch.await(2, TimeUnit.SECONDS);
            if (!success) {
                isConnectHid = false;
            }
//            latch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "Bluetooth connection exception；" + e.getMessage());
            e.printStackTrace();
            isConnectHid = false;
        }
        return isConnectHid;
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        getCrdRpaExecutor().disconnectHid();
    }


    /**
     * 是否已连接
     */
    public boolean isConnect() {
//        return getCrdRpaExecutor().isConnectHid();
        return getCrdRpaExecutor().isHidConnected();
    }

    /**
     * 重置鼠标位置到左上角
     */
    public void resetMousePositionTopLeft() {
        Log.d(TAG, "resetMousePositionTopLeft");
        getCrdRpaExecutor().resetMousePositionTopLeft();
    }

    /**
     * 重置鼠标位置到右上角
     */
    public void resetMousePositionTopRight() {
        Log.d(TAG, "resetMousePositionTopRight");
        getCrdRpaExecutor().resetMousePositionTopRight();
    }

    /**
     * 重置鼠标位置到左下角
     */
    public void resetMousePositionBottomLeft() {
        Log.d(TAG, "resetMousePositionBottomLeft");
        getCrdRpaExecutor().resetMousePositionBottomLeft();
    }

    /**
     * 重置鼠标位置到右下角
     */
    public void resetMousePositionBottomRight() {
        Log.d(TAG, "resetMousePositionBottomRight");
        getCrdRpaExecutor().resetMousePositionBottomRight();
    }

    /**
     * 单击
     */
    public void leftClick() {
        Log.d(TAG, "leftClick");
        getCrdRpaExecutor().leftClick();
    }

    /**
     * 类似右键单击 todo
     */
    public void rightClick() {
        Log.d(TAG, "rightClick");
        getCrdRpaExecutor().rightClick();
    }

    /**
     * 长按
     */
    public void leftClickLongPress(int time) {
        Log.d(TAG, "leftClickLongPress");
        getCrdRpaExecutor().leftClickLongPress(time);
    }

    /**
     * 左键双击
     */
    public void leftDoubleClick() {
        Log.d(TAG, "leftClick");
        getCrdRpaExecutor().leftDoubleClick();
    }

    /**
     * 移动到指定坐标位置
     *
     * @param x 屏幕x轴坐标
     * @param y 屏幕y轴坐标
     */
    public void tap(int x, int y) {
        Log.d(TAG, "tap x: " + x + ", y: " + y);
        getCrdRpaExecutor().tap(x, y);
    }

    /**
     * 锁屏
     */
    public void lockIos() {
        Log.d(TAG, "lock");
        getCrdRpaExecutor().lockIos();
    }

    /**
     * 锁屏
     */
    public void lockAndroid() {
        Log.d(TAG, "lockAndroid");
        getCrdRpaExecutor().lockAndroid();
    }

    /**
     * 解锁
     *
     * @param pwd 密码
     */
    public void unlockIOS(String pwd) {
        Log.d(TAG, "unlock: " + pwd);
        getCrdRpaExecutor().unlockIOS(pwd);
    }

    /**
     * 解锁
     *
     * @param pwd 密码
     */
    public void unlockIOSNoActivation(String pwd) {
        Log.d(TAG, "unlock: " + pwd);
        getCrdRpaExecutor().unlockIOSNoActivation(pwd);
    }

    /**
     * 解锁
     *
     * @param pwd 密码
     */
    public void unlockAndroid(String pwd) {
        Log.d(TAG, "unlock: " + pwd);
        getCrdRpaExecutor().unlockAndroid(pwd);
    }


    /**
     * 单键
     */
    public void enterKeyCombination(char key) {
        Log.d(TAG, "enterKeyCombination: " + key);
        getCrdRpaExecutor().sendModifierKeyboardEvent((byte) 0, key);
    }

    /**
     * 单键
     */
    public void enterKeyCombination(byte key) {
        Log.d(TAG, "enterKeyCombination: " + key);
        getCrdRpaExecutor().enterKeyCombination(key);
    }


    /**
     * 组合键
     *
     * @param modifier
     * @param key
     */
    public void sendModifierKeyboardEvent(byte modifier, char key) {
        Log.d(TAG, "enterKeyCombination: " + modifier + ", " + key);
        getCrdRpaExecutor().sendModifierKeyboardEvent(modifier, key);
    }

    /**
     * 组合键
     *
     * @param modifier
     * @param key
     */
    public void sendModifierKeyboardEvent(byte modifier, byte key) {
        Log.d(TAG, "enterKeyCombination: " + modifier + ", " + key);
        getCrdRpaExecutor().sendModifierKeyboardEvent(modifier, key);
    }


    /**
     * 按下某个键再按下另一个键
     *
     * @param key  第一个键 Constants.FunctionKeyMap
     * @param key2 第二个键 Constants.keyMap
     */
    public void pressModifierThenKey(byte key, char key2) {
        Log.d(TAG, "pressModifierThenKey: " + ", " + key + ", " + key2);
        getCrdRpaExecutor().pressModifierThenKey(key, key2);
    }

    /**
     * 按下某个键再按下另一个键
     *
     * @param key  第一个键 Constants.FunctionKeyMap
     * @param key2 第二个键 Constants.keyMap
     */
    public void pressModifierThenKey(byte key, byte key2) {
        Log.d(TAG, "pressModifierThenKey: " + ", " + key + ", " + key2);
        getCrdRpaExecutor().pressModifierThenKey(key, key2);
    }

    /**
     * 按下某个键再按下另一个键
     *
     * @param key  第一个键 非Constants.FunctionKeyMap
     * @param key2 第二个键 Constants.keyMap
     */
    public void pressModifierThenKeyNew(byte key, byte key2) {
        Log.d(TAG, "pressModifierThenKey: " + ", " + key + ", " + key2);
        getCrdRpaExecutor().pressModifierThenKeyNew(key, key2);
    }

    /**
     * 输入文字
     */
    public void inputString(String input) {
        Log.d(TAG, "inputString: " + input);
        getCrdRpaExecutor().inputString(input);
    }

    /**
     * 滚动 负数向上滑动，正数向下滑动 todo 待验证
     */
    public void wheelMove(int what) {
        Log.d(TAG, "wheelMove: " + what);
//        getCrdRpaExecutor().wheelMove(what);
    }

    /**
     * 移动到指定位置并点击
     *
     * @param x
     * @param y
     */
    public void moveAndClick(int x, int y, long sleepTime) {
        tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftClick();
    }

    /**
     * 移动到指定位置并双进击
     *
     * @param x
     * @param y
     */
    public void moveAndDoubleClick(int x, int y, long sleepTime) {
        tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftDoubleClick();
    }

    /**
     * 移动到指定位置并长按
     *
     * @param x
     * @param y
     */
    public void moveAndClickLongPress(int x, int y, long sleepTime) {
        tap(x, y);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftClickLongPress(2000);
    }

    /**
     * 按下电源键
     */
    public void pressHomeKey() {
        Log.d(TAG, "pressHomeKey");
        sendModifierKeyboardEvent(Constants.LEFT_GUI, 'h');
    }

    /**
     * 切换输入法
     */
    public void switchInputMethod() {
        Log.d(TAG, "switchInputMethod");
        sendModifierKeyboardEvent(Constants.RIGHT_CTRL, ' ');
    }

    /**
     * 打开搜索页面 todo 待验证
     */
    public void openSearchPage() {
        Log.d(TAG, "openSearch");
        sendModifierKeyboardEvent(Constants.RIGHT_GUI, ' ');
    }


    /**
     * 粘贴
     */
    public void stickup() {
        Log.d(TAG, "stickup");
        sendModifierKeyboardEvent(Constants.LEFT_GUI, 'v');
    }

    /**
     * 全选
     */
    public void allSelect() {
        Log.d(TAG, "allSelect");
        sendModifierKeyboardEvent(Constants.LEFT_GUI, 'a');
    }

    /**
     * copy
     */
    public void copy() {
        Log.d(TAG, "allSelect");
        sendModifierKeyboardEvent(Constants.LEFT_GUI, 'c');
    }

    /**
     * 按下上键
     */
    public void pressUpKey() {
        Log.d(TAG, "pressUpKey");
        getCrdRpaExecutor().onUpKey();
    }

    /**
     * 按下下键
     */
    public void pressDownKey() {
        Log.d(TAG, "pressDownKey");
        getCrdRpaExecutor().onDownKey();
    }

    /**
     * 按下左键
     */
    public void pressLeftKey() {
        Log.d(TAG, "pressLeftKey");
        getCrdRpaExecutor().onLeftKey();
    }

    /**
     * 按下右键
     */
    public void pressRightKey() {
        Log.d(TAG, "pressRightKey");
        getCrdRpaExecutor().onRightKey();
    }

    /**
     * 按下删除键
     */
    public void onBackSpaceKey() {
        Log.d(TAG, "onBackSpaceKey");
        getCrdRpaExecutor().onBackSpaceKey();
    }


    /**
     * 空格键激活
     */
    public void onSpaceKey() {
        Log.d(TAG, "pressActivationSpaceKey");
        getCrdRpaExecutor().onSpaceKey();
    }

    /**
     * 按下回车按钮
     */
    public void pressEnterKey() {
        Log.d(TAG, "pressEnterKey");
        getCrdRpaExecutor().onEnterKey();
    }

    /**
     * 按下返回键
     */
    public void onEscKey() {
        Log.d(TAG, "pressBackKey");
//        pressModifierThenKey(Constants.TAB, 'b');
        getCrdRpaExecutor().onEscKey();
    }

    /**
     * 按下Tab键
     */
    public void onTabKey() {
        Log.d(TAG, "onTabKey");
//        pressModifierThenKey(Constants.TAB, 'b');
        getCrdRpaExecutor().onTabKey();
    }

}
