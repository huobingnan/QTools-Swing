package cspace.component

import cspace.ApplicationStarter
import cspace.dialog.ChannelOptionDialog
import cspace.dialog.ChannelOptionNewExtSettingDialog
import cspace.util.JComponentInitializer
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
        item.addActionListener {
            // 添加设置按钮点击触发的事件
            val dialog = ApplicationStarter.getContext().getInstance(ChannelOptionNewExtSettingDialog::class.java)
            JComponentInitializer.showDialogSupport(dialog)
            if (!dialog.isExitOnApprove())return@addActionListener
            val settingPair = dialog.getSettingPair()
            val channelOptionDialog = ApplicationStarter.getContext().getInstance(ChannelOptionDialog::class.java)
            channelOptionDialog.acceptExtChannelSettingPair(settingPair)
        }
        item
    }

    // 删除设置按钮
    private val deleteSettingMenu: JMenuItem by lazy {
        val item = JMenuItem("delete")
        item.addActionListener {
            val channelOptionDialog = ApplicationStarter.getContext().getInstance(ChannelOptionDialog::class.java)
            channelOptionDialog.deleteSelectedExtSetting()
        }
        item
    }

    init {
        add(newSettingMenu)
        add(deleteSettingMenu)
    }
}