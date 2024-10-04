package ai.matter.agentruntime.bean.location;

/**
 * 鼠标位置bean
 */
public class MouseLocations {

    public String phoneModel;//手机型号

    public Location allowButtonLocation;//运行快捷指令时，点击弹窗允许按钮位置
    public Location browserSearchAddressLocation;//浏览器-搜索地址栏
    public Location browserOpenShortcutButtonLocation;//浏览器-加载完快捷指令链接后-最上面添加快捷指令按钮
    public Location shortcutsAddShortcutButtonLocation;//安装快捷指令-从浏览器点击open过去-添加快捷指令按钮
    public Location shortcutsAddShortcutCancelButtonLocation;//浏览器-从浏览器点击open过去-快捷指令里面-取消按钮

    public Location shortcutsSetShortcutIdDoneButtonLocation;//安装快捷指令-编辑快捷指令页面-右上角完成按钮
    public Location shortcutsSetShortcutIdSearchShortcutFirstLocation;//设置快捷指令id-快捷指令页面搜索指定的快捷指令-搜索出来的第一个位置
    public Location settingSearchEditTextLocation;//设置-搜索地址栏
    public Location settingSearchResultListFirstItemLocation;//设置-搜索列表-第一项
    public Location settingFullKeyboardAccessFirstLocation;//设置-全键盘开关-第一次出现位置
    public Location settingFullKeyboardAccessButtonSecondLocation;//设置-全键盘开关-第二次出现位置，开关位置
    public Location shortcutsAutomationTabButtonLocation;//快捷指令-首页-下面自动化tab位置
    public Location shortcutsGalleryTabButtonLocation;//快捷指令-首页-下面Gallery tab位置
    public Location shortcutsAutomationAddAutomationButtonLocation;//快捷指令-新建自动化-右上角-添加自动化加号按钮
    public Location shortcutsAutomationCreateNewAutomationButtonLocation;//快捷指令-新建自动化-中间-新建自动化加号按钮
    public Location shortcutsAutomationSearchEdittextLocation;//快捷指令-新建自动化-搜索框
    public Location shortcutsAutomationSearchMessageItemLocation;//快捷指令-新建自动化-搜索出来的message第一个item位置
    public Location shortcutsAutomationContactDownButtonLocation;//快捷指令-新建自动化-信息-输入联系人页面-右上角-完成按钮(完成按钮后next按钮，同一个位置)
    public Location shortcutsAutomationSelectShortcutSecondItemLocation;//快捷指令-新建自动化-最后一步选择快捷指令页面-第二项位置（第一个快捷指令位置）

}
