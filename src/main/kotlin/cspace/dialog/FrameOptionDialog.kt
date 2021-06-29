package cspace.dialog

import cspace.ApplicationStarter
import cspace.constants.ApplicationDialogConstants
import cspace.model.AnalyseKeyFrame
import cspace.ui.ResourceBrowserView
import cspace.util.DialogSupport
import cspace.util.JComponentInitializer
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import java.lang.IllegalArgumentException
import javax.swing.*
import javax.swing.border.TitledBorder

/**
 * 关键帧设置对话框
 * @author huobn
 */
class FrameOptionDialog: JDialog(), DialogSupport {

    private val log = LoggerFactory.getLogger(FrameOptionDialog::class.java)

    private var exitOnApprove = false

    private val instanceFrame = AnalyseKeyFrame("undefined", "undefined")

    private val nameTextField: JTextField by lazy {
        val textField = JTextField()
        textField
    }

    private val resourceComboBox: JComboBox<String> by lazy {
        val comboBox = JComboBox<String>()
        comboBox
    }

    private val approveButton: JButton by lazy {
        val button = JButton("apply")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button.addActionListener {
            instanceFrame.name = nameTextField.text
            instanceFrame.resourceName = (resourceComboBox.selectedItem ?: "") as String
            try {
                AnalyseKeyFrame.validate(instanceFrame)
                exitOnApprove = true
                this@FrameOptionDialog.isVisible = false
            }catch (ex: IllegalArgumentException) {
                JOptionPane.showMessageDialog(this@FrameOptionDialog, ex.message,
                "invalid input", JOptionPane.ERROR_MESSAGE)
            }
        }
        button
    }

    private val cancelButton: JButton by lazy {
        val button = JButton("cancel")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button.addActionListener {
            exitOnApprove = false
            this@FrameOptionDialog.isVisible = false
        }
        button
    }

    private val buttonGroup: JPanel by lazy {
        val pane = JPanel()
        pane.layout = FlowLayout().apply {
            hgap = 10
            vgap = 10
            alignment = FlowLayout.RIGHT
        }
        pane.add(cancelButton)
        pane.add(approveButton)
        pane
    }

    private val settingPane: JPanel by lazy {
        val pane = JPanel()
        pane.layout = GridLayout(2,2, 10, 15)
        pane.border = TitledBorder("key frame")
        pane.add(JLabel("frame name:"))
        pane.add(nameTextField)
        pane.add(JLabel("resource:"))
        pane.add(resourceComboBox)
        pane
    }

    init {
        title = "frame option"
        val windowSize = Dimension(350, 200)
        size = windowSize
        preferredSize = windowSize
        minimumSize = windowSize
        maximumSize = windowSize
        isVisible = false
        isModal = true
        val box = Box.createVerticalBox()
        box.add(Box.createVerticalStrut(10))
        box.add(settingPane)
        box.add(Box.createVerticalStrut(10))
        box.add(buttonGroup)
        contentPane = box

    }

    override fun isExitOnApprove(): Boolean {
        return exitOnApprove
    }

    override fun showDialog() {
        JComponentInitializer.alignCenter(this)
        // 更新resourceComboBox显示
        val model = resourceComboBox.model as DefaultComboBoxModel
        model.removeAllElements()
        val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
        val resources = resourceBrowser.getAllResources()
        // 2021-6-29 BUGFIX 使用Kotlin扩展方法导致运行时异常
        if (resources.isNotEmpty()) {
            resources.forEach {
                model.addElement(it.name)
            }
            resourceComboBox.selectedIndex = 0
        }

        exitOnApprove = false
        isVisible = true
    }

    override fun closeDialog() {
       isVisible = false
    }

    // ------------------------- 业务方法 ---------------------------

    fun getAnalyseKeyFrame(): AnalyseKeyFrame {
        return AnalyseKeyFrame(nameTextField.text, resourceComboBox.selectedItem as String)
    }

    fun getAnalyseKeyFrame(frame: AnalyseKeyFrame): AnalyseKeyFrame {
        frame.name = nameTextField.text
        frame.resourceName = resourceComboBox.selectedItem as String
        return frame
    }
}