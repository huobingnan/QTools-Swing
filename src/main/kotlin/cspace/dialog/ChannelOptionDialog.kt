package cspace.dialog

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*
import javax.swing.border.TitledBorder

/**
 * 通道选项对话框
 * @author huobn
 */
class ChannelOptionDialog: JDialog() {

    private val nameTextField: JTextField by lazy {
        val textField = JTextField()
        textField.preferredSize = Dimension(200, 35)
        textField.size = Dimension(200, 35)
        textField
    }

    private val basicSettingPane: JPanel by lazy {
        val pane = JPanel()
        pane.size = Dimension(500, 240)
        pane.preferredSize = Dimension(500, 240)
        pane.border = TitledBorder("basic settings")
        pane.layout = GridLayout(1, 2)
        pane.add(JLabel("channel name:"))
        pane.add(nameTextField)
        pane
    }

    private val extSettingPane: JPanel by lazy {
        val pane = JPanel()
        pane.size = Dimension(500, 200)
        pane.layout = BorderLayout()
        pane.border = TitledBorder("additional setting")
        pane.preferredSize = Dimension(500, 200)
        pane
    }

    init {
        size = Dimension(500, 600)
        preferredSize = Dimension(500, 600)
        isModal = true
        isVisible = false
        title = "channel options"
        val box = Box.createVerticalBox()
        box.add(Box.createVerticalStrut(10))
        box.add(basicSettingPane)
        box.add(Box.createVerticalStrut(10))
        box.add(extSettingPane)
        contentPane = box
    }
}