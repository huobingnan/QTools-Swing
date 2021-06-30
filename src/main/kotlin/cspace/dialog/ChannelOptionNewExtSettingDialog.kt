package cspace.dialog

import cspace.constants.ApplicationDialogConstants
import cspace.model.ExtChannelSettings
import cspace.util.DialogSupport
import java.awt.*
import java.awt.event.ItemEvent
import javax.swing.*

/**
 * 通道选项新建额外配置对话框
 * @author huobn
 */
class ChannelOptionNewExtSettingDialog: JDialog(), DialogSupport {

    private var exitOnApprove = false

    private val BOND_LENGTH_MAX_DESCRIPTION = """
        Description:
            The maximum bond length parameter is used to set the maximum bond length in the chemical bond search of the system.
        Example:
            setting key   : bond length max
            setting value : 2.0
        Value constrain:
            setting value must be a real number.
    """.trimIndent()

    private val BOND_NUMBER_CONSTRAIN_DESCRIPTION = """
        Description:
            Limit the number of bonds each component can form in chemical bond search
        Example:
            setting key   : bond number constrain
            setting value : C;H;N
        Value constrain:
            The value must be a valid chemical element symbol, used ';' between multiple elements separated
    """.trimIndent()
    private val settingKeyComboBox: JComboBox<String> by lazy {
        val comboBox = JComboBox<String>()
        val model = comboBox.model as DefaultComboBoxModel
        model.addElement(ExtChannelSettings.BOND_LENGTH_MAX)
        model.addElement(ExtChannelSettings.BOND_NUMBER_CONSTRAIN)
        comboBox.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                when (it.item) {
                    ExtChannelSettings.BOND_LENGTH_MAX -> {
                        settingKeyDescriptionArea.text = BOND_LENGTH_MAX_DESCRIPTION
                    }
                    ExtChannelSettings.BOND_NUMBER_CONSTRAIN -> {
                        settingKeyDescriptionArea.text = BOND_NUMBER_CONSTRAIN_DESCRIPTION
                    }
                }
            }
        }
        comboBox.selectedIndex = 0
        settingKeyDescriptionArea.text = BOND_LENGTH_MAX_DESCRIPTION
        comboBox
    }

    private val settingValueTextField: JTextField by lazy {
        val textField = JTextField()

        textField
    }
    // 对设置项进行描述的文字栏
    private val settingKeyDescriptionArea: JTextArea by lazy {
        val textArea = JTextArea()
        textArea.isEditable = false
        textArea.size = Dimension(200, 150)
        textArea.preferredSize = textArea.size
        textArea.lineWrap = true
        textArea.font = Font(Font.SANS_SERIF, Font.ITALIC, 13)
        textArea
    }

    private val applyButton: JButton by lazy {
        val button = JButton("apply")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button.addActionListener {
            exitOnApprove = true
            isVisible = false
        }
        button
    }

    private val cancelButton: JButton by lazy {
        val button = JButton("cancel")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button.addActionListener {
            exitOnApprove = false
            isVisible = false
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
        pane.add(applyButton)
        pane
    }

    private val functionalPane: JPanel by lazy {
        val pane = JPanel()
        val layout = GridLayout(2, 2, 0, 10)
        val keyLabel = JLabel("setting key:")
        val valueLabel = JLabel("setting value:")
        pane.size = Dimension(100, 50)
        pane.layout = layout
        pane.add(keyLabel)
        pane.add(settingKeyComboBox)
        pane.add(valueLabel)
        pane.add(settingValueTextField)
        pane
    }

    init {
        title = "setting option"
        isModal = true
        isAlwaysOnTop = true
        size = Dimension( 380, 350)
        preferredSize = size
        isResizable = false
        val box = Box.createVerticalBox()
        box.add(functionalPane)
        box.add(JScrollPane(settingKeyDescriptionArea))
        box.add(buttonGroup)
        contentPane = box
        isVisible = false
    }

    override fun isExitOnApprove(): Boolean {
        return exitOnApprove
    }

    override fun showDialog() {
        settingValueTextField.selectAll()
        exitOnApprove = false
        isVisible = true
    }

    override fun closeDialog() {
        isVisible = false
    }

    // ------------------ 业务方法 ------------------
    fun refreshUI(pair: Pair<String, String>) {
        acceptSettingPair(pair)
    }

    fun acceptSettingPair(pair: Pair<String, String>) {
        settingKeyComboBox.selectedItem = pair.first
        settingValueTextField.text = pair.second
    }

    fun getSettingPair(): Pair<String, String> {
        return Pair(settingKeyComboBox.selectedItem as String, settingValueTextField.text)
    }
}