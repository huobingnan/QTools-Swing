package cspace.dialog

import cspace.constants.ApplicationDialogConstants
import cspace.util.DialogSupport
import java.awt.Dimension
import java.awt.FlowLayout
import java.io.File
import javax.swing.*

/**
 * 多选导入的任务框
 * @author huobn
 */
class MultipleImportDialog: JDialog(), DialogSupport {


    private val closeButton: JButton by lazy {
        val button = JButton("close")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button
    }

    private val cancelButton: JButton by lazy {
        val button = JButton("cancel")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button
    }

    private val buttonGroup: JPanel by lazy {
        val pane = JPanel()
        pane.layout = FlowLayout().apply {
            hgap = 10
            vgap = 10
            alignment = FlowLayout.CENTER
        }
        pane.add(cancelButton)
        pane.add(closeButton)
        pane
    }

    private val resourceImportProgressList: JList<File> by lazy {
        val list = JList<File>()
        list.setCellRenderer { jList, importFile, i, b, b2 ->
            val label = JLabel()
            label
        }
        list
    }

    init {
        val windowSize = Dimension(350, 400)
        isModal = true
        size = windowSize
        preferredSize = windowSize
        isVisible = false
        val box = Box.createVerticalBox()
        box.add(JScrollPane(resourceImportProgressList))
        box.add(Box.createVerticalStrut(10))
        box.add(buttonGroup)
        box.add(Box.createVerticalStrut(10))
        contentPane = box

    }

    override fun isExitOnApprove(): Boolean {
        return false
    }

    override fun showDialog() {
        isVisible = true
    }

    override fun closeDialog() {
        isVisible = false
    }
}