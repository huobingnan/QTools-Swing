package cspace.dialog

import cspace.ApplicationStarter
import cspace.frame.MainFrame
import cspace.model.Contcar
import cspace.model.GaussianLog
import cspace.model.Resource
import cspace.util.DialogSupport
import cspace.util.JComponentInitializer
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.*
import javax.swing.border.TitledBorder
import javax.swing.table.DefaultTableModel
import javax.swing.table.JTableHeader

/**
 * 资源详情对话框
 * @author huobn
 */
class ResourceDetailDialog: JDialog(), DialogSupport{

    // 头部信息展示表
    private val headerInformationTable: JTable by lazy {
        val columnNameArray = arrayOf("property", "value")
        val tableModel = DefaultTableModel(null, columnNameArray)
        val table = object: JTable(tableModel) {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                return false
            }
        }
        table.tableHeader.resizingAllowed = true
        table.tableHeader.reorderingAllowed = false
        table.tableHeader.font = Font(Font.SANS_SERIF, Font.PLAIN, 15)
        // 调整JTable
        table.rowHeight = table.rowHeight + 5
        table.setShowGrid(true)
        table
    }

    // 文件头部信息展示面板
    private val headerInformationPane: JPanel by lazy {
        val pane = JPanel()
        pane.preferredSize = Dimension(600, 200)
        pane.size = Dimension(600, 200)
        pane.layout = BorderLayout()
        pane.border = TitledBorder("header")
        pane.add(JScrollPane(headerInformationTable), BorderLayout.CENTER)
        pane
    }

    private val coordinateInformationTable: JTable by lazy {
        val columnNameArray = arrayOf("component", "coordinate")
        val tableModel = DefaultTableModel(null, columnNameArray)
        val table = object: JTable(tableModel) {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                return false
            }
        }
        table.tableHeader.resizingAllowed = true
        table.tableHeader.reorderingAllowed = false
        table.tableHeader.font = Font(Font.SANS_SERIF, Font.PLAIN, 15)
        table.rowHeight = table.rowHeight + 5
        table.setShowGrid(true)
        //table.getColumn("component").preferredWidth = 50
        table
    }

    // 坐标信息展示面板
    private val coordinateInformationPane: JPanel by lazy {
        val pane = JPanel()
        pane.preferredSize = Dimension(600, 300)
        pane.size = Dimension(600, 300)
        pane.layout = BorderLayout()
        pane.border = TitledBorder("coordinate")
        pane.add(JScrollPane(coordinateInformationTable), BorderLayout.CENTER)
        pane
    }

    private val approveButton: JButton by lazy {
        val button = JButton("ok")
        button.preferredSize = Dimension(80, 35)
        button.size = Dimension(80, 35)
        button.addActionListener {
            this@ResourceDetailDialog.isVisible = false
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
        pane.add(approveButton)
        pane
    }

    private val topLevelContainer: Box by lazy {
        val vBox = Box.createVerticalBox()
        vBox.add(Box.createVerticalStrut(10))
        vBox.add(headerInformationPane)
        vBox.add(Box.createVerticalStrut(10))
        vBox.add(coordinateInformationPane)
        vBox.add(Box.createVerticalStrut(10))
        vBox.add(buttonGroup)
        vBox
    }

    init {
        isVisible = false
        isModal = true
        title = "information"
        preferredSize = Dimension(600, 700)
        setLocationRelativeTo(null)
        size = Dimension(600, 700)
        layout = BorderLayout()
        add(topLevelContainer, BorderLayout.CENTER)
    }

    // 填充高斯文件的信息
    private fun fillGaussianInformation(resource: Resource) {
        val instance = resource.instance as GaussianLog
        val headerTableModel = headerInformationTable.model as DefaultTableModel
        // 清空显示
        headerTableModel.dataVector.setSize(0)
        headerTableModel.addRow(
            arrayOf("input", instance.input)
        )
        headerTableModel.addRow(
            arrayOf("output", instance.output)
        )
        headerTableModel.addRow(
            arrayOf("routine", instance.routine)
        )
        headerTableModel.addRow(
            arrayOf("calculation level", instance.calculationLevel)
        )
        headerTableModel.addRow(
            arrayOf("calculation basic group", instance.calculationBasicGroup)
        )
        headerTableModel.addRow(
            arrayOf("maximum force convergence", if (instance.maximumForceReached) "YES" else "NO")
        )
        headerTableModel.addRow(
            arrayOf("RMS force convergence", if (instance.RMSForceReached) "YES" else "NO")
        )
        headerTableModel.addRow(
            arrayOf("maximum displacement convergence", if (instance.maximumDisplacementReached) "YES" else "NO")
        )
        headerTableModel.addRow(
            arrayOf("RMS displacement convergence", if (instance.RMSDisplacementReached) "YES" else "NO")
        )
        // 填充坐标信息
        val coordinateTableModel = coordinateInformationTable.model as DefaultTableModel
        coordinateTableModel.dataVector.setSize(0)
        val atomCoordinateList = instance.lastAtomCoordinateList
        atomCoordinateList.forEach{
            coordinateTableModel.addRow(arrayOf("${it.symbol}${it.sequenceNumber}", "[${it.x}, ${it.y}, ${it.z}]"))
        }
    }

    // 在UI界面上填充VASP文件信息
    private fun fillVASPInformation(resource: Resource) {
        val instance = resource.instance as Contcar
        val headerInformationTableModel = headerInformationTable.model as DefaultTableModel
        // 清空原有显示， 填充头部信息
        headerInformationTableModel.dataVector.setSize(0)
        headerInformationTableModel.addRow(arrayOf("name", instance.name))
        headerInformationTableModel.addRow(arrayOf("scale",instance.scale))
        headerInformationTableModel.addRow(arrayOf("component amount",instance.componentAmount))
        headerInformationTableModel.addRow(
            arrayOf("vector a", "[${instance.matrix!![0][0]}, ${instance.matrix!![1][0]}, ${instance.matrix!![2][0]}]")
        )
        headerInformationTableModel.addRow(
            arrayOf("vector b", "[${instance.matrix!![0][1]}, ${instance.matrix!![1][1]}, ${instance.matrix!![2][1]}]")
        )
        headerInformationTableModel.addRow(
            arrayOf("vector c", "[${instance.matrix!![0][2]}, ${instance.matrix!![1][2]}, ${instance.matrix!![2][2]}]")
        )
        headerInformationTableModel.addRow(arrayOf("coordinate type", instance.coordinateType.toString()))

        val coordinateInformationTableModel = coordinateInformationTable.model as DefaultTableModel
        // 清空原有显示，填充坐标信息
        coordinateInformationTableModel.dataVector.setSize(0)
        // 转换为原子坐标形式
        val atomCoordinateList = Resource.convertCoordinateToCommonType(resource)
        atomCoordinateList.forEach {
            // 坐标的精确度保留到小数点后十位
            val coordinateFormat = String.format("[%.10f, %.10f, %.10f]", it.x, it.y, it.z)
            coordinateInformationTableModel.addRow(
                arrayOf("${it.symbol}${it.sequenceNumber}", coordinateFormat)
            )
        }

    }

    // --------------------------------- 业务方法 --------------------------------

    fun acceptResource(resource: Resource) {
        if (resource.type == Resource.VASP) {
            fillVASPInformation(resource)
        } else if (resource.type == Resource.GAUSSIAN) {
            fillGaussianInformation(resource)
        }
    }

    override fun isExitOnApprove(): Boolean {
        return false
    }

    override fun showDialog() {
        JComponentInitializer.alignCenter(this)
        isVisible = true
    }

    override fun closeDialog() {
        isVisible = false
    }
}