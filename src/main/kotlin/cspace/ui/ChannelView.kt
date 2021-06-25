package cspace.ui

import cspace.ApplicationStarter
import cspace.dialog.ChannelOptionDialog
import cspace.util.AssetsResolver
import cspace.util.JComponentInitializer
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.border.TitledBorder

/**
 * 分析通道视图类
 * @author huobn
 */
class ChannelView: JPanel() {

    // 新建分析通道按钮
    private val newChannelButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.new-channel.icon")))
        button.toolTipText = "new channel"
        button.isOpaque = false
        button.addActionListener {
            val dialog = ApplicationStarter.getContext().getInstance(ChannelOptionDialog::class.java)
            JComponentInitializer.showDialog(dialog)
        }
        button
    }

    private val executeButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.execute.icon")))
        button.toolTipText = "execute"
        button.isOpaque = false
        button
    }

    private val settingButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.setting.icon")))
        button.toolTipText = "setting"
        button.isOpaque = false
        button
    }


    private val deleteButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.delete.icon")))
        button.toolTipText = "delete"
        button.isOpaque = false
        button
    }

    // 分析通道功能栏
    private val functionToolBar: JToolBar by lazy {
        val toolBar = JToolBar()
        toolBar.layout = FlowLayout().apply {
            alignment = FlowLayout.CENTER
        }
        toolBar.preferredSize = Dimension(65, 200)
        toolBar.size = Dimension(65, 200)
        toolBar.orientation = JToolBar.VERTICAL
        toolBar.isFloatable = false
        val box = Box.createVerticalBox()
        box.add(newChannelButton)
        box.add(Box.createVerticalStrut(10))
        box.add(settingButton)
        box.add(Box.createVerticalStrut(10))
        box.add(executeButton)
        box.add(Box.createVerticalStrut(10))
        box.add(deleteButton)
        toolBar.add(box)
        toolBar
    }


    private val channelTabPane: JTabbedPane by lazy {
        val tabPane = JTabbedPane()
        tabPane.border = TitledBorder("channel")
        tabPane
    }

    init {
        layout = BorderLayout()
        size = Dimension(1000, 200)
        preferredSize = Dimension(1000, 200)
        add(functionToolBar, BorderLayout.WEST)
        add(channelTabPane, BorderLayout.CENTER)
    }
}