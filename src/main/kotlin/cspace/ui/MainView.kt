package cspace.ui

import cspace.ApplicationStarter
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.SwingUtilities

/**
 * 主视图类
 * @author huobingnan
 */
class MainView: JPanel() {

    private val resourceAndDisplaySplitPane: JSplitPane by lazy {
        val pane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT) // 水平分隔
        pane.leftComponent = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
        pane.rightComponent = ApplicationStarter.getContext().getInstance(DisplayView::class.java)
        pane
    }

    init {
        layout = BorderLayout() // 采用边界布局的方法

        // 添加组件
        add(resourceAndDisplaySplitPane, BorderLayout.CENTER)
        add(ApplicationStarter.getContext().getInstance(ChannelView::class.java), BorderLayout.SOUTH)
    }
}