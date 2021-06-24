package cspace.dialog

import java.awt.BorderLayout
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.border.Border
import javax.swing.border.TitledBorder
import javax.swing.table.DefaultTableModel
import javax.swing.table.JTableHeader

/**
 * 资源详情对话框
 */
class ResourceDetailDialog: JDialog() {

    // 头部信息展示表
    private val headerInformationTable: JTable by lazy {
        val tableModel = DefaultTableModel()
        val table = JTable(tableModel)
        table.tableHeader = JTableHeader().apply {
            resizingAllowed = false
            reorderingAllowed = false
        }
        table
    }

    // 文件头部信息展示面板
    private val headerInformationPane: JPanel by lazy {
        val pane = JPanel()
        pane.layout = BorderLayout()
        pane.border = TitledBorder("header")
        pane.add(headerInformationTable, BorderLayout.CENTER)
        pane
    }

    init {

    }
}