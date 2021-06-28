package cspace.component

import cspace.ApplicationStarter
import cspace.dialog.ExceptionDialog
import cspace.dialog.ResourceDetailDialog
import cspace.frame.MainFrame
import cspace.model.Resource
import cspace.ui.MainView
import cspace.ui.ResourceBrowserView
import cspace.util.JComponentInitializer
import cspace.support.ContcarFileParser
import cspace.support.GaussianFileParser
import java.awt.Desktop
import java.lang.Exception
import javax.swing.*

/**
 * 资源浏览界面的右键点击菜单
 * @author huobn
 */
class ResourceBrowserPopupMenu: JPopupMenu() {

    private val importVaspResourceMenuItem: JMenuItem by lazy {
        val item = JMenuItem("vasp")
        // 导入VASP文件
        item.addActionListener {
            val fileChooser = JFileChooser()
            fileChooser.isMultiSelectionEnabled = true
            val fileChooserParentComponent = ApplicationStarter.getContext().getInstance(MainView::class.java)
            val result = fileChooser.showOpenDialog(fileChooserParentComponent)
            if (result != JFileChooser.APPROVE_OPTION) return@addActionListener
            // 可以多选
            val contcarFileList = fileChooser.selectedFiles
            if (contcarFileList == null || contcarFileList.isEmpty()) return@addActionListener
            contcarFileList.forEach {
                // 解析文件
                try {
                    val contcar = ContcarFileParser.parse(it)
                    // 构造Resource对象
                    val resource = Resource()
                    resource.name = "${it.nameWithoutExtension} [${Resource.VASP}]"
                    resource.instance = contcar
                    resource.type = Resource.VASP
                    resource.associatedFile = it
                    // 更新ResourceBrowser的显示
                    val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
                    resourceBrowser.acceptResource(resource)
                }catch (ex: Exception) {
                    val exceptionDialog = ApplicationStarter.getContext().getInstance(ExceptionDialog::class.java)
                    exceptionDialog.acceptException(ex)
                    JComponentInitializer.showDialogSupport(exceptionDialog)
                }
            }

        }
        item
    }

    private val importGaussianResourceMenuItem: JMenuItem by lazy {
        val item = JMenuItem("gaussian")

        item.addActionListener {
            // 导入高斯文件
            val fileChooser = JFileChooser()
            val fileChooserParentComponent = ApplicationStarter.getContext().getInstance(MainView::class.java)
            val result = fileChooser.showOpenDialog(fileChooserParentComponent)
            if (result != JFileChooser.APPROVE_OPTION) return@addActionListener
            val gaussianFile = fileChooser.selectedFile
            try {
                val gaussianLog = GaussianFileParser.parse(gaussianFile)
                // 构造Resource对象
                val resource = Resource()
                resource.name = "${gaussianFile.nameWithoutExtension} [${Resource.GAUSSIAN}]"
                resource.instance = gaussianLog
                resource.type = Resource.GAUSSIAN
                resource.associatedFile = gaussianFile
                val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
                resourceBrowser.acceptResource(resource)
            }catch (ex: Exception) {
                val exceptionDialog = ApplicationStarter.getContext().getInstance(ExceptionDialog::class.java)
                exceptionDialog.acceptException(ex)
                JComponentInitializer.showDialogSupport(exceptionDialog)
            }
        }
        item
    }

    private val importMenu: JMenu by lazy {
        val item = JMenu("Import")
        item.add(importVaspResourceMenuItem)
        item.add(importGaussianResourceMenuItem)
        item
    }

    // 展示资源详情
    private val detailMenu: JMenuItem by lazy {
        val item = JMenuItem("Detail")
        item.addActionListener {
            val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
            val resource = resourceBrowser.getSelectedResource()
            if (resource != null) {
                val dialog = ApplicationStarter.getContext().getInstance(ResourceDetailDialog::class.java)
                dialog.acceptResource(resource)
                JComponentInitializer.showDialogSupport(dialog)
            }
        }
        item
    }

    // 删除文件
    private val deleteMenu: JMenuItem by lazy {
        val item = JMenuItem("Delete")
        item.addActionListener {
            val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
            val resource = resourceBrowser.getSelectedResource() ?: return@addActionListener
            val parentComponent = ApplicationStarter.getContext().getInstance(MainFrame::class.java)
            val confirmation = JOptionPane.showConfirmDialog(
                parentComponent,
                "Are you sure to delete ${resource.name}?", "warning", JOptionPane.WARNING_MESSAGE
            )
            if (confirmation == JOptionPane.OK_OPTION) {
                resourceBrowser.deleteResource(resource)
            }
        }
        item
    }

    // 重新加载文件
    private val reloadMenu: JMenuItem by lazy {
        val item = JMenuItem("Reload")
        item.addActionListener {
            val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
            val resource = resourceBrowser.getSelectedResource() ?: return@addActionListener
            reloadResource(resource) // 重新加载
            val parentComponent = ApplicationStarter.getContext().getInstance(MainFrame::class.java)
            JOptionPane.showMessageDialog(parentComponent, "reload successfully!", "success", JOptionPane.INFORMATION_MESSAGE)
        }
        item
    }

    // 在文件浏览器中打开
    private val openInFileExplore: JMenuItem by lazy {
        var fileExploreName = "File Explore"
        val osName = System.getProperty("os.name").toLowerCase()
        if (osName.startsWith("mac")) {
            fileExploreName = "Finder"
        }
        val item = JMenuItem(fileExploreName)
        item.addActionListener {
            // 获取到ResourceBrowser对象
            val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
            val resource = resourceBrowser.getSelectedResource()
            if (resource != null) {
                Desktop.getDesktop().browseFileDirectory(resource.associatedFile)
            }
        }
        item
    }

    // 以默认的方式打开
    private val openInDefaultSoftware: JMenuItem by lazy {
        val item = JMenuItem("Default software")
        item.addActionListener {
            val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
            val resource = resourceBrowser.getSelectedResource()
            if (resource != null) {
                Desktop.getDesktop().open(resource.associatedFile)
            }
        }
        item
    }

    // 文件打开
    private val openInMenu: JMenu by lazy {
        val menu = JMenu("Open in")
        menu.add(openInFileExplore)
        menu.add(openInDefaultSoftware)
        menu
    }

    init {
        // 添加MenuItem
        add(importMenu)
        add(detailMenu)
        add(deleteMenu)
        add(reloadMenu)
        add(openInMenu)
    }

    // ----------------------------- 业务方法 -------------------------------
    private fun reloadResource(resource: Resource) {
        if (resource.type == Resource.VASP) {
            try {
                val file = resource.associatedFile
                val result = ContcarFileParser.parse(file!!)
                resource.instance = result // 刷新
            }catch (ex: Exception) {
                val exceptionDialog = ApplicationStarter.getContext().getInstance(ExceptionDialog::class.java)
                exceptionDialog.acceptException(ex)
                JComponentInitializer.showDialogSupport(exceptionDialog)
            }
        }else if (resource.type == Resource.GAUSSIAN) {
            try {
                val file = resource.associatedFile
                val result = GaussianFileParser.parse(file!!)
                resource.instance = resource
            }catch (ex: Exception) {
                val exceptionDialog = ApplicationStarter.getContext().getInstance(ExceptionDialog::class.java)
                exceptionDialog.acceptException(ex)
                JComponentInitializer.showDialogSupport(exceptionDialog)
            }
        }
    }
}