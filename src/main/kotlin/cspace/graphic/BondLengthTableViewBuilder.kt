package cspace.graphic

import cspace.model.BondLengthResult
import java.awt.Font
import java.util.*
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class BondLengthTableViewBuilder: GraphBuilder {

    override fun build(result: Any): JComponent? {
        if (result is BondLengthResult) {

            val columnName = Vector<String>()
            columnName.addElement("bond name")
            result.analyseKeyFrameList.forEach {
                columnName.addElement(it.name)
            }
            val tableModel = DefaultTableModel(null, columnName)
            // 将将所有搜寻到的键取并集
            val finalBondSet: MutableSet<String> = TreeSet()
            for (stringDoubleHashMap in result.data) {
                finalBondSet.addAll(stringDoubleHashMap.keys)
            }

            for (key in finalBondSet) {
                val row = Vector<String>()
                row.addElement(key)
                for (map in result.data) {
                    val distance = map[key]
                    if (distance == null) {
                        row.addElement("-")
                    } else {
                        row.addElement(String.format("%.2f", distance))
                    }
                }
                tableModel.dataVector.addElement(row)
            }


            val table = object: JTable(tableModel) {
                override fun isCellEditable(row: Int, column: Int): Boolean {
                    return false
                }
            }
            table.tableHeader.reorderingAllowed = false
            table.tableHeader.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
            return JScrollPane(table)
        }else {
            return null
        }
    }
}