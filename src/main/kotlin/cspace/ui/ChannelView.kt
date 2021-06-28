package cspace.ui

import com.kitfox.svg.app.MainFrame
import cspace.ApplicationStarter
import cspace.component.ChannelTab
import cspace.dialog.ChannelOptionDialog
import cspace.graphic.BondLengthTableViewBuilder
import cspace.model.BondLengthResult
import cspace.model.ChannelSetting
import cspace.support.BondLength
import cspace.util.AssetsResolver
import cspace.util.JComponentInitializer
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.border.TitledBorder

/**
 * 分析通道视图类
 * @author huobn
 */
class ChannelView: JPanel() {

    private val logger  = LoggerFactory.getLogger(ChannelView::class.java)

    // ----------------------------- 数据域 --------------------------------

    private val channelSettingCache = HashMap<String, ChannelSetting>()

    // 新建分析通道按钮
    private val newChannelButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.new-channel.icon")))
        button.toolTipText = "new channel"
        button.isOpaque = false
        button.addActionListener {
            val dialog = ApplicationStarter.getContext().getInstance(ChannelOptionDialog::class.java)
            dialog.refreshUI()
            // JComponentInitializer.showDialog(dialog)
            JComponentInitializer.showDialogSupport(dialog)
            if (!dialog.isExitOnApprove()) return@addActionListener
            var channelSetting = dialog.getChannelSetting()
            // channelName是索引一个Channel的唯一键值，所以要保证其唯一性（检测重复）
            while (dialog.isExitOnApprove() && channelSettingCache.containsKey(channelSetting.channelName)) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "duplicated channel name : ${channelSetting.channelName}",
                    "invalid input",
                    JOptionPane.ERROR_MESSAGE
                )
                dialog.refreshUI(channelSetting)
                JComponentInitializer.showDialogSupport(dialog)
                channelSetting = dialog.getChannelSetting(channelSetting)
            }
            if (!dialog.isExitOnApprove()) return@addActionListener
            channelSettingCache[channelSetting.channelName] = channelSetting // 写入到缓存
            //--------------------------- 更新UI显示 -------------------------------
            //!   1. 新建tabPane
            //!--------------------------------------------------------------------
            channelTabPane.addTab(channelSetting.channelName, newChannelTab())

        }
        button
    }

    // 执行按钮
    private val executeButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.execute.icon")))
        button.toolTipText = "execute"
        button.isOpaque = false
        button.addActionListener {
            //!--------------------- 执行分析 ------------------------
            //! 1. 获取当前tab框的关键帧内容
            //! 2. 获取当前通道的ChannelSetting信息
            //! 3. 获取所有的Resource信息
            //! 4. 根据分析通道设置，选择相应的分析策略
            //! 5. 将分析策略结果交由GraphBuilder处理，生成图像
            //! 6. 将图像展示在面板中
            //!------------------------------------------------------
            val selectedIndex = channelTabPane.selectedIndex
            if (selectedIndex < 0) return@addActionListener
            val channelName = channelTabPane.getTitleAt(selectedIndex)

            //  获取所有的关键帧
            val channelTab = channelTabPane.getComponentAt(selectedIndex) as ChannelTab
            val frameList = channelTab.getAnalyseKeyFrameList()

            //  获取channelSetting
            if (!channelSettingCache.containsKey(channelName)) return@addActionListener
            val channelSetting = channelSettingCache[channelName]!!

            // 获取所有的Resource
            val resourceBrowser = ApplicationStarter.getContext().getInstance(ResourceBrowserView::class.java)
            val resourceSearchTable = resourceBrowser.getResourceSearchTable()


            when (channelSetting.channelType) {
                ChannelSetting.TYPE_BOND_LENGTH -> {
                    // 分析键长
                    val result = BondLength.perform(frameList, resourceSearchTable, channelSetting)
                    val displayView = ApplicationStarter.getContext().getInstance(DisplayView::class.java)
                    val builder = ApplicationStarter.getContext().getInstance(BondLengthTableViewBuilder::class.java)
                    displayView.acceptGraphBuilder(builder, result)
                }
                else -> {
                    val parent = ApplicationStarter.getContext().getInstance(MainFrame::class.java)
                    JOptionPane.showMessageDialog(
                    parent,
                        "nonsupport channel type strategy \"${channelSetting.channelType}\"",
                        "error",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }

        }
        button
    }

    // 设置按钮
    private val settingButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.setting.icon")))
        button.toolTipText = "setting"
        button.isOpaque = false
        button.addActionListener {
            // 查看当前显示的tab面板
            val selectIndex = channelTabPane.selectedIndex
            if (selectIndex < 0) return@addActionListener
            // 获取到通道名称
            val channelName = channelTabPane.getTitleAt(selectIndex)
            if (!channelSettingCache.containsKey(channelName)) return@addActionListener
            var channelSetting = channelSettingCache[channelName]!!
            // 获取展示的对话框
            val channelOptionDialog = ApplicationStarter.getContext().getInstance(ChannelOptionDialog::class.java)
            channelOptionDialog.refreshUI(channelSetting)
            JComponentInitializer.showDialogSupport(channelOptionDialog)
            // 获取更新之后的channelSetting
            if (!channelOptionDialog.isExitOnApprove()) return@addActionListener
            channelSetting = channelOptionDialog.getChannelSetting(channelSetting)
            // 检查是否有重复
            while (channelOptionDialog.isExitOnApprove() &&
                (channelSetting.channelName != channelName && channelSettingCache.containsKey(channelSetting.channelName))
            ) {
                JOptionPane.showMessageDialog(
                    channelOptionDialog,
                    "duplicated channel name ${channelSetting.channelName}",
                    "invalid input",
                    JOptionPane.ERROR_MESSAGE
                )
                JComponentInitializer.showDialogSupport(channelOptionDialog)
                channelSetting = channelOptionDialog.getChannelSetting(channelSetting)
            }

            if (channelOptionDialog.isExitOnApprove()) {
                logger.info("更新channel setting : {}", channelSetting)
                //  更新缓存
                channelSettingCache[channelSetting.channelName] = channelSetting
                // 更新tab框
                channelTabPane.setTitleAt(selectIndex, channelSetting.channelName)
            }
        }
        button
    }

    // 删除分析通道
    private val deleteButton: JButton by lazy {
        val button = JButton(ImageIcon(AssetsResolver.getAsset("channel-view.delete.icon")))
        button.toolTipText = "delete"
        button.isOpaque = false
        button.addActionListener {
            // 查看当前显示的tab面板
            val selectIndex = channelTabPane.selectedIndex
            if (selectIndex < 0) return@addActionListener
            // 获取到通道名称
            val channelName = channelTabPane.getTitleAt(selectIndex)
            if (!channelSettingCache.containsKey(channelName)) return@addActionListener

            val parent = ApplicationStarter.getContext().getInstance(MainFrame::class.java)
            val choice = JOptionPane.showConfirmDialog(
                parent,
                "Are you sure to delete channel $channelName",
                "warning",
                JOptionPane.WARNING_MESSAGE
            )
            if (choice == JOptionPane.OK_OPTION) {
                channelSettingCache.remove(channelName)
                channelTabPane.removeTabAt(selectIndex)
            }
        }
        button
    }

    // 分析通道功能栏
    private val functionToolBar: JToolBar by lazy {
        val toolBar = JToolBar()
        toolBar.layout = FlowLayout().apply {
            alignment = FlowLayout.CENTER
        }
        toolBar.preferredSize = Dimension(65, 200)
        toolBar.size = Dimension(65, 200)
        toolBar.orientation = JToolBar.VERTICAL
        toolBar.isFloatable = false
        val box = Box.createVerticalBox()
        box.add(newChannelButton)
        box.add(Box.createVerticalStrut(10))
        box.add(settingButton)
        box.add(Box.createVerticalStrut(10))
        box.add(executeButton)
        box.add(Box.createVerticalStrut(10))
        box.add(deleteButton)
        box.add(Box.createVerticalStrut(10))
        toolBar.add(box)
        toolBar
    }


    private val channelTabPane: JTabbedPane by lazy {
        val tabPane = JTabbedPane()
        tabPane.border = TitledBorder("channel")
        tabPane
    }

    init {
        layout = BorderLayout()
        size = Dimension(1000, 200)
        preferredSize = Dimension(1000, 200)
        add(functionToolBar, BorderLayout.WEST)
        add(channelTabPane, BorderLayout.CENTER)
    }

    // ------------------------- 业务方法 -------------------------

    private fun newChannelTab(): JComponent {
       return ChannelTab()
    }

    fun getSelectedChannelSetting(): ChannelSetting? {
        val selectedIndex = channelTabPane.selectedIndex
        if (selectedIndex < 0) return null
        val channelName = channelTabPane.getTitleAt(selectedIndex)
        if (!channelSettingCache.containsKey(channelName)) return null
        return channelSettingCache[channelName]!!
    }

}