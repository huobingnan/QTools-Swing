package cspace.ui

import cspace.ApplicationStarter
import cspace.component.ResourceBrowserPopupMenu
import cspace.dialog.ResourceDetailDialog
import cspace.frame.MainFrame
import cspace.model.Resource
import cspace.util.JComponentInitializer
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

/**
 * 资源浏览视图
 * @author huobn
 */
class ResourceBrowserView: JPanel() {

    private val logger = LoggerFactory.getLogger(ResourceBrowserView::class.java)

    // 资源显示列表
    private val resourceListBrowser: JList<Resource> by lazy {
        val model = DefaultListModel<Resource>()
        val list = JList<Resource>(model)
        list.selectionMode = DefaultListSelectionModel.SINGLE_SELECTION
        list
    }

    private val resourceListBrowserScrollPane: JScrollPane by lazy {
        val scrollPane = JScrollPane(resourceListBrowser)
        scrollPane.preferredSize = Dimension(200, 600)
        scrollPane
    }

    init {
        layout = BorderLayout()
        add(resourceListBrowserScrollPane, BorderLayout.CENTER)
    }

    // 事件的初始化
    init {
        // 设置右键弹出菜单
        resourceListBrowser.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(event: MouseEvent?) {
                super.mousePressed(event)
                if (event!!.button == MouseEvent.BUTTON3) {
                    val popup = ApplicationStarter.getContext().getInstance(ResourceBrowserPopupMenu::class.java)
                    popup.show(this@ResourceBrowserView, event.x, event.y)
                }

            }
        })

        // 设置双击查看资源详情
        resourceListBrowser.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(event: MouseEvent?) {
                super.mouseClicked(event)
                if (event!!.button == MouseEvent.BUTTON1 && event.clickCount == 2) {
                    val resource = getSelectedResource()
                    if (resource != null) {
                        val dialog = ApplicationStarter.getContext().getInstance(ResourceDetailDialog::class.java)
                        dialog.acceptResource(resource)
                        JComponentInitializer.showDialogSupport(dialog)
                    }
                }
            }
        })
    }


    private fun checkResourceDuplication(resource: Resource, listModel: DefaultListModel<Resource>): Boolean {
        var i = 0
        val size = listModel.size()
        // 检查是否重复
        var isDuplicated = false
        while (i < size) {
            if ((listModel[i] as Resource).name == resource.name) {
                // 重复
                isDuplicated = true
                break
            }
            i++
        }
        return isDuplicated
    }

    // --------------------------------------- 业务逻辑方法 ------------------------------------------

    /**
     * 接收一个Resource对象，然后更新UI显示
     * @author huobn
     */
    fun acceptResource(resource: Resource) {
        val listModel = (resourceListBrowser.model as DefaultListModel)
        val renameDialogParent = ApplicationStarter.getContext().getInstance(MainFrame::class.java)
        // 检查Resource是否重新导入了
        while (checkResourceDuplication(resource, listModel)) {
            val inputString = JOptionPane.showInputDialog(
                renameDialogParent, "new name", "duplicated resource",
                JOptionPane.WARNING_MESSAGE
            )
            resource.name = "$inputString [${resource.type}]"
        }
        listModel.addElement(resource) // 添加resource

    }

    /**
     * 获取选中的Resource对象。
     * 当ResourceBrowser为空，或者未选中任何Resource时返回null
     * @author huobn
     */
    fun getSelectedResource(): Resource? {
        return resourceListBrowser.selectedValue
    }

    /**
     * 获取所有的Resource
     * @author huobn
     */
    fun getAllResources(): List<Resource> {
        val model = resourceListBrowser.model as DefaultListModel
        if (model.isEmpty) return emptyList()
        return model.elements().toList()
    }

    fun getResourceSearchTable(): Map<String, Resource> {
        val searchTable = HashMap<String, Resource>()
        getAllResources().forEach {
            searchTable[it.name] = it
        }
        return searchTable
    }

    /**
     * 删除资源
     * @author huobn
     */
    fun deleteResource(resource: Resource) {
        val model = resourceListBrowser.model as DefaultListModel
        model.removeElement(resource)
    }

}