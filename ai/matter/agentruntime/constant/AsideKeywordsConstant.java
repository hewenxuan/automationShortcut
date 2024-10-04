package ai.matter.agentruntime.constant;

/**
 * 旁白关键字
 */
public interface AsideKeywordsConstant {

    int TIME_2000 = 2000;
    int TIME_3000 = 3000;
    int TIME_5000 = 5000;
    int TIME_10000 = 10000;
    int TIME_15000 = 15000;

    String NETWORK_ALERT_DO_NOT_ALLOW_BUTTON="don't allow button";//网络请求弹窗里的最下面按钮

    //OK, voice alert important. 提示旁白会更改iphone手势确认取消弹窗，
    //Voice over alert Voice over would like to send you notification. 旁白请求通知的弹窗，
    //alert voice over changes the gestures used to control your iphone. double tap to start the voice over tutorial. 旁白新手教程弹窗
    String ALERT="alert";
    String VOICE="voice";

    //切换输入法-英文
    String KEYWORDS_SWITCH_INPUTMETHOD_ENGLISH = "english";
    //浏览器地址栏
    String BROWSER_ADDRESS_TAB = "address";

    //********************打开全键盘*********************************
    //设置界面搜索框
    String KEYWORDS_FULL_KEYBOARD_SETTING_SEARCH_EDITTEXT = "search field";
    //全键盘开关关闭 full keyboard access Switch button off      Full keyboard access. Switch button off. Double tap to toggle setting    access Switch button off
    String KEYWORDS_FULL_KEYBOARD_ACCESS_OFF = "button off";
    String KEYWORDS_FULL_KEYBOARD_ACCESS = "keyboard access";//full keyboard access
    //全键盘隐藏开关item
    String KEYWORDS_FULL_KEYBOARD_ACCESS_AUTO_HIDE_OFF = "auto hide off button";

    //********************辅助触控设置*********************************
    //辅助触控设置页面左上角touch按钮
    String KEYWORDS_ASSISTIVE_TOUCH_PAGE_TOP_TOUCH_BACK_BUTTON = "touch back button";
    //辅助触控菜单按钮开关
    String KEYWORDS_ASSISTIVE_TOUCH_ALWAYS_SHOW_MENU_OFF = "show menu switch button off";

    //********************安装快捷指令*********************************
    //浏览器最上面打开快捷指令button
    String KEYWORDS_INSTALL_SHORTCUT_BROWSER_OPEN_SHORTCUT_BUTTON = "shortcuts open in the shortcuts app open button";
    //快捷指令页面添加快捷指令按钮
    String KEYWORDS_INSTALL_SHORTCUT_ADD_SHORTCUT_BUTTON = "add shortcut button";
    //验证是否安装成功（安装可能重复，监听重复的按钮）点击完成时候可能会有语音提示重复
    String KEYWORDS_INSTALL_SHORTCUT_VERIFICATION = "You already have a shortcut named";
    ///验证是否安装成功（安装可能重复，监听重复的按钮）替换按钮
    String KEYWORDS_INSTALL_SHORTCUT_REPLACE_BUTTON_VERIFICATION = "replace button";
    String KEYWORDS_INSTALL_SHORTCUT_CLOSE_SHORTCUTS_BUTTON = "close Shortcuts button";//第一次进入快捷指令页面的引导页面的最下面关闭快捷指令按钮

    //********************设置快捷指令快捷键************************************
    String KEYWORDS_JARVIS = "jarvis";//ble和外挂蓝牙名称
    String KEYWORDS_BLUETOOTH_DEVICES  = "bluetooth devices";//找ble结束标志 Bluetooth Devices Button
    String KEYWORDS_HAND = "hand";//外挂蓝牙携带
    String KEYWORDS_ERROR = "error";
    String KEYWORDS_SHORTCUTS_HEADING = "heading";//快捷指令key设置最后一步上面的结束标志 shortcuts, heading
    //********************设置快捷指令ID****************************************
    //设置快捷指令id-搜索结果结果（只验证后面，不验证快捷指令名称）
    String KEYWORDS_SET_SHORTCUT_ID_SEARCH_RESULT_ONE_BUTTON = "access have a double";
    //设置快捷指令id-编辑快捷指令页面里面的-text文本框
    String KEYWORDS_SET_SHORTCUT_ID_TEXT_FIELD = "text field";
    //设置快捷指令id-编辑快捷指令页面里面的-右上角完成按钮
    String KEYWORDS_SET_SHORTCUT_ID_DONE_BUTTON = "done button";

    //********************创建快捷指令文件夹*************************************

    //创建文件夹点击添加时候显示弹窗，名字重复 Add, a name cannot be used, OK, button
    String KEYWORDS_CREATE_SHORTCUT_FOLDER_NAME_CON_NOT_BE_USED = "name cannot be used";

    //********************删除快捷指令******************************************

    //********************更新快捷指令******************************************

    //********************删除快捷指令******************************************

    //********************执行/验证快捷指令******************************************
    //运行快捷指令有弹窗然后点击允许按钮
    String KEYWORDS_allow_BUTTON = "allow button";

    //********************Imessage配置*****************************************
    //快捷指令首页下面tab，中间的自动化按钮
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_BUTTON = "automation tab";
    //自动化页面中间的新建自动化按钮
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_NEW_AUTOMATION_BUTTON = "automation button";
    //自动化页面右上角的添加自动化按钮
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_ADD_BUTTON = "add button";
    //自动化页面新建自动化页面的搜索出的message的位置 Message. X. When I get a message from mom. Button.
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_FIND_MESSAGE_ITEM = "message When I get a message from mom Button";
    //搜索框 search field double tap to edit
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_FIND_EDIT_SEARCH = "search field";
    //搜索出来的message的第1个 when I get a message from
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_FIND_EDIT_SEARCH_ITEM_MESSAGE = "message";
    //自动化页面-选择联系人 完成按钮
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_DOWN_BUTTON = "done button";

    //自动化页面-选择快捷指令页面-我的快捷指令按钮
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_MY_SHORTCUTS_BUTTON = "my shortcuts button";
    //最后一步，搜索出来的快捷指令位置验证 select add matter push message button
    String KEYWORDS_IMESSAGE_CONFIG_SELECT_ADD_BUTTON = "button";

    //********************Imessage更新联系人************************************
    //快捷指令首页下面tab，左边的shortcuts按钮
    String KEYWORDS_IMESSAGE_CONFIG_AUTO_SHORTCUTS_TAB = "shortcuts tab";


}
