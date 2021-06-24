package cspace.ui

import cspace.ApplicationStarter
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * 主视图类
 * @author huobingnan
 */
class MainView: JPanel() {


    init {
        layout = BorderLayout() // 采用边界布局的方法

        // 添加组件
        add(ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java), BorderLayout.WEST)
    }
}