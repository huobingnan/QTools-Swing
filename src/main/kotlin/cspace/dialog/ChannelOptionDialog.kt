package cspace.dialog

import com.kitfox.svg.app.MainFrame
import cspace.ApplicationStarter
import cspace.component.ExtSettingNewPopupMenu
import cspace.constants.ApplicationDialogConstants
import cspace.model.ChannelSetting
import cspace.ui.MainView
import cspace.util.DialogSupport
import cspace.util.JComponentInitializer
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.lang.IllegalArgumentException
import javax.swing.*
import javax.swing.border.TitledBorder
import javax.swing.table.DefaultTableModel

/**
 * 通道选项对话框
 * @author huobn
 */
class ChannelOptionDialog: JDialog(), DialogSupport {


    private var exitOnApprove = false

    // ChannelOptionDialog的模型
    // 修改channelSetting的域并调用refreshUI方法后，界面会自动刷新
    private val channelSetting = ChannelSetting(
        "undefined",
        ChannelSetting.TYPE_BOND_LENGTH,
        "undefined",
        ChannelSetting.SHOW_TABLE_VIEW
    )
    // 分析通道名称输入框
    private val nameTextField: JTextField by lazy {
        val textField = JTextField()
        textField.preferredSize = Dimension(200, 35)
        textField.size = Dimension(200, 35)
        textField
    }
    // 分析通道类型
    private val typeComboBox: JComboBox<String> by lazy {
        val comboBox = JComboBox<String>()
        comboBox.isEditable = false
        val model = comboBox.model as DefaultComboBoxModel
        model.addElement(ChannelSetting.TYPE_BOND_LENGTH)
        model.addElement(ChannelSetting.TYPE_BOND_ANGLE)
        comboBox
    }
    // 展示区域选择框
    private val displayAreaComboBox: JComboBox<String> by lazy {
        val comboBox = JComboBox<String>()
        comboBox.isEditable = false
        val model = comboBox.model as DefaultComboBoxModel
        model.addElement(ChannelSetting.SHOW_AREA_DEFAULT) // 通过默认的形式展示结果
        model.addElement(ChannelSetting.SHOW_AREA_ALWAYS_NEW) // 总是新建面板展示结果
        comboBox
    }
    // 展示类型选择框
    private val displayTypeComboBox: JComboBox<String> by lazy {
        val comboBox = JComboBox<String>()
        comboBox.isEditable = false
        val model = comboBox.model as DefaultComboBoxModel
        model.addElement(ChannelSetting.SHOW_TABLE_VIEW)
        model.addElement(ChannelSetting.SHOW_BAR_CHART)
        model.addElement(ChannelSetting.SHOW_LINE_CHART)
        comboBox
    }
    // 基础设置栏
    private val basicSettingPane: JPanel by lazy {
        val pane = JPanel()
        pane.size = Dimension(500, 240)
        pane.preferredSize = Dimension(500, 240)
        pane.border = TitledBorder("basic settings")
        pane.layout = GridLayout(4, 2, -10, 15)
        pane.add(JLabel("channel name:"))
        pane.add(nameTextField)
        pane.add(JLabel("channel type:"))
        pane.add(typeComboBox)
        pane.add(JLabel("display area:"))
        pane.add(displayAreaComboBox)
        pane.add(JLabel("display type:"))
        pane.add(displayTypeComboBox)
        pane
    }

    // 额外设置表
    private val extSettingTable: JTable by lazy {
        val tableModel = DefaultTableModel(null, arrayOf("key", "value"))
        val table = object :JTable(tableModel) {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                return false
            }
        }
        table.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val event = e!!
                if (event.button == MouseEvent.BUTTON1 && event.clickCount == 2) {
                    val selectedRow = table.selectedRow
                    if (selectedRow < 0) return
                    val model = table.model as DefaultTableModel
                    val data = model.dataVector[selectedRow]
                    // 双击查看详情
                    val dialog = ApplicationStarter.getContext().getInstance(ChannelOptionNewExtSettingDialog::class.java)
                    dialog.refreshUI(Pair(data[0] as String, data[1] as String))
                    JComponentInitializer.showDialogSupport(dialog)
                    // 是否更新
                    if (!dialog.isExitOnApprove()) return
                    // 更新
                    val settingPair = dialog.getSettingPair()
                    model.setValueAt(settingPair.second, selectedRow, 1)

                }
            }
        })
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.rowHeight = table.rowHeight + 10
        table.toolTipText = "Right click to new setting"
        table.tableHeader.reorderingAllowed = false
        table.tableHeader.font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
        table
    }

    private val extSettingPane: JPanel by lazy {
        val pane = JPanel()
        pane.size = Dimension(500, 200)
        pane.layout = BorderLayout()
        pane.border = TitledBorder("additional setting")
        pane.preferredSize = Dimension(500, 200)
        val scrollPane = JScrollPane(extSettingTable)
        scrollPane.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e!!.button == MouseEvent.BUTTON3) {
                    val popupMenu = ApplicationStarter.getContext().getInstance(ExtSettingNewPopupMenu::class.java)
                    popupMenu.show(pane, e.x, e.y)
                }
            }
        })
        pane.add(scrollPane, BorderLayout.CENTER)
        pane
    }


    private val applyButton: JButton by lazy {
        val button = JButton("apply")
        button.size = ApplicationDialogConstants.BUTTON_SIZE
        button.preferredSize = ApplicationDialogConstants.BUTTON_SIZE
        button.addActionListener {
            // 更新模型对象
            channelSetting.channelName = nameTextField.text
            channelSetting.channelType = typeComboBox.selectedItem as String
            channelSetting.showArea = displayAreaComboBox.selectedItem as String
            channelSetting.showType = displayTypeComboBox.selectedItem as String
            try {
                ChannelSetting.validate(channelSetting) // 检查输入是否合法
                exitOnApprove = true
                this@ChannelOptionDialog.isVisible = false
            }catch (ex: IllegalArgumentException) {
                JOptionPane.showMessageDialog(this@ChannelOptionDialog, ex.message,
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
            this@ChannelOptionDialog.isVisible = false
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
        box.add(Box.createVerticalStrut(10))
        box.add(buttonGroup)
        contentPane = box

    }

    // ------------------------------- 业务方法 -------------------------------

    /**
     * 根据模型的值，更新UI的显示
     */
    fun refreshUI() {
        nameTextField.text = channelSetting.channelName
        typeComboBox.selectedItem = channelSetting.channelType
        displayAreaComboBox.selectedItem = channelSetting.showArea
        displayTypeComboBox.selectedItem = channelSetting.showType
        val model = extSettingTable.model as DefaultTableModel
        model.dataVector.setSize(0)
        channelSetting.extSetting.forEach { (k, v) ->
            model.addRow(arrayOf(k, v))
        }
    }

    /**
     * 根据外部输入值，更新UI显示
     */
    fun refreshUI(channelSetting: ChannelSetting) {
        nameTextField.text = channelSetting.channelName
        typeComboBox.selectedItem = channelSetting.channelType
        displayAreaComboBox.selectedItem = channelSetting.showArea
        displayTypeComboBox.selectedItem = channelSetting.showType
        val model = extSettingTable.model as DefaultTableModel
        model.dataVector.setSize(0)
        channelSetting.extSetting.forEach { (k, v) ->
            model.addRow(arrayOf(k, v))
        }
    }

    /**
     * 获取ChannelSetting实例对象
     * @author: huobn
     */
    fun getChannelSetting(): ChannelSetting {
        val model = extSettingTable.model as DefaultTableModel
        return ChannelSetting(
           nameTextField.text,
           typeComboBox.selectedItem as String,
           displayAreaComboBox.selectedItem as String,
           displayTypeComboBox.selectedItem as String,
        ).apply {
            extSetting.clear()
            model.dataVector.forEach {
                extSetting[it[0] as String] = it[1] as String
            }
        }
    }

    /**
     * 对获取ChannelSetting的方法进行重载
     * 接收一个channelSetting修改它的值，并返回修改之后的channelSetting
     * @author huobn
     */
    fun getChannelSetting(channelSetting: ChannelSetting): ChannelSetting {
        channelSetting.channelName = nameTextField.text
        channelSetting.channelType = typeComboBox.selectedItem as String
        channelSetting.showArea = displayAreaComboBox.selectedItem as String
        channelSetting.showType = displayTypeComboBox.selectedItem as String
        val model = extSettingTable.model as DefaultTableModel
        channelSetting.extSetting.clear()
        model.dataVector.forEach {
            channelSetting.extSetting[it[0] as String] = it[1] as String
        }
        return channelSetting
    }

    fun acceptExtChannelSettingPair(pair: Pair<String, String>) {
        val tableModel = extSettingTable.model as DefaultTableModel
        var duplicated = false
        var index = 0
        tableModel.dataVector.forEach {
            if (it[0] == pair.first) {
                duplicated = true
                return@forEach
            }
            index++
        }
        if (duplicated) {
            // 重复就不添加
            JOptionPane.showMessageDialog(
                this,
                "duplication setting key \"${pair.first}\"",
                "error",
                JOptionPane.ERROR_MESSAGE
            )
            extSettingTable.setRowSelectionInterval(index, index)
        } else {
            // 不重复就添加
            tableModel.addRow(arrayOf(pair.first, pair.second))
        }
    }

    // 删除选中的额外配置项
    fun deleteSelectedExtSetting() {
        if (!isVisible) return
        val selectedRow = extSettingTable.selectedRow ?: return
        if (selectedRow < 0) return
        val option = JOptionPane.showConfirmDialog(
            this,
            "are you sure to delete those settings?",
            "warning",
            JOptionPane.WARNING_MESSAGE
        )
        if (option != JOptionPane.OK_OPTION) return
        val model = extSettingTable.model as DefaultTableModel
        model.removeRow(selectedRow)
    }

    override fun isExitOnApprove(): Boolean {
        return exitOnApprove
    }

    override fun showDialog() {
        JComponentInitializer.alignCenter(this)
        exitOnApprove = false
        isVisible = true
    }

    override fun closeDialog() {
        isVisible = false
    }
}