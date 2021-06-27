package cspace.component

import javax.swing.JMenuItem
import javax.swing.JPopupMenu

/**
 * 新建额外设置的弹出按钮
 * @author huobn
 */
class ExtSettingNewPopupMenu: JPopupMenu() {

    // 新建设置按钮
    private val newSettingMenu: JMenuItem by lazy {
        val item = JMenuItem("new")

        item
    }

    // 删除设置按钮
    private val deleteSettingMenu: JMenuItem by lazy {
        val item = JMenuItem("delete")
        item
    }

    init {
        add(newSettingMenu)
        add(deleteSettingMenu)
    }
}